package com.renascence.backend.controllers;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.dtos.city.CreateCityDto;
import com.renascence.backend.dtos.cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.dtos.deliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import com.renascence.backend.dtos.deliveryGuySalary.DeliveryGuySalaryDto;
import com.renascence.backend.dtos.food.CreateFoodDto;
import com.renascence.backend.dtos.food.EditFoodDto;
import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.report.DeliverySystemStatistics;
import com.renascence.backend.dtos.report.IncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.restaurant.EditRestaurantDto;
import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get-statistics")
    public ResponseEntity<DeliverySystemStatistics> getStatistics() {
        return ResponseEntity.ok(adminService.getStatistics());
    }

    @GetMapping("/get-income")
    public ResponseEntity<IncomeForPeriodOfTimeDto> getReportForIncome(@Valid @RequestBody IncomeForPeriodOfTimeDto dto){
        return ResponseEntity.ok(adminService.getIncome(dto));
    }

    @GetMapping("/get-income-by-delivery-guys")
    public ResponseEntity<List<DeliveryGuyIncomeDto>> getReportForIncomeByDeliveryGuy(@Valid @RequestBody DeliveryGuyIncomeForPeriodOfTimeDto dto) {
        return ResponseEntity.ok(adminService.getIncomeByDeliveryGuy(dto));
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

    @PostMapping("/create-city")
    public ResponseEntity<CityDto> createCity(@Valid @RequestBody CreateCityDto createCityDto) {
        CityDto savedCity = adminService.createCity(createCityDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCity);
    }

    @PostMapping("/create-cuisine")
    public ResponseEntity<CuisineDto> createCuisine(@Valid @RequestBody CreateCuisineDto createCuisineDto) {
        CuisineDto savedCuisine = adminService.createCuisine(createCuisineDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCuisine);
    }

    @PostMapping("/create-food")
    public ResponseEntity<FoodDto> createFood(@Valid @RequestBody CreateFoodDto dto) {
        FoodDto savedFood = adminService.createFood(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedFood);
    }

    @PostMapping("/create-restaurant")
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody CreateRestaurantDto createDto) {
        RestaurantDto savedRestaurant = adminService.createRestaurant(createDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRestaurant);
    }

    @PostMapping("/pay-delivery-guy/{id}")
    public ResponseEntity<DeliveryGuySalaryDto> payDeliveryGuy(@Valid @RequestBody CreateDeliveryGuySalaryDto dto, @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.payDeliveryGuy(dto, id));
    }

    @PutMapping("/edit-cuisine/{id}")
    public ResponseEntity<CuisineDto> editCuisine(@Valid @RequestBody CreateCuisineDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(adminService.editCuisine(dto, id));
    }

    @PutMapping("/edit-city/{id}")
    public ResponseEntity<CityDto> editCity(@Valid @RequestBody CreateCityDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(adminService.editCity(dto, id));
    }

    @PutMapping("/edit-food/{id}")
    public ResponseEntity<FoodDto> editFood(@Valid @RequestBody EditFoodDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(adminService.editFood(dto, id));
    }

    @PutMapping("/edit-restaurant/{id}")
    public ResponseEntity<RestaurantDto> editRestaurant(@Valid @RequestBody EditRestaurantDto dto, @PathVariable Long id) {
        return ResponseEntity.ok(adminService.editRestaurant(dto, id));
    }

    @DeleteMapping("/remove-restaurant/{id}")
    public ResponseEntity<String> removeRestaurant(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.removeRestaurant(id));
    }

    @DeleteMapping("/remove-city/{id}")
    public ResponseEntity<String> removeCity(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.removeCity(id));
    }

    @DeleteMapping("/remove-food/{id}")
    public ResponseEntity<String> removeFood(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.removeFood(id));
    }

    @DeleteMapping("/fire-delivery-guy/{id}")
    public ResponseEntity<String> fireDeliveryGuy(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.fireDeliveryGuy(id));
    }
}
