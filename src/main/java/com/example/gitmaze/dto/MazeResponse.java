package com.example.gitmaze.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MazeResponse {
    private int width;
    private int height;
    private List<Wall> walls;
    private List<Item> items;
    private Map<String, Integer> startPos;
}
