package com.renascence.backend.repositories;

import com.renascence.backend.entities.Food;
import com.renascence.backend.enums.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByRestaurantId(Long restaurantId);
    List<Food> findByRestaurantIdAndCuisineIdAndCategory(Long restaurantId, Long cuisineId, FoodCategory category);
    List<Food> findByCuisineId(Long cuisineId);
}
