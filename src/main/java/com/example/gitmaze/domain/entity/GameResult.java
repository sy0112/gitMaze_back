package com.example.gitmaze.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String userId;

    private int commandCount;
    private long playTime; // seconds
    private LocalDateTime clearedAt;

    @PrePersist
    public void setTimestamp() {
        this.clearedAt = LocalDateTime.now();
    }
}
