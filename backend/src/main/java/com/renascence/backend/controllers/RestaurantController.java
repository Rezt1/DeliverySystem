package com.renascence.backend.controllers;

import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // sorting: 0 -> alphabetical A-Z, 1 -> alphabetical Z-A, 2 -> rating 5-1, 3 -> rating 1-5
    @GetMapping("")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants(
            @RequestParam(defaultValue = "-1") long cityId,
            @RequestParam(defaultValue = "-1") long cuisineId,
            @RequestParam(defaultValue = "-1") int sorting) {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants(cityId, cuisineId, sorting);
        return ResponseEntity.ok(restaurants);
    }
}
