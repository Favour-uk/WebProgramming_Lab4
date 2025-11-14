package com.favour.web_lab4_backend.repositories;

import com.favour.web_lab4_backend.models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    // Find all results associated with a specific user ID
    List<Result> findByUserId(Long userId);
}