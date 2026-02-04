package com.example.gitmaze.controller;

import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.service.MazeService;
import com.example.gitmaze.service.StageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maze")
@RequiredArgsConstructor
public class MazeController {

    private final MazeService mazeService;
    private final StageService stageService;

    @GetMapping("/generate")
    public ResponseEntity<MazeResponse> getNewMaze(
            @RequestParam(defaultValue = "1") int level) {
        // Redirect to main stage
        return ResponseEntity.ok(stageService.getStage("main", level));
    }

    @GetMapping("/tutorial/{level}")
    public ResponseEntity<MazeResponse> getTutorialMaze(@PathVariable int level) {
        return ResponseEntity.ok(stageService.getStage("tutorial", level));
    }

    @GetMapping("/stage/{category}/{level}")
    public ResponseEntity<MazeResponse> getStage(
            @PathVariable String category,
            @PathVariable int level) {
        return ResponseEntity.ok(stageService.getStage(category, level));
    }
}