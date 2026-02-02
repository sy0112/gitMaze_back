package com.example.gitmaze.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private int x;
    private int z;
    private String type; // e.g., "star", "key"
}
