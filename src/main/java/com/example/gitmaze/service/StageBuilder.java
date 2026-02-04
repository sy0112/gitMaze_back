package com.example.gitmaze.service;

import com.example.gitmaze.dto.Item;
import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.dto.Wall;

import java.util.*;

public class StageBuilder {
    private final int width;
    private final int height;
    private char[][] grid;
    private int startX = 0;
    private int startZ = 0;

    public StageBuilder(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        for (char[] row : grid) {
            Arrays.fill(row, '.');
        }
    }

    public StageBuilder grid(String... rows) {
        if (rows.length != height) {
            throw new IllegalArgumentException("Grid height mismatch: expected " + height + ", got " + rows.length);
        }
        for (int z = 0; z < rows.length; z++) {
            if (rows[z].length() != width) {
                throw new IllegalArgumentException("Grid width mismatch at row " + z + ": expected " + width + ", got " + rows[z].length());
            }
            grid[z] = rows[z].toCharArray();
        }
        return this;
    }

    public MazeResponse build() {
        List<Item> items = new ArrayList<>();
        String[][] gridData = new String[height][width];
        
        // Parse grid
        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                char cell = grid[z][x];
                
                // Determine floor type
                String floorType = "void";
                
                switch (cell) {
                    case '#': // Solid floor
                        floorType = "solid";
                        break;
                    case 'X': // Pit
                        floorType = "pit";
                        break;
                    case '.': // Void (no floor)
                        floorType = "void";
                        break;
                    case 'S': // Start position
                        floorType = "solid";
                        startX = x;
                        startZ = z;
                        break;
                    case 'B': // Block (cube)
                        floorType = "solid";
                        items.add(new Item("block_" + x + "_" + z, x, z, "block_cube"));
                        break;
                    case 'b': // Block (sphere)
                        floorType = "solid";
                        items.add(new Item("block_s_" + x + "_" + z, x, z, "block_sphere"));
                        break;
                    case 'v': // Block (tetra)
                        floorType = "solid";
                        items.add(new Item("block_t_" + x + "_" + z, x, z, "block_tetra"));
                        break;
                    case 'P': // Plate (cube)
                        floorType = "solid";
                        items.add(new Item("plate_" + x + "_" + z, x, z, "plate_cube"));
                        break;
                    case 'p': // Plate (sphere)
                        floorType = "solid";
                        items.add(new Item("plate_s_" + x + "_" + z, x, z, "plate_sphere"));
                        break;
                    case 'w': // Plate (tetra)
                        floorType = "solid";
                        items.add(new Item("plate_t_" + x + "_" + z, x, z, "plate_tetra"));
                        break;
                    case '*': // Star
                        floorType = "solid";
                        items.add(new Item("star_" + x + "_" + z, x, z, "star"));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown cell type: " + cell + " at (" + x + ", " + z + ")");
                }
                
                gridData[z][x] = floorType;
            }
        }

        return MazeResponse.builder()
                .width(width)
                .height(height)
                .grid(gridData)
                .walls(Collections.emptyList())
                .items(items)
                .startPos(Map.of("x", startX, "z", startZ))
                .build();
    }
}
