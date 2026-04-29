package ru.chess.datastore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chess.datastore.dto.MessageDTO;
import ru.chess.datastore.entity.Message;
import ru.chess.datastore.repository.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public MessageDTO saveMessage(MessageDTO messageDTO) {
        Message message = new Message(
                messageDTO.getSender(),
                messageDTO.getContent(),
                messageDTO.getTimestamp(),
                messageDTO.getRoomId()   // передаём roomId
        );
        Message saved = messageRepository.save(message);
        log.info("Message saved with id: {}", saved.getId());
        return messageDTO;
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getLastMessages(int limit) {
        return messageRepository.findTop10ByOrderByReceivedAtDesc()
                .stream()
                .limit(limit)
                .map(msg -> new MessageDTO(
                        msg.getSender(),
                        msg.getContent(),
                        msg.getTimestamp(),
                        msg.getRoomId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesBySender(String sender) {
        return messageRepository.findBySenderOrderByReceivedAtDesc(sender)
                .stream()
                .map(msg -> new MessageDTO(
                        msg.getSender(),
                        msg.getContent(),
                        msg.getTimestamp(),
                        msg.getRoomId()
                ))
                .collect(Collectors.toList());
    }
}