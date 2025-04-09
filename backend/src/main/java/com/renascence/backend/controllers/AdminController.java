package com.renascence.backend.controllers;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.dtos.city.CreateCityDto;
import com.renascence.backend.dtos.cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.dtos.deliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import com.renascence.backend.dtos.deliveryGuySalary.DeliveryGuySalaryDto;
import com.renascence.backend.dtos.food.CreateFoodDto;
import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.dtos.restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/pay-delivery-guy/{id}")
    public ResponseEntity<DeliveryGuySalaryDto> payDeliveryGuy(@Valid @RequestBody CreateDeliveryGuySalaryDto dto, @PathVariable Long id){
        return ResponseEntity.ok(adminService.payDeliveryGuy(dto, id));
    }
}
