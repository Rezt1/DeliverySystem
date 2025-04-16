package com.renascence.backend.service;

import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import com.renascence.backend.services.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CuisineRepository cuisineRepository;

    private final City city = new City();
    private final Cuisine cuisine = new Cuisine();
    private final Restaurant restaurant = new Restaurant();
    private final Food food = new Food();

    @BeforeEach
    void setup() {
        city.setId(1L);
        city.setName("Sofia");

        cuisine.setId(1L);
        cuisine.setName("Italian");

        food.setCuisine(cuisine);

        restaurant.setId(1L);
        restaurant.setName("Bella Italia");
        restaurant.setCity(city);
        restaurant.setFoods(List.of(food));
        restaurant.setRating(4.5F);
        restaurant.setDeleted(false);
    }

    @Test
    void testGetAllRestaurantsWithNoFilters_returnsAll() {
        when(restaurantRepository.findAllByOrderByRatingDesc())
                .thenReturn(List.of(restaurant));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, -1, 0);

        assertEquals(1, result.size());
        assertEquals("Bella Italia", result.getFirst().getName());
        verify(restaurantRepository).findAllByOrderByRatingDesc();
    }

    @Test
    void testGetAllRestaurants_filterByCity_returnsOnlyMatchingCity() {
        restaurant.setCity(city); // already in setup
        when(restaurantRepository.findAllByOrderByRatingDesc()).thenReturn(List.of(restaurant));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(1L, -1, 0);

        assertEquals(1, result.size());
        assertEquals("Sofia", result.getFirst().getCityName());
        verify(cityRepository).findById(1L);
    }

    @Test
    void testGetAllRestaurants_filterByCuisine_returnsOnlyMatchingCuisine() {
        restaurant.setFoods(List.of(food));
        when(restaurantRepository.findAllByOrderByRatingDesc()).thenReturn(List.of(restaurant));
        when(cuisineRepository.findById(1L)).thenReturn(Optional.of(cuisine));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, 1L, 0);

        assertEquals(1, result.size());
        assertEquals("Bella Italia", result.getFirst().getName());
        verify(cuisineRepository).findById(1L);
    }

    @Test
    void testGetAllRestaurants_sortByNameAscending_sortedCorrectly() {
        Restaurant r2 = new Restaurant();
        r2.setName("Al Dente");
        r2.setCity(city);
        r2.setFoods(List.of(food));
        r2.setRating(4.0F);
        r2.setDeleted(false);

        when(restaurantRepository.findAllByOrderByRatingDesc())
                .thenReturn(List.of(restaurant, r2));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, -1, 0);

        assertEquals("Al Dente", result.get(0).getName());
        assertEquals("Bella Italia", result.get(1).getName());
    }

    @Test
    void testGetAllRestaurants_sortByNameDescending_sortedCorrectly() {
        Restaurant r2 = new Restaurant();
        r2.setName("Al Dente");
        r2.setCity(city);
        r2.setFoods(List.of(food));
        r2.setRating(4.0F);
        r2.setDeleted(false);

        when(restaurantRepository.findAllByOrderByRatingDesc())
                .thenReturn(List.of(restaurant, r2));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, -1, 1);

        assertEquals("Bella Italia", result.get(0).getName());
        assertEquals("Al Dente", result.get(1).getName());
    }

    @Test
    void testGetAllRestaurants_sortByRatingDescending_sortedCorrectly() {
        Restaurant r2 = new Restaurant();
        r2.setName("Zora");
        r2.setCity(city);
        r2.setFoods(List.of(food));
        r2.setRating(3.0F);
        r2.setDeleted(false);

        when(restaurantRepository.findAllByOrderByRatingDesc())
                .thenReturn(List.of(restaurant, r2)); // already sorted

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, -1, 2);

        assertEquals(4.5F, result.get(0).getRating());
        assertEquals(3.0F, result.get(1).getRating());
    }

    @Test
    void testGetAllRestaurants_sortByRatingAscending_sortedCorrectly() {
        Restaurant r2 = new Restaurant();
        r2.setName("Zora");
        r2.setCity(city);
        r2.setFoods(List.of(food));
        r2.setRating(3.0F);
        r2.setDeleted(false);

        when(restaurantRepository.findAllByOrderByRatingDesc())
                .thenReturn(List.of(restaurant, r2));

        List<RestaurantDto> result = restaurantService.getAllRestaurants(-1, -1, 3);

        assertEquals(3.0F, result.get(0).getRating());
        assertEquals(4.5F, result.get(1).getRating());
    }

    @Test
    void testGetAllRestaurants_cityNotFound_throwsException() {
        when(restaurantRepository.findAllByOrderByRatingDesc()).thenReturn(List.of(restaurant));
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restaurantService.getAllRestaurants(99L, -1, 0));
    }

    @Test
    void testGetAllRestaurants_cuisineNotFound_throwsException() {
        when(restaurantRepository.findAllByOrderByRatingDesc()).thenReturn(List.of(restaurant));
        when(cuisineRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> restaurantService.getAllRestaurants(-1, 99L, 0));
    }

    @Test
    void testGetAllRestaurants_deletedCity_throwsException() {
        city.setDeleted(true);
        when(restaurantRepository.findAllByOrderByRatingDesc()).thenReturn(List.of(restaurant));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        assertThrows(EntityNotFoundException.class,
                () -> restaurantService.getAllRestaurants(1L, -1, 0));
    }

}

