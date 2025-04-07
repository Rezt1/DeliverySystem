package com.renascence.backend.controllers;

import com.renascence.backend.dtos.Restaurant.RestaurantDto;
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

    @GetMapping("/")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants(
            @RequestParam(defaultValue = "-1") long cityId,
            @RequestParam(defaultValue = "-1") long cuisineId) {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants(cityId, cuisineId);
        return ResponseEntity.ok(restaurants);
    }

//    @GetMapping("/by-city/{cityId}")
//    public ResponseEntity<List<RestaurantDto>> getRestaurantsByCity(
//            @PathVariable Long cityId) {
//        List<RestaurantDto> restaurants = restaurantService.getRestaurantsByCityId(cityId);
//        return ResponseEntity.ok(restaurants);
//    }
//
//    @GetMapping("/top-rated")
//    public ResponseEntity<List<RestaurantDto>> getTopRatedRestaurants(
//            @RequestParam(defaultValue = "5") int count) {
//        List<RestaurantDto> restaurants = restaurantService.getTopRatedRestaurants(count);
//        return ResponseEntity.ok(restaurants);
//    }
//
//    @GetMapping("/search/cuisine")
//    public ResponseEntity<List<RestaurantDto>> searchByCuisine(
//            @RequestParam String name) {
//        List<RestaurantDto> results = restaurantService.searchByCuisineName(name);
//        return ResponseEntity.ok(results);
//    }
}
