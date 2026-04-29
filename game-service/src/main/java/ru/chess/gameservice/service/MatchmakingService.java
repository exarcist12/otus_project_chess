package ru.chess.gameservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.chess.gameservice.model.MatchFoundEvent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
public class MatchmakingService {
    private final Queue<String> waitingPlayers = new ConcurrentLinkedQueue<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final GameSessionManager gameSessionManager;
    private final AtomicLong roomCounter = new AtomicLong(1);
    public void addToQueue(String playerId) {
        waitingPlayers.offer(playerId);
        tryMatch();
    }


    private void tryMatch() {
        if (waitingPlayers.size() >= 2) {
            String player1 = waitingPlayers.poll();
            String player2 = waitingPlayers.poll();
            String roomId = String.valueOf(roomCounter.getAndIncrement());
            gameSessionManager.createGame(roomId, player1, player2);
            messagingTemplate.convertAndSend("/topic/match-found",
                    new MatchFoundEvent(roomId, List.of(player1, player2)));
        }
    }
}