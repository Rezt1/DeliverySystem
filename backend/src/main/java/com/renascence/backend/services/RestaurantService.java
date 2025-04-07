package com.renascence.backend.services;

import com.renascence.backend.dtos.Restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.Restaurant.RestaurantDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;
    private final CuisineRepository cuisineRepository;

    public List<RestaurantDto> getAllRestaurants(long cityId, long cuisineId) {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        if (cityId != -1){
            City filterCity = cityRepository.findById(cityId)
                    .orElseThrow(() -> new EntityNotFoundException("city not found"));

            restaurants = restaurants.stream().filter(r -> r.getCity().getId() == filterCity.getId()).toList();
        }

        if (cuisineId != -1){
            Cuisine filterCuisine =  cuisineRepository.findById(cuisineId)
                    .orElseThrow(() -> new EntityNotFoundException("cuisine not found"));

            restaurants = restaurants.stream().filter(r -> {
                for (Food food : r.getFoods()){
                    return food.getCuisine().getId() == filterCuisine.getId();
                }
                return false;
            }).toList();
        }

        return restaurants
                .stream()
                .map(this::convertToDto)
                .toList();
    }

//    public List<RestaurantDto> getRestaurantsByCityId(Long cityId) {
//        return restaurantRepository.findByCityId(cityId).stream()
//                .map(this::convertToDto)
//                .toList();
//    }
//
//    public List<RestaurantDto> getTopRatedRestaurants(int count) {
//        return restaurantRepository.findTopRatedRestaurants(count).stream()
//                .map(this::convertToDto)
//                .toList();
//    }
//
//    public List<RestaurantDto> searchByCuisineName(String cuisineName) {
//        return restaurantRepository.findByCuisineNameContainingIgnoreCase(cuisineName)
//                .stream()
//                .map(this::convertToDto)
//                .toList();
//    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityId(restaurant.getCity().getId());
        dto.setRating(restaurant.getRating());
        return dto;
    }
}
