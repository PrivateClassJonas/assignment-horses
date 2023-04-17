package com.accenture.assignment.horsefeeder.Repository;

import com.accenture.assignment.horsefeeder.Entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findById(Long id);
}
