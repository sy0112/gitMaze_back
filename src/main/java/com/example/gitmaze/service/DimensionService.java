package com.example.gitmaze.service;

import com.example.gitmaze.domain.entity.GameState;
import com.example.gitmaze.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DimensionService {

    private final GameStateRepository gameStateRepository;

    @Transactional
    public void pushDimensions(String userId, Map<String, Object> dimensions) {
        GameState gameState = gameStateRepository.findById(userId)
                .orElse(GameState.builder()
                        .userId(userId)
                        .build());

        gameState.setDimensions(dimensions);
        gameStateRepository.save(gameState);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> pullDimensions(String userId) {
        return gameStateRepository.findById(userId)
                .map(GameState::getDimensions)
                .orElse(null);
    }
}
