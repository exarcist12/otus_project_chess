package ru.chess.gameservice.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class GameResultDTO {
    private String whitePlayerId;
    private String blackPlayerId;
    private String winnerId;
    private String moves;
    private String  startedAt;
    private String  finishedAt;
}