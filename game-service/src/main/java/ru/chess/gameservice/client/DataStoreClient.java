package ru.chess.gameservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.chess.gameservice.dto.GameResultDTO;
import ru.chess.gameservice.dto.LoginRequestDTO;
import ru.chess.gameservice.dto.PlayerDTO;
import ru.chess.gameservice.model.ChatMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class DataStoreClient {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String dataStoreUrl = "http://localhost:8082";

    public PlayerDTO authenticate(String username, String password) {
        try {
            String url = dataStoreUrl + "/api/v1/auth/login";
            log.info("=== AUTH REQUEST to {} ===", url);

            LoginRequestDTO request = new LoginRequestDTO(username, password);
            String json = objectMapper.writeValueAsString(request);
            log.info("Request body: {}", json);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("Response status: {}", response.statusCode());
            log.info("Response body: {}", response.body());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                if (root.has("player") && !root.get("player").isNull()) {
                    JsonNode playerNode = root.get("player");
                    PlayerDTO player = objectMapper.treeToValue(playerNode, PlayerDTO.class);
                    log.info("Parsed player: {}", player.getName());
                    return player;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Auth failed: {}", e.getMessage(), e);
            return null;
        }
    }

    public void saveMessage(ChatMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dataStoreUrl + "/api/v1/messages"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            log.info("Saved: {}", message.getSender());
        } catch (Exception e) {
            log.error("Failed to save: {}", e.getMessage());
        }
    }

    public void saveGameResult(GameResultDTO result) {
        try {
            String json = objectMapper.writeValueAsString(result);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dataStoreUrl + "/api/v1/games"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            log.info("Game saved: {} vs {}", result.getWhitePlayerId(), result.getBlackPlayerId());
        } catch (Exception e) {
            log.error("Failed to save game: {}", e.getMessage());
        }
    }
}
