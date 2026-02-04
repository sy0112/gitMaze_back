package com.example.gitmaze.service;

import com.example.gitmaze.dto.MazeResponse;
import com.example.gitmaze.dto.StageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class StageService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StageBuilder stageBuilder = new StageBuilder(0, 0);

    public MazeResponse getStage(String category, int level) {
        // Try to load from JSON first
        String path = String.format("stages/%s/%d.json", category, level);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        
        if (inputStream != null) {
            try {
                StageDTO dto = objectMapper.readValue(inputStream, StageDTO.class);
                return new StageBuilder(dto.getWidth(), dto.getHeight())
                        .grid(dto.getGrid().toArray(new String[0]))
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load stage: " + path, e);
            }
        }
        
        // Fallback to hardcoded stages (legacy)
        if ("tutorial".equalsIgnoreCase(category)) {
            return getTutorialStageLegacy(level);
        } else {
            return getMainStageLegacy(level);
        }
    }

    // Legacy hardcoded stages (fallback)
    private MazeResponse getTutorialStageLegacy(int level) {
        return new StageBuilder(5, 5)
                .grid("S####", "#####", "###..", "##...", "*#...")
                .build();
    }

    private MazeResponse getMainStageLegacy(int level) {
        return new StageBuilder(8, 8)
                .grid("S#######", "########", "########", "XXX#####", "...#####", "....####", ".....###", "......*#")
                .build();
    }
}
