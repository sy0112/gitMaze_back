package com.example.gitmaze.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Wall {
    private String id; // 예: w_0_0_h
    private int startX;
    private int startZ;
    private int endX;
    private int endZ;
    private String type; // HORIZONTAL, VERTICAL
    private boolean isOpened;
}
