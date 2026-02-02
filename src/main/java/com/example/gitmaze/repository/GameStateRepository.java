package com.example.gitmaze.repository;

import com.example.gitmaze.domain.entity.GameState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepository extends JpaRepository<GameState, String> {
}
