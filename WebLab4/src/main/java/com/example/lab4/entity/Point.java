package com.example.lab4.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@Data
@NoArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double x;
    private double y;
    private double r;
    private boolean result;
    private LocalDateTime checkedTime;
    private long executionTime; // в наносекундах или мс

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}