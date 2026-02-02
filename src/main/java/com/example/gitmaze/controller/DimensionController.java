package com.example.gitmaze.controller;

import com.example.gitmaze.service.DimensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dimension")
@RequiredArgsConstructor
public class DimensionController {

    private final DimensionService dimensionService;

    @PostMapping("/push")
    public ResponseEntity<Void> pushDimensions(@RequestBody Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        Map<String, Object> dimensions = (Map<String, Object>) payload.get("dimensions");

        if (userId == null || dimensions == null) {
            return ResponseEntity.badRequest().build();
        }

        dimensionService.pushDimensions(userId, dimensions);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pull")
    public ResponseEntity<Map<String, Object>> pullDimensions(@RequestParam String userId) {
        Map<String, Object> dimensions = dimensionService.pullDimensions(userId);
        if (dimensions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dimensions);
    }
}
