package com.favour.web_lab4_backend.repositories;

import com.favour.web_lab4_backend.models.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    List<CheckResult> findAllByUserOrderByCheckedAtDesc(com.favour.web_lab4_backend.models.User user);
}