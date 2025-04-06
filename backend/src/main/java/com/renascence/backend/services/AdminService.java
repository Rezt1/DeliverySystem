package com.renascence.backend.services;

import com.renascence.backend.dtos.City.CityDto;
import com.renascence.backend.dtos.City.CreateCityDto;
import com.renascence.backend.dtos.Cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.Cuisine.CuisineDto;
import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.Food.CreateFoodDto;
import com.renascence.backend.dtos.Food.FoodDto;
import com.renascence.backend.dtos.Restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.Restaurant.RestaurantDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DeliveryGuyRepository deliveryGuyRepository;
    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;
    private final CuisineRepository cuisineRepository;
    private final FoodRepository foodRepository;

    public CityDto createCity(CreateCityDto createCityDto) {
        City city = new City();
        city.setName(createCityDto.getName());
        city.setSalary(createCityDto.getSalary());
        cityRepository.save(city);
        return new CityDto(city.getId(), city.getName());
    }

    public CuisineDto createCuisine(CreateCuisineDto createCuisineDto) {
        Cuisine cuisine =new Cuisine();
        cuisine.setName(createCuisineDto.getName());
        Cuisine savedCuisine = cuisineRepository.save(cuisine);
        return new CuisineDto(cuisine.getId(), cuisine.getName());
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
        return convertToFoodDto(savedFood);
    }

    public RestaurantDto createRestaurant(CreateRestaurantDto createDto) {
        // Validate city exists
        City city = cityRepository.findById(createDto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + createDto.getCityId()));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(createDto.getName());
        restaurant.setCity(city);
        restaurant.setIban(createDto.getIban());
        restaurant.setRating(createDto.getRating());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToRestaurantDto(savedRestaurant);
    }


    public List<DeliveryGuy> getAllDeliveryGuys() {
        return deliveryGuyRepository.findAll();
    }

    public DeliveryGuy getDeliveryGuyById(Long id) {
        return deliveryGuyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Guy not found with ID: " + id));
    }

    private RestaurantDto convertToRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityId(restaurant.getCity().getId());
        dto.setRating(restaurant.getRating());
        return dto;
    }

    private FoodDto convertToFoodDto(Food food) {
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
