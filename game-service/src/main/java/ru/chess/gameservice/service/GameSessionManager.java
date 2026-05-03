package ru.chess.gameservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.chess.gameservice.client.DataStoreClient;
import ru.chess.gameservice.dto.GameResultDTO;
import ru.chess.gameservice.model.GameSession;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameSessionManager {

    private final Map<String, GameSession> activeGames = new ConcurrentHashMap<>();
    private final DataStoreClient dataStoreClient;

    /**
     * Создать новую игровую сессию
     */
    public GameSession createGame(String gameId, String whitePlayerId, String blackPlayerId) {
        GameSession session = new GameSession(gameId, whitePlayerId, blackPlayerId);
        activeGames.put(gameId, session);
        log.info("Game session created: {} - {} vs {}", gameId, whitePlayerId, blackPlayerId);
        return session;
    }

    /**
     * Получить активную игру
     */
    public GameSession getGame(String gameId) {
        return activeGames.get(gameId);
    }

    /**
     * Обновить состояние игры
     */
    public void updateGame(GameSession session) {
        activeGames.put(session.getGameId(), session);
    }

    /**
     * Завершить игру и сохранить результат в БД
     */
    public void endGame(String gameId, String winnerId, String reason) {

        log.info("=== END GAME CALLED ===");
        log.info("GameId: {}, Winner: {}, Reason: {}", gameId, winnerId, reason);

        GameSession session = activeGames.remove(gameId);
        if (session == null) {
            log.warn("Game session not found: {}", gameId);
            return;
        }

        session.setGameOver(true);
        session.setWinner(winnerId);
        session.setFinishedAt(Instant.now());
        session.setActive(false);

        // Сохраняем результат в DataStore
        GameResultDTO result = new GameResultDTO();
        result.setWhitePlayerId(session.getWhitePlayerId());
        result.setBlackPlayerId(session.getBlackPlayerId());
        result.setWinnerId(winnerId);
        result.setMoves(String.join(",", session.getMoveHistory()));
        result.setStartedAt(session.getStartedAt().toString());
        result.setFinishedAt(session.getFinishedAt().toString());

        log.info("Saving game result: {} vs {}, moves: {}",
                session.getWhitePlayerId(),
                session.getBlackPlayerId(),
                session.getMoveHistory().size());

        dataStoreClient.saveGameResult(result);

        log.info("Game ended: {} - winner: {} ({})", gameId, winnerId, reason);
    }
}