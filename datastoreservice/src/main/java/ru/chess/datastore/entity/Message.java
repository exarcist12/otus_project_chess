package ru.chess.datastore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String timestamp;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(nullable = false)
    private LocalDateTime receivedAt = LocalDateTime.now();

    public Message(String sender, String content, String timestamp, String roomId) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }
}