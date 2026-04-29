package ru.chess.datastore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.chess.datastore.dto.LoginRequestDTO;
import ru.chess.datastore.dto.LoginResponseDTO;
import ru.chess.datastore.dto.PlayerDTO;
import ru.chess.datastore.entity.Player;
import ru.chess.datastore.repository.PlayerRepository;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PlayerRepository playerRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("Login attempt: {}", request.getUsername());
        var playerOpt = playerRepository.findById(request.getUsername());
        if (playerOpt.isPresent() && playerOpt.get().getPassword().equals(request.getPassword())) {
            Player p = playerOpt.get();
            PlayerDTO dto = new PlayerDTO(p.getId(), p.getName(), p.getRating(), null);
            return ResponseEntity.ok(new LoginResponseDTO(true, "OK", dto));
        } else {
            log.warn("Failed login for {}", request.getUsername());
            return ResponseEntity.status(401).body(new LoginResponseDTO(false, "Invalid credentials", null));
        }
    }

}