package com.renascence.backend.services;

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
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final DeliveryGuySalaryRepository deliveryGuySalaryRepository;

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

        cuisineRepository.save(cuisine);

        return new CuisineDto(cuisine.getId(), cuisine.getName());
    }

    public FoodDto createFood(CreateFoodDto dto) {
        // Validate Cuisine and Restaurant exist
        Cuisine cuisine = cuisineRepository.findById(dto.getCuisineId())
                .orElseThrow(() -> new EntityNotFoundException("Cuisine not found"));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

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
                .orElseThrow(() -> new EntityNotFoundException("City not found with ID: " + createDto.getCityId()));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(createDto.getName());
        restaurant.setCity(city);
        restaurant.setIban(createDto.getIban());
        restaurant.setRating(createDto.getRating());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return convertToRestaurantDto(savedRestaurant);
    }

    public List<DeliveryGuyDto> getAllDeliveryGuys() {
        return deliveryGuyRepository
                .findAll()
                .stream()
                .map(this::convertToDeliveryGuyDto)
                .toList();
    }

    public DeliveryGuyDto getDeliveryGuyById(Long id) {
        DeliveryGuy deliveryGuy = deliveryGuyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery guy not found"));

        return convertToDeliveryGuyDto(deliveryGuy);
    }

    public DeliveryGuySalaryDto payDeliveryGuy(CreateDeliveryGuySalaryDto dto, Long id) {
        DeliveryGuy deliveryGuy = deliveryGuyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery guy not found"));

        if (deliveryGuy.getStartWorkDate().isAfter(dto.getSalaryStartDate())){
            throw new IllegalArgumentException("Cannot pay salary for a period before the delivery guy started working");
        }

        long count = deliveryGuy.getDeliveries().stream().filter(d ->
            d.getStatus() == DeliveryStatus.DELIVERED
            && !d.getDeliveredDate().toLocalDate().isBefore(dto.getSalaryStartDate())
            && !d.getDeliveredDate().toLocalDate().isAfter(dto.getSalaryEndDate())
        ).count();

        double standardSalary = deliveryGuy.getWorkCity().getSalary();
        double bonus = count * 1.50;
        double wholeSalary = standardSalary + bonus;

        DeliveryGuySalary deliveryGuySalary = new DeliveryGuySalary();
        deliveryGuySalary.setAmount(wholeSalary);
        deliveryGuySalary.setStartDate(dto.getSalaryStartDate());
        deliveryGuySalary.setEndDate(dto.getSalaryEndDate());
        deliveryGuySalary.setDeliveryGuy(deliveryGuy);

        deliveryGuySalaryRepository.save(deliveryGuySalary);

        return convertToDeliveryGuySalaryDto(deliveryGuySalary, bonus);
    }

    private RestaurantDto convertToRestaurantDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityName(restaurant.getCity().getName());
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
        dto.setCuisineName(food.getCuisine().getName());
        dto.setRestaurantName(food.getRestaurant().getName());
        return dto;
    }

    private DeliveryGuyDto convertToDeliveryGuyDto(DeliveryGuy deliveryGuy){
        DeliveryGuyDto dto = new DeliveryGuyDto();
        dto.setUserId(deliveryGuy.getId());
        dto.setDeliveryGuyName(deliveryGuy.getUser().getName());
        dto.setDeliveryGuyPhoneNumber(deliveryGuy.getUser().getPhoneNumber());
        dto.setWorkCity(deliveryGuy.getWorkCity().getName());
        dto.setIban(deliveryGuy.getIban());

        return dto;
    }

    private DeliveryGuySalaryDto convertToDeliveryGuySalaryDto(DeliveryGuySalary deliveryGuySalary, double bonus){
        DeliveryGuySalaryDto dto = new DeliveryGuySalaryDto();

        dto.setDeliveryGuyName(deliveryGuySalary.getDeliveryGuy().getUser().getName());
        dto.setAmount(deliveryGuySalary.getAmount());
        dto.setBonusAmount(bonus);
        dto.setStartDate(deliveryGuySalary.getStartDate());
        dto.setEndDate(deliveryGuySalary.getEndDate());

        return dto;
    }
}
