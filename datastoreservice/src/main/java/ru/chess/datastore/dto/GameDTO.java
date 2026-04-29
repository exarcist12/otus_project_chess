package ru.chess.datastore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long id;
    private String whitePlayerId;
    private String blackPlayerId;
    private String winnerId;
    private String moves;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}