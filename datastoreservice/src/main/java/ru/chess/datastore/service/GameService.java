package ru.chess.datastore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chess.datastore.dto.GameDTO;
import ru.chess.datastore.entity.Game;
import ru.chess.datastore.repository.GameRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public GameDTO saveGame(GameDTO dto) {
        Game game = new Game();
        game.setWhitePlayerId(dto.getWhitePlayerId());
        game.setBlackPlayerId(dto.getBlackPlayerId());
        game.setWinnerId(dto.getWinnerId());
        game.setMoves(dto.getMoves());
        game.setStartedAt(dto.getStartedAt());
        game.setFinishedAt(dto.getFinishedAt());
        Game saved = gameRepository.save(game);
        dto.setId(saved.getId());
        return dto;
    }

    public GameDTO getGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found: " + id));
        return new GameDTO(game.getId(), game.getWhitePlayerId(), game.getBlackPlayerId(),
                game.getWinnerId(), game.getMoves(), game.getStartedAt(), game.getFinishedAt());
    }

    public List<GameDTO> getGamesByPlayer(String playerId) {
        List<Game> games = gameRepository.findByWhitePlayerIdOrBlackPlayerId(playerId, playerId);
        return games.stream()
                .map(g -> new GameDTO(
                        g.getId(),
                        g.getWhitePlayerId(),
                        g.getBlackPlayerId(),
                        g.getWinnerId(),
                        g.getMoves(),
                        g.getStartedAt(),
                        g.getFinishedAt()
                ))
                .collect(Collectors.toList());
    }
}
