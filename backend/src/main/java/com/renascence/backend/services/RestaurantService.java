package com.renascence.backend.services;

import com.renascence.backend.dtos.Restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.Restaurant.RestaurantDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, CityRepository cityRepository) {
        this.restaurantRepository = restaurantRepository;
        this.cityRepository = cityRepository;
    }

    public List<RestaurantDto> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<RestaurantDto> getRestaurantsByCityId(Long cityId) {
        return restaurantRepository.findByCityId(cityId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<RestaurantDto> getTopRatedRestaurants(int count) {
        return restaurantRepository.findTopRatedRestaurants(count).stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<RestaurantDto> searchByCuisineName(String cuisineName) {
        return restaurantRepository.findByCuisineNameContainingIgnoreCase(cuisineName)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public RestaurantDto createRestaurant(CreateRestaurantDto createDto) {
        // Validate city exists
        City city = cityRepository.findById(createDto.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + createDto.getCityId()));

        // Validate owner exists  // SET LATER BY ADMIN

        Restaurant restaurant = new Restaurant();
        restaurant.setName(createDto.getName());
        restaurant.setCity(city);
        //restaurant.setOwner(owner);
        restaurant.setIban(createDto.getIban());
        restaurant.setRating(createDto.getRating());

        restaurant.setDeliveryGuySalary(900.0);  // DEFAULT VALUE; IT IS POSSIBLE TO BE CHANGED BY THE ADMIN?
        //restaurant.setBonusId(null);           // SET LATER BY ADMIN

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToDto(savedRestaurant);
    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityId(restaurant.getCity().getId());
        dto.setRating(restaurant.getRating());
        //dto.setOwnerId(restaurant.getOwner().getId());
        //dto.setIban(restaurant.getIban());
        //dto.setDeliveryGuySalary(restaurant.getDeliveryGuySalary());
        return dto;
    }
}
