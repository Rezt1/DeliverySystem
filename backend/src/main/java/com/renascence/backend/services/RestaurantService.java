package com.renascence.backend.services;

import com.renascence.backend.dtos.restaurant.RestaurantDto;
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

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CityRepository cityRepository;
    private final CuisineRepository cuisineRepository;

    public List<RestaurantDto> getAllRestaurants(long cityId, long cuisineId, int sorting) {
        List<Restaurant> restaurants = restaurantRepository
                .findAllByOrderByRatingDesc()
                .stream()
                .filter(r -> !r.isDeleted())
                .toList();

        if (cityId != -1) {
            City filterCity = cityRepository.findById(cityId)
                    .orElseThrow(() -> new EntityNotFoundException("city not found"));

            if (filterCity.isDeleted()) {
                throw new EntityNotFoundException("City no longer exists in our system");
            }

            restaurants = restaurants.stream().filter(r -> r.getCity().getId() == filterCity.getId()).toList();
        }

        if (cuisineId != -1) {
            Cuisine filterCuisine =  cuisineRepository.findById(cuisineId)
                    .orElseThrow(() -> new EntityNotFoundException("cuisine not found"));

            restaurants = restaurants.stream().filter(r -> {
                for (Food food : r.getFoods()) {
                    return food.getCuisine().getId() == filterCuisine.getId();
                }
                return false;
            }).toList();
        }

        if (sorting == 0) {
            restaurants = restaurants
                    .stream()
                    .sorted(Comparator.comparing(Restaurant::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
        } else if (sorting == 1) {
            restaurants = restaurants
                    .stream()
                    .sorted(Comparator.comparing(Restaurant::getName, String.CASE_INSENSITIVE_ORDER).reversed())
                    .toList();
        } else if (sorting == 2) {
            restaurants = restaurants
                    .stream()
                    .sorted(Comparator.comparing(Restaurant::getRating).reversed())
                    .toList();
        } else if (sorting == 3) {
            restaurants = restaurants
                    .stream()
                    .sorted(Comparator.comparing(Restaurant::getRating))
                    .toList();
        }

        return restaurants
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private RestaurantDto convertToDto(Restaurant restaurant) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setCityName(restaurant.getCity().getName());
        dto.setRating(restaurant.getRating());
        dto.setIban(restaurant.getIban());
        return dto;
    }
}
