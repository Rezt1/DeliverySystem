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

    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityId(restaurant.getCity().getId());
        dto.setRating(restaurant.getRating());
        return dto;
    }
}
