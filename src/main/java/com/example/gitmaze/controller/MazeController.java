package com.example.gitmaze.controller;

import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.service.MazeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maze")
@RequiredArgsConstructor
public class MazeController {

    private final MazeService mazeService;

    @GetMapping("/generate")
    public ResponseEntity<MazeResponse> getNewMaze(
            @RequestParam(defaultValue = "6") int width,
            @RequestParam(defaultValue = "6") int height) {
        return ResponseEntity.ok(mazeService.generateMaze(width, height));
    }
}