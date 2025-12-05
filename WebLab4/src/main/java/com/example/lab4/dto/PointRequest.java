package com.example.lab4.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PointRequest {
    @NotNull
    private Double x;

    @NotNull
    @Min(value = -3, message = "Y must be >= -3")
    @Max(value = 5, message = "Y must be <= 5")
    private Double y;

    @NotNull
    @Positive(message = "R must be positive")
    private Double r;
}