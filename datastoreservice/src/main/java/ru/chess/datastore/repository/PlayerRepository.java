package ru.chess.datastore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chess.datastore.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
}
