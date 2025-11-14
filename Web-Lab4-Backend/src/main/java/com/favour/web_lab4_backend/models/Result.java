package com.favour.web_lab4_backend.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
public class Result implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "result_seq")
    @SequenceGenerator(name = "result_seq", sequenceName = "result_id_seq", allocationSize = 1)
    private Long id;

    private Double x;
    private Double y;
    private Double r;
    private Boolean hit;
    private LocalDateTime checkTime = LocalDateTime.now();

    // Many results belong to one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // 🔥 CRITICAL FIX: This tells Jackson NOT to serialize the user object.
    private User user;

    // Default constructor
    public Result() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }

    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }

    public Double getR() { return r; }
    public void setR(Double r) { this.r = r; }

    public Boolean getHit() { return hit; }
    public void setHit(Boolean hit) { this.hit = hit; }

    public LocalDateTime getCheckTime() { return checkTime; }
    public void setCheckTime(LocalDateTime checkTime) { this.checkTime = checkTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}