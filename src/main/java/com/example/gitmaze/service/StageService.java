package com.example.gitmaze.service;

import com.example.gitmaze.dto.Item;
import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.dto.Wall;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StageService {

    public MazeResponse getStage(String category, int level) {
        if ("tutorial".equalsIgnoreCase(category)) {
            return getTutorialStage(level);
        } else {
            return getMainStage(level);
        }
    }

    private MazeResponse getTutorialStage(int level) {
        switch (level) {
            case 1: return createTutorial1();
            case 2: return createTutorial2();
            case 3: return createTutorial3();
            case 4: return createTutorial4();
            default: return createTutorial1();
        }
    }

    private MazeResponse getMainStage(int level) {
        switch (level) {
            case 1: return createMain1();
            case 2: return createMain2();
            // Add more as needed
            default: return createMain1();
        }
    }

    // --- Tutorial Levels ---

    private MazeResponse createTutorial1() {
        int w = 3, h = 3;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        vWalls[1][0] = false; vWalls[2][0] = false;
        hWalls[2][1] = false; hWalls[2][2] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(new Item("t1_star", 2, 2, "star")));
    }

    private MazeResponse createTutorial2() {
        int w = 4, h = 4;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        vWalls[1][0] = false; vWalls[2][0] = false; vWalls[3][0] = false;
        hWalls[1][1] = false; hWalls[1][2] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(
            new Item("t2_star1", 3, 0, "star"),
            new Item("t2_star2", 1, 2, "star")
        ));
    }

    private MazeResponse createTutorial3() {
        int w = 5, h = 5;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        hWalls[0][1] = false; hWalls[0][2] = false;
        vWalls[1][2] = false; vWalls[2][2] = false;
        hWalls[2][3] = false; hWalls[2][4] = false;
        vWalls[3][4] = false; vWalls[4][4] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(new Item("t3_star", 4, 4, "star")));
    }

    private MazeResponse createTutorial4() {
        int w = 6, h = 6;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        for(int i=0; i<5; i++) vWalls[i+1][0] = false;
        for(int i=0; i<5; i++) hWalls[5][i+1] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(new Item("t4_star", 5, 5, "star")));
    }

    // --- Main Game Levels ---

    private MazeResponse createMain1() {
        // Spiral-like path for Main 1
        int w = 6, h = 6;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        for(int i=0; i<5; i++) vWalls[i+1][0] = false;
        for(int i=0; i<5; i++) hWalls[5][i+1] = false;
        for(int i=5; i>0; i--) vWalls[i][5] = false;
        for(int i=5; i>1; i--) hWalls[0][i] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(new Item("m1_star", 0, 1, "star")));
    }

    private MazeResponse createMain2() {
        // S-curve for Main 2
        int w = 6, h = 6;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        initWalls(vWalls, hWalls, w, h);

        for(int i=0; i<5; i++) vWalls[i+1][0] = false;
        hWalls[5][1] = false;
        for(int i=5; i>0; i--) vWalls[i][1] = false;
        hWalls[0][2] = false;
        for(int i=0; i<5; i++) vWalls[i+1][2] = false;

        return buildResponse(w, h, vWalls, hWalls, List.of(new Item("m2_star", 5, 2, "star")));
    }

    // --- Helpers ---

    private void initWalls(boolean[][] v, boolean[][] h, int w, int height) {
        for (int i = 0; i <= w; i++) Arrays.fill(v[i], true);
        for (int i = 0; i < w; i++) Arrays.fill(h[i], true);
    }

    private MazeResponse buildResponse(int w, int h, boolean[][] vWalls, boolean[][] hWalls, List<Item> items) {
        return MazeResponse.builder()
                .width(w)
                .height(h)
                .walls(convertToWallList(w, h, hWalls, vWalls))
                .items(items)
                .startPos(Map.of("x", 0, "z", 0))
                .build();
    }

    private List<Wall> convertToWallList(int w, int h, boolean[][] hWalls, boolean[][] vWalls) {
        List<Wall> wallList = new ArrayList<>();
        for (int i = 0; i <= w; i++) {
            for (int j = 0; j < h; j++) {
                wallList.add(new Wall("w_" + i + "_" + j + "_v", i, j, i, j + 1, "VERTICAL", !vWalls[i][j]));
            }
        }
        for (int i = 0; i < w; i++) {
            for (int j = 0; j <= h; j++) {
                wallList.add(new Wall("w_" + i + "_" + j + "_h", i, j, i + 1, j, "HORIZONTAL", !hWalls[i][j]));
            }
        }
        return wallList;
    }
}
