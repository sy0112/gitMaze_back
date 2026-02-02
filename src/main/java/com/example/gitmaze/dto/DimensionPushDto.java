package com.example.gitmaze.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DimensionPushDto {
    private String userId;
    private Map<String, Object> dimensions;
}
