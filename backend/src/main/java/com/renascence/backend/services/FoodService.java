package com.renascence.backend.services;

import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.repositories.FoodRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final CuisineRepository cuisineRepository;
    private final RestaurantRepository restaurantRepository;

    public List<FoodDto> searchFoods(String name, FoodCategory category) {
        // Build dynamic query with JPA Criteria API or QueryDSL (better for complex queries)
        List<Food> foods;

        if (name != null && category != null) {
            foods = foodRepository.findByNameContainingIgnoreCaseAndFoodCategory(name, category);
        } else if (name != null) {
            foods = foodRepository.findByNameContainingIgnoreCase(name);
        } else if (category != null) {
            foods = foodRepository.findByFoodCategory(category);
        } else {
            foods = foodRepository.findAll();
        }

        return foods.stream()
                .filter(f -> !f.isDeleted())
                .map(this::convertToDto)
                .toList();
    }

    public List<FoodDto> getFoodsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with ID: " + restaurantId));

        if (restaurant.isDeleted()) {
            throw new EntityNotFoundException("Restaurant no longer exists with ID: " + restaurantId);
        }

        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        return foods.stream()
                .filter(f -> !f.isDeleted())
                .map(this::convertToDto)
                .toList();
    }

    public FoodDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food not found with ID: " + id));

        if (food.isDeleted()) {
            throw new EntityNotFoundException("Food no longer exist with id: " + id);
        }

        return convertToDto(food);
    }

    private FoodDto convertToDto(Food food) {
        FoodDto dto = new FoodDto();

        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setPrice(food.getPrice());
        dto.setDescription(food.getDescription());
        dto.setFoodCategory(food.getFoodCategory());
        dto.setCuisineName(food.getCuisine().getName());
        dto.setRestaurantName(food.getRestaurant().getName());

        return dto;
    }
}