package com.renascence.backend.repositories;

import com.renascence.backend.entities.Food;
import com.renascence.backend.enums.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByNameContainingIgnoreCase(String name);
    List<Food> findByFoodCategory(FoodCategory category);
    List<Food> findByNameContainingIgnoreCaseAndFoodCategory(String name, FoodCategory category);
    List<Food> findByRestaurantId(Long restaurantId);
}
