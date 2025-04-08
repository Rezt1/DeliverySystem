package com.renascence.backend.controllers;

import com.renascence.backend.dtos.City.CityDto;
import com.renascence.backend.dtos.City.CreateCityDto;
import com.renascence.backend.dtos.Cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.Cuisine.CuisineDto;
import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.DeliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.Food.CreateFoodDto;
import com.renascence.backend.dtos.Food.FoodDto;
import com.renascence.backend.dtos.Restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.Restaurant.RestaurantDto;
import com.renascence.backend.entities.DeliveryGuy;
import com.renascence.backend.services.AdminService;
import com.renascence.backend.services.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/create-city")
    public ResponseEntity<CityDto> createCity(@Valid @RequestBody CreateCityDto createCityDto) {
        CityDto savedCity = adminService.createCity(createCityDto);
        return ResponseEntity.ok(savedCity);
    }

    @PostMapping("/create-cuisine")
    public ResponseEntity<CuisineDto> createCuisine(@Valid @RequestBody CreateCuisineDto createCuisineDto) {
        CuisineDto savedCuisine = adminService.createCuisine(createCuisineDto);
        return ResponseEntity.ok(savedCuisine);
    }

    @PostMapping("/create-food")
    public ResponseEntity<FoodDto> createFood(@Valid @RequestBody CreateFoodDto dto) {
        FoodDto savedFood = adminService.createFood(dto);
        return ResponseEntity.ok(savedFood);
    }

    @PostMapping("/create-restaurant")
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody CreateRestaurantDto createDto) {
        RestaurantDto savedRestaurant = adminService.createRestaurant(createDto);
        return ResponseEntity.ok(savedRestaurant);
    }

    @GetMapping("/get-all-delivery-guys")
    public ResponseEntity<List<DeliveryGuyDto>> getAllDeliveryGuys() {
        List<DeliveryGuyDto> deliveryGuys = adminService.getAllDeliveryGuys();

        return ResponseEntity.ok(deliveryGuys);
    }

    @GetMapping("/get-delivery-guys/{id}")
    public ResponseEntity<DeliveryGuyDto> getDeliveryGuyById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDeliveryGuyById(id));
    }
}
