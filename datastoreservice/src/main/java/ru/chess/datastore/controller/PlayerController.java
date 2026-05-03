package ru.chess.datastore.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.chess.datastore.dto.PlayerDTO;
import ru.chess.datastore.service.PlayerService;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDTO createOrUpdatePlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        log.info("POST /api/v1/players - create/update player: {}", playerDTO.getName());
        return playerService.savePlayer(playerDTO);
    }

    @GetMapping("/{id}")
    public PlayerDTO getPlayer(@PathVariable String id) {
        log.info("GET /api/v1/players/{}", id);
        return playerService.getPlayer(id);
    }

    @PatchMapping("/{id}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRating(@PathVariable String id, @RequestParam int newRating) {
        log.info("PATCH /api/v1/players/{}/rating?newRating={}", id, newRating);
        playerService.updateRating(id, newRating);
    }


}