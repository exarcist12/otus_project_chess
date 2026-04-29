package ru.chess.datastore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String whitePlayerId;
    private String blackPlayerId;
    private String winnerId;
    private String moves;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}