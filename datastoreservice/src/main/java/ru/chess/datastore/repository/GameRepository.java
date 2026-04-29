package ru.chess.datastore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chess.datastore.entity.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByWhitePlayerIdOrBlackPlayerId(String whiteId, String blackId);
}