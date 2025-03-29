package com.renascence.backend.services;

import com.renascence.backend.dtos.Food.CreateFoodDto;
import com.renascence.backend.dtos.Food.FoodDto;
import com.renascence.backend.entities.Cuisine;
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
            foods = foodRepository.findAll(); // Or return empty list? Your choice.
        }

        return foods.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<FoodDto> getFoodsByRestaurant(Long restaurantId) {
        // Validate restaurant exists (optional)
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with ID: " + restaurantId);
        }

        List<Food> foods = foodRepository.findByRestaurantId(restaurantId);
        return foods.stream()
                .map(this::convertToDto)
                .toList();
    }

    public FoodDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food not found with ID: " + id));

        return convertToDto(food);
    }

    public FoodDto createFood(CreateFoodDto dto) {
        // Validate Cuisine and Restaurant exist
        Cuisine cuisine = cuisineRepository.findById(dto.getCuisineId())
                .orElseThrow(() -> new IllegalArgumentException("Cuisine not found"));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        // Convert DTO to Entity
        Food food = new Food();
        food.setName(dto.getName());
        food.setPrice(dto.getPrice());
        food.setDescription(dto.getDescription());
        food.setFoodCategory(dto.getFoodCategory());
        food.setCuisine(cuisine);
        food.setRestaurant(restaurant);

        Food savedFood = foodRepository.save(food);
        return convertToDto(savedFood);
    }

    private FoodDto convertToDto(Food food) {
        FoodDto dto = new FoodDto();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setPrice(food.getPrice());
        dto.setDescription(food.getDescription());
        dto.setFoodCategory(food.getFoodCategory());
        dto.setCuisineId(food.getCuisine().getId());
        dto.setRestaurantId(food.getRestaurant().getId());
        return dto;
    }
}