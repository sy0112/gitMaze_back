package com.example.gitmaze.service;

import org.springframework.stereotype.Service;
import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.dto.Wall;
import com.example.gitmaze.dto.Item;
import java.util.*;

@Service
public class MazeService {

    public MazeResponse generateMaze(int width, int height) {
        boolean[][] visited = new boolean[width][height];
        // 각 셀의 오른쪽(v)과 아래쪽(h) 벽 상태 관리 (true = 벽 있음)
        boolean[][] hWalls = new boolean[width][height + 1];
        boolean[][] vWalls = new boolean[width + 1][height];

        // 초기화: 모든 벽을 세움
        for (int i = 0; i <= width; i++)
            Arrays.fill(vWalls[i], true);
        for (int i = 0; i < width; i++)
            Arrays.fill(hWalls[i], true);

        // DFS 미로 생성 시작 (0,0 부터)
        dfs(0, 0, visited, hWalls, vWalls, width, height);

        // 아이템 랜덤 배치 (예: 10% 확률)
        List<Item> items = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 시작 지점(0,0) 제외하고 랜덤 배치
                if ((i != 0 || j != 0) && random.nextDouble() < 0.1) {
                    items.add(new Item("item_" + i + "_" + j, i, j, "star"));
                }
            }
        }

        return MazeResponse.builder()
                .width(width)
                .height(height)
                .walls(convertToWallList(width, height, hWalls, vWalls))
                .items(items)
                .startPos(Map.of("x", 0, "z", 0))
                .build();
    }

    public MazeResponse getTutorialMaze(int level) {
        switch (level) {
            case 1: return createTutorialLevel1();
            case 2: return createTutorialLevel2();
            case 3: return createTutorialLevel3();
            case 4: return createTutorialLevel4();
            default: return generateMaze(6, 6);
        }
    }

    private MazeResponse createTutorialLevel1() {
        // 3x3 simple path
        int w = 3;
        int h = 3;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        for (int i = 0; i <= w; i++) Arrays.fill(vWalls[i], true);
        for (int i = 0; i < w; i++) Arrays.fill(hWalls[i], true);

        // Path: (0,0) -> (1,0) -> (2,0) -> (2,1) -> (2,2)
        vWalls[1][0] = false;
        vWalls[2][0] = false;
        hWalls[2][1] = false;
        hWalls[2][2] = false;

        List<Item> items = List.of(new Item("t1_star", 2, 2, "star"));
        return MazeResponse.builder().width(w).height(h).walls(convertToWallList(w, h, hWalls, vWalls))
                .items(items).startPos(Map.of("x", 0, "z", 0)).build();
    }

    private MazeResponse createTutorialLevel2() {
        // 4x4 Branching
        int w = 4;
        int h = 4;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        for (int i = 0; i <= w; i++) Arrays.fill(vWalls[i], true);
        for (int i = 0; i < w; i++) Arrays.fill(hWalls[i], true);

        // Main Path
        vWalls[1][0] = false; vWalls[2][0] = false; vWalls[3][0] = false;
        // Branch at (1,0) down to (1,1)
        hWalls[1][1] = false; hWalls[1][2] = false;

        List<Item> items = List.of(
            new Item("t2_star1", 3, 0, "star"),
            new Item("t2_star2", 1, 2, "star")
        );
        return MazeResponse.builder().width(w).height(h).walls(convertToWallList(w, h, hWalls, vWalls))
                .items(items).startPos(Map.of("x", 0, "z", 0)).build();
    }

    private MazeResponse createTutorialLevel3() {
        // 5x5 Dimension Switching path
        int w = 5;
        int h = 5;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        for (int i = 0; i <= w; i++) Arrays.fill(vWalls[i], true);
        for (int i = 0; i < w; i++) Arrays.fill(hWalls[i], true);

        // Path (0,0) -> (0,2) -> (2,2) -> (2,4) -> (4,4)
        hWalls[0][1] = false; hWalls[0][2] = false;
        vWalls[1][2] = false; vWalls[2][2] = false;
        hWalls[2][3] = false; hWalls[2][4] = false;
        vWalls[3][4] = false; vWalls[4][4] = false;

        List<Item> items = List.of(new Item("t3_star", 4, 4, "star"));
        return MazeResponse.builder().width(w).height(h).walls(convertToWallList(w, h, hWalls, vWalls))
                .items(items).startPos(Map.of("x", 0, "z", 0)).build();
    }

    private MazeResponse createTutorialLevel4() {
        // 6x6 Advanced
        int w = 6;
        int h = 6;
        boolean[][] hWalls = new boolean[w][h + 1];
        boolean[][] vWalls = new boolean[w + 1][h];
        for (int i = 0; i <= w; i++) Arrays.fill(vWalls[i], true);
        for (int i = 0; i < w; i++) Arrays.fill(hWalls[i], true);

        // Complex-ish fixed path
        for(int i=0; i<5; i++) vWalls[i+1][0] = false; // top row
        for(int i=0; i<5; i++) hWalls[5][i+1] = false; // right col

        List<Item> items = List.of(new Item("t4_star", 5, 5, "star"));
        return MazeResponse.builder().width(w).height(h).walls(convertToWallList(w, h, hWalls, vWalls))
                .items(items).startPos(Map.of("x", 0, "z", 0)).build();
    }

    private void dfs(int x, int z, boolean[][] visited, boolean[][] hWalls, boolean[][] vWalls, int w, int h) {
        visited[x][z] = true;
        Integer[] dirs = { 0, 1, 2, 3 }; // 상, 우, 하, 좌
        Collections.shuffle(Arrays.asList(dirs));

        for (int dir : dirs) {
            int nx = x + (dir == 1 ? 1 : dir == 3 ? -1 : 0);
            int nz = z + (dir == 2 ? 1 : dir == 0 ? -1 : 0);

            if (nx >= 0 && nx < w && nz >= 0 && nz < h && !visited[nx][nz]) {
                // 벽 허물기
                if (dir == 0)
                    hWalls[x][z] = false; // 위쪽
                else if (dir == 1)
                    vWalls[x + 1][z] = false; // 오른쪽
                else if (dir == 2)
                    hWalls[x][z + 1] = false; // 아래쪽
                else if (dir == 3)
                    vWalls[x][z] = false; // 왼쪽
                dfs(nx, nz, visited, hWalls, vWalls, w, h);
            }
        }
    }

    private List<Wall> convertToWallList(int w, int h, boolean[][] hWalls, boolean[][] vWalls) {
        List<Wall> wallList = new ArrayList<>();
        // 수직 벽 변환
        for (int i = 0; i <= w; i++) {
            for (int j = 0; j < h; j++) {
                wallList.add(new Wall("w_" + i + "_" + j + "_v", i, j, i, j + 1, "VERTICAL", !vWalls[i][j]));
            }
        }
        // 수평 벽 변환
        for (int i = 0; i < w; i++) {
            for (int j = 0; j <= h; j++) {
                wallList.add(new Wall("w_" + i + "_" + j + "_h", i, j, i + 1, j, "HORIZONTAL", !hWalls[i][j]));
            }
        }
        return wallList;
    }
}