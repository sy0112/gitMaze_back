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