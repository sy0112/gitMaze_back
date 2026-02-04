package com.example.gitmaze.dto;

import lombok.Data;
import java.util.List;

@Data
public class StageDTO {
    private int width;
    private int height;
    private List<String> grid; // Array of strings representing each row
}
