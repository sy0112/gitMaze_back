package com.example.gitmaze.service;

import org.springframework.stereotype.Service;
import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.dto.Wall;
import com.example.gitmaze.dto.Item;
import java.util.*;

@Service
public class MazeService {

    /**
     * @deprecated Use StageService for predefined stages. 
     * This remains for potential procedural generation needs.
     */
    public MazeResponse generateMaze(int width, int height) {
        boolean[][] visited = new boolean[width][height];
        boolean[][] hWalls = new boolean[width][height + 1];
        boolean[][] vWalls = new boolean[width + 1][height];

        for (int i = 0; i <= width; i++) Arrays.fill(vWalls[i], true);
        for (int i = 0; i < width; i++) Arrays.fill(hWalls[i], true);

        dfs(0, 0, visited, hWalls, vWalls, width, height);

        List<Item> items = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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

    private void dfs(int x, int z, boolean[][] visited, boolean[][] hWalls, boolean[][] vWalls, int w, int h) {
        visited[x][z] = true;
        Integer[] dirs = { 0, 1, 2, 3 }; // 상, 우, 하, 좌
        Collections.shuffle(Arrays.asList(dirs));

        for (int dir : dirs) {
            int nx = x + (dir == 1 ? 1 : dir == 3 ? -1 : 0);
            int nz = z + (dir == 2 ? 1 : dir == 0 ? -1 : 0);

            if (nx >= 0 && nx < w && nz >= 0 && nz < h && !visited[nx][nz]) {
                if (dir == 0) hWalls[x][z] = false;
                else if (dir == 1) vWalls[x + 1][z] = false;
                else if (dir == 2) hWalls[x][z + 1] = false;
                else if (dir == 3) vWalls[x][z] = false;
                dfs(nx, nz, visited, hWalls, vWalls, w, h);
            }
        }
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