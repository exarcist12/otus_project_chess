package ru.chess.datastore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chess.datastore.dto.GameDTO;
import ru.chess.datastore.service.GameService;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameDTO> saveGame(@RequestBody GameDTO gameDTO) {
        return ResponseEntity.ok(gameService.saveGame(gameDTO));
    }
}