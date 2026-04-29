package ru.chess.gameservice.service;

import org.springframework.stereotype.Service;
import ru.chess.gameservice.dto.PlayerDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, String> sessionToPlayer = new ConcurrentHashMap<>();
    private final Map<String, Boolean> sessionAuthenticated = new ConcurrentHashMap<>();
    private final Map<String, PlayerDTO> sessionToPlayerInfo = new ConcurrentHashMap<>();

    public void setAuthenticated(String sessionId, boolean auth) {
        sessionAuthenticated.put(sessionId, auth);
    }

    public boolean isAuthenticated(String sessionId) {
        return sessionAuthenticated.getOrDefault(sessionId, false);
    }

    public void registerPlayer(String sessionId, PlayerDTO player) {
        sessionToPlayer.put(sessionId, player.getId());
        sessionToPlayerInfo.put(sessionId, player);
    }

    public PlayerDTO getPlayerInfo(String sessionId) {
        return sessionToPlayerInfo.get(sessionId);
    }

    public String getPlayerName(String sessionId) {
        return sessionToPlayer.get(sessionId);
    }

    public void unregister(String sessionId) {
        sessionToPlayer.remove(sessionId);
        sessionAuthenticated.remove(sessionId);
    }
}