package com.example.gitmaze.controller;

import com.example.gitmaze.domain.entity.GameResult;
import com.example.gitmaze.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameResultController {

    private final GameResultRepository gameResultRepository;

    @PostMapping("/end")
    public ResponseEntity<Void> endGame(@RequestBody Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        Integer commandCount = (Integer) payload.get("commandCount");
        Integer playTimeObj = (Integer) payload.get("playTime");

        if (userId == null || commandCount == null || playTimeObj == null) {
            return ResponseEntity.badRequest().build();
        }

        GameResult result = GameResult.builder()
                .userId(userId)
                .commandCount(commandCount)
                .playTime(playTimeObj.longValue())
                .build();

        gameResultRepository.save(result);
        return ResponseEntity.ok().build();
    }
}
