package com.favour.web_lab4_backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
public class CheckResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean hit;
    private LocalDateTime checkedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}