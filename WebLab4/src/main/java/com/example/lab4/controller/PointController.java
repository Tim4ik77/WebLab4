package com.example.lab4.controller;

import com.example.lab4.dto.PointRequest;
import com.example.lab4.entity.Point;
import com.example.lab4.entity.User;
import com.example.lab4.repository.PointRepository;
import com.example.lab4.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {

    @Autowired PointRepository pointRepository;
    @Autowired UserRepository userRepository;

    @GetMapping
    public List<Point> getUserPoints() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return pointRepository.findByUserOrderByIdDesc(user);
    }

    @PostMapping
    public Point addPoint(@Valid @RequestBody PointRequest request) {
        long startTime = System.nanoTime();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Point point = new Point();
        point.setX(request.getX());
        point.setY(request.getY());
        point.setR(request.getR());
        point.setUser(user);
        point.setCheckedTime(LocalDateTime.now());

        // Логика проверки попадания (Замените на вашу фигуру по варианту)
        // Пример: Четверть круга, Прямоугольник, Треугольник
        point.setResult(checkArea(request.getX(), request.getY(), request.getR()));

        long endTime = System.nanoTime();
        point.setExecutionTime((endTime - startTime) / 1000); // микросекунды

        return pointRepository.save(point);
    }

    @DeleteMapping
    public void clearPoints() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Point> points = pointRepository.findByUserOrderByIdDesc(user);
        pointRepository.deleteAll(points);
    }

    private boolean checkArea(double x, double y, double r) {
        if (r <= 0) return false;

        // 1. Четверть I (Верх-Право): Сектор (четверть круга) радиусом R
        // Условие: x^2 + y^2 <= R^2
        if (x >= 0 && y >= 0) {
            return (x * x + y * y) <= (r * r);
        }

        // 2. Четверть II (Верх-Лево): Пусто
        if (x < 0 && y > 0) {
            return false;
        }

        // 3. Четверть III (Низ-Лево): Треугольник
        // Вершины: (-R/2, 0) и (0, -R).
        // Уравнение прямой по двум точкам: (y - y1)/(y2 - y1) = (x - x1)/(x2 - x1)
        // (y - 0)/(-r - 0) = (x - (-r/2))/(0 - (-r/2))
        // y / -r = (x + r/2) / (r/2)
        // y = -r * (x + r/2) / (r/2) => y = -2 * (x + r/2) => y = -2x - r
        // Так как область выше прямой, то: y >= -2x - r
        if (x <= 0 && y <= 0) {
            return y >= (-2 * x - r);
        }

        // 4. Четверть IV (Низ-Право): Прямоугольник (Квадрат)
        // По X от 0 до R, по Y от 0 до -R
        if (x >= 0 && y <= 0) {
            return x <= r && y >= -r;
        }

        return false;
    }
}