package com.example.lab4.repository;

import com.example.lab4.entity.Point;
import com.example.lab4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUserOrderByIdDesc(User user);
}