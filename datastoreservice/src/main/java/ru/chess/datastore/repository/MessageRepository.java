package ru.chess.datastore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.chess.datastore.entity.Message;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderOrderByReceivedAtDesc(String sender);
    List<Message> findTop10ByOrderByReceivedAtDesc();
}