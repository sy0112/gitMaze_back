package com.example.gitmaze.repository;

import com.example.gitmaze.domain.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GameResultRepository extends JpaRepository<GameResult, UUID> {
}
