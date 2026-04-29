package ru.chess.gameservice.dto;

import lombok.Data;

@Data
public class MoveRequest {
    private String roomId;
    private String from;
    private String to;
    private String promotion;
}