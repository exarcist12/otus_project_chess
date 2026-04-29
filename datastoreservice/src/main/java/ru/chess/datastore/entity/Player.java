package ru.chess.datastore.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    private String id;
    private String name;
    private int rating;
    private int gamesPlayed;
    private int gamesWon;
    private String password;
}