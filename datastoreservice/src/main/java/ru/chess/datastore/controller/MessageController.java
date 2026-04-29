package ru.chess.datastore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.chess.datastore.dto.MessageDTO;
import ru.chess.datastore.service.MessageService;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @PostMapping
    public void saveMessage(@Valid @RequestBody MessageDTO messageDTO) {
        messageService.saveMessage(messageDTO);
    }


    @GetMapping("/latest")
    public List<MessageDTO> getLatestMessages(@RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/v1/messages/latest - limit: {}", limit);
        return messageService.getLastMessages(limit);
    }


    @GetMapping("/sender/{sender}")
    public List<MessageDTO> getMessagesBySender(@PathVariable String sender) {
        log.info("GET /api/v1/messages/sender/{}", sender);
        return messageService.getMessagesBySender(sender);
    }

    @GetMapping("/health")
    public String health() {
        return "DataStoreService is running!";
    }
}