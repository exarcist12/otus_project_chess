package ru.chess.datastore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chess.datastore.dto.PlayerDTO;
import ru.chess.datastore.entity.Player;
import ru.chess.datastore.repository.PlayerRepository;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Transactional
    public PlayerDTO savePlayer(PlayerDTO dto) {
        Player player = new Player(dto.getId(), dto.getName(), dto.getRating(), 0, 0, dto.getPassword());
        Player saved = playerRepository.save(player);
        return new PlayerDTO(saved.getId(), saved.getName(), saved.getRating(), null);
    }

    public PlayerDTO getPlayer(String id) {
        Player player = playerRepository.findById(id).orElseThrow();
        return new PlayerDTO(player.getId(), player.getName(), player.getRating(), null);
    }

    public void updateRating(String id, int newRating) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found: " + id));
        player.setRating(newRating);
        playerRepository.save(player);
    }
}
