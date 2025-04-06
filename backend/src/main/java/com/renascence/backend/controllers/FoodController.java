package com.renascence.backend.controllers;

import com.renascence.backend.dtos.Food.CreateFoodDto;
import com.renascence.backend.dtos.Food.FoodDto;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.services.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<FoodDto>> getFoodsByRestaurant(
            @PathVariable Long restaurantId) {

        List<FoodDto> foods = foodService.getFoodsByRestaurant(restaurantId);
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDto> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodDto>> searchFoods(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) FoodCategory category) {

        List<FoodDto> foods = foodService.searchFoods(name, category);
        return ResponseEntity.ok(foods);
    }
}
