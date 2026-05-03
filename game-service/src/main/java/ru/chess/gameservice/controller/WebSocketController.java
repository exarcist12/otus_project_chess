package ru.chess.gameservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.chess.gameservice.client.DataStoreClient;
import ru.chess.gameservice.dto.GameResultDTO;
import ru.chess.gameservice.dto.LoginRequestDTO;
import ru.chess.gameservice.dto.MoveRequest;
import ru.chess.gameservice.dto.PlayerDTO;
import ru.chess.gameservice.model.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.chess.gameservice.model.GameSession;
import ru.chess.gameservice.service.ChessEngine;
import ru.chess.gameservice.service.GameSessionManager;
import ru.chess.gameservice.service.MatchmakingService;
import ru.chess.gameservice.service.SessionService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DataStoreClient dataStoreClient;
    private final SessionService sessionService;
    private final MatchmakingService matchmakingService;
    private final GameSessionManager gameSessionManager;
    private final ChessEngine chessEngine;


    @MessageMapping("/find-match")
    public void findMatch(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        if (!sessionService.isAuthenticated(sessionId)) return;
        PlayerDTO player = sessionService.getPlayerInfo(sessionId);
        matchmakingService.addToQueue(player.getId());
    }


    @MessageMapping("/chat-room")
    public void handleRoomChat(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.info("Chat from session: {}", sessionId);

        PlayerDTO sender = sessionService.getPlayerInfo(sessionId);
        log.info("Sender from sessionService: {}", sender);

        if (sender == null) {
            log.warn("No player info for session: {}", sessionId);
            return;
        }

        message.setSender(sender.getName());
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/login")
    public void login(@Payload LoginRequestDTO loginRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.info("=== LOGIN ATTEMPT: {} ===", loginRequest.getUsername());

        PlayerDTO player = dataStoreClient.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("success", player != null);
        response.put("player", player);

        messagingTemplate.convertAndSend("/topic/login-response", response);

        if (player != null) {
            sessionService.setAuthenticated(sessionId, true);
            sessionService.registerPlayer(sessionId, player);
            log.info("User {} logged in successfully", player.getName());
        } else {
            log.warn("Failed login attempt for {}", loginRequest.getUsername());
        }
    }

    private void sendError(String sessionId, String message) {
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/errors", message);
    }

    @MessageMapping("/move")
    public void makeMove(@Payload MoveRequest moveRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        PlayerDTO player = sessionService.getPlayerInfo(sessionId);

        if (player == null) {
            log.warn("Unauthorized move attempt");
            return;
        }

        GameSession session = gameSessionManager.getGame(moveRequest.getRoomId());
        if (session == null || !session.isActive()) {
            sendError(sessionId, "Game not found");
            return;
        }


        boolean isWhiteTurn = "w".equals(session.getCurrentTurn());
        boolean isWhitePlayer = session.getWhitePlayerId().equals(player.getId());
        if ((isWhiteTurn && !isWhitePlayer) || (!isWhiteTurn && isWhitePlayer)) {
            sendError(sessionId, "Not your turn");
            return;
        }



        log.info("Validating move: {} -> {}", moveRequest.getFrom(), moveRequest.getTo());
        if (!chessEngine.isValidMove(session.getFen(), moveRequest.getFrom(), moveRequest.getTo())) {
            sendError(sessionId, "Invalid move");
            log.warn("Invalid move rejected: {} -> {}", moveRequest.getFrom(), moveRequest.getTo());
            return;
        }
        log.info("Move is valid, applying...");

        String newFen = chessEngine.applyMove(session.getFen(), moveRequest.getFrom(), moveRequest.getTo(), null);

        session.getMoveHistory().add(moveRequest.getFrom() + moveRequest.getTo());
        session.setFen(newFen);
        session.setLastMove(moveRequest.getFrom() + moveRequest.getTo());
        session.setCurrentTurn("w".equals(session.getCurrentTurn()) ? "b" : "w");

        gameSessionManager.updateGame(session);

        if (chessEngine.isGameOver(newFen)) {
            String winnerId = chessEngine.isCheck(newFen)
                    ? (session.getCurrentTurn().equals("w") ? session.getBlackPlayerId() : session.getWhitePlayerId())
                    : null;

            gameSessionManager.endGame(moveRequest.getRoomId(), winnerId,
                    winnerId == null ? "STALEMATE" : "CHECKMATE");

            log.info("Game over! Winner: {}", winnerId == null ? "Draw (stalemate)" : winnerId);
        }

        messagingTemplate.convertAndSend("/topic/game/" + moveRequest.getRoomId(), session);
    }


    @MessageMapping("/test-save")
    public void testSave() {
        GameResultDTO result = new GameResultDTO();
        result.setWhitePlayerId("player1");
        result.setBlackPlayerId("player2");
        result.setWinnerId("player1");
        result.setMoves("e2e4,e7e5");
        result.setStartedAt(Instant.now().toString());
        result.setFinishedAt(Instant.now().toString());
        dataStoreClient.saveGameResult(result);
        log.info("Test save executed");
    }
}