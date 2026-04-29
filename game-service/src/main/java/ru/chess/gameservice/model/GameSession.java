package ru.chess.gameservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    private String gameId;
    private String whitePlayerId;
    private String blackPlayerId;
    private String currentTurn;
    private String fen;
    private String lastMove;
    private boolean gameOver;
    private String winner;
    private List<String> moveHistory;
    private Instant startedAt;
    private Instant finishedAt;
    private boolean active;

    public GameSession(String gameId, String whitePlayerId, String blackPlayerId) {
        this.gameId = gameId;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.currentTurn = "w";  // белые ходят первыми
        this.fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        this.gameOver = false;
        this.active = true;
        this.moveHistory = new ArrayList<>();
        this.startedAt = Instant.now();
    }
}