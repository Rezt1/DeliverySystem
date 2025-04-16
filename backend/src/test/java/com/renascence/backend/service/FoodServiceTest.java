package com.renascence.backend.service;

import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.entities.Cuisine;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.repositories.CuisineRepository;
import com.renascence.backend.repositories.FoodRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import com.renascence.backend.services.FoodService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private CuisineRepository cuisineRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private Food food;
    private Cuisine cuisine;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        cuisine = new Cuisine();
        cuisine.setName("Italian");

        restaurant = new Restaurant();
        restaurant.setName("Luigi’s");

        food = new Food();
        food.setId(1L);
        food.setName("Pizza");
        food.setDescription("Delicious");
        food.setPrice(12.50);
        food.setFoodCategory(FoodCategory.MAIN_COURSE);
        food.setCuisine(cuisine);
        food.setRestaurant(restaurant);
        food.setDeleted(false);
    }

    @Test
    void testSearchFoods_WithNameAndCategory() {
        List<Food> mockFoods = List.of(food);
        when(foodRepository.findByNameContainingIgnoreCaseAndFoodCategory("Pizza", FoodCategory.MAIN_COURSE))
                .thenReturn(mockFoods);

        List<FoodDto> result = foodService.searchFoods("Pizza", FoodCategory.MAIN_COURSE);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.getFirst().getName());
        verify(foodRepository).findByNameContainingIgnoreCaseAndFoodCategory("Pizza", FoodCategory.MAIN_COURSE);
    }

    @Test
    void testSearchFoods_WithOnlyName() {
        List<Food> mockFoods = List.of(food);
        when(foodRepository.findByNameContainingIgnoreCase("Pizza"))
                .thenReturn(mockFoods);

        List<FoodDto> result = foodService.searchFoods("Pizza", null);

        assertEquals(1, result.size());
        verify(foodRepository).findByNameContainingIgnoreCase("Pizza");
    }

    @Test
    void testSearchFoods_WithOnlyCategory() {
        List<Food> mockFoods = List.of(food);
        when(foodRepository.findByFoodCategory(FoodCategory.MAIN_COURSE))
                .thenReturn(mockFoods);

        List<FoodDto> result = foodService.searchFoods(null, FoodCategory.MAIN_COURSE);

        assertEquals(1, result.size());
        verify(foodRepository).findByFoodCategory(FoodCategory.MAIN_COURSE);
    }

    @Test
    void testSearchFoods_WithNullNameAndCategory() {
        List<Food> mockFoods = List.of(food);
        when(foodRepository.findAll()).thenReturn(mockFoods);

        List<FoodDto> result = foodService.searchFoods(null, null);

        assertEquals(1, result.size());
        verify(foodRepository).findAll();
    }

    @Test
    void testSearchFoods_ExcludesDeletedFoods() {
        Food deletedFood = new Food();
        deletedFood.setDeleted(true);

        when(foodRepository.findAll()).thenReturn(List.of(food, deletedFood));

        List<FoodDto> result = foodService.searchFoods(null, null);

        assertEquals(1, result.size());
        assertFalse(result.getFirst().getName().isEmpty());
        verify(foodRepository).findAll();
    }



    @Test
    void testGetFoodsByRestaurant_Success() {
        food.setRestaurant(restaurant);
        List<Food> mockFoods = List.of(food);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodRepository.findByRestaurantId(1L)).thenReturn(mockFoods);

        List<FoodDto> result = foodService.getFoodsByRestaurant(1L);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
        verify(restaurantRepository).findById(1L);
        verify(foodRepository).findByRestaurantId(1L);
    }

    @Test
    void testGetFoodsByRestaurant_RestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> foodService.getFoodsByRestaurant(99L));

        assertEquals("Restaurant not found with ID: 99", exception.getMessage());
        verify(restaurantRepository).findById(99L);
        verifyNoInteractions(foodRepository);
    }

    @Test
    void testGetFoodsByRestaurant_RestaurantIsDeleted() {
        restaurant.setDeleted(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> foodService.getFoodsByRestaurant(1L));

        assertEquals("Restaurant no longer exists with ID: 1", exception.getMessage());
        verify(restaurantRepository).findById(1L);
        verifyNoMoreInteractions(foodRepository);
    }

    @Test
    void testGetFoodsByRestaurant_ExcludesDeletedFoods() {
        Food deletedFood = new Food();
        deletedFood.setDeleted(true);
        deletedFood.setRestaurant(restaurant);

        List<Food> mockFoods = List.of(food, deletedFood);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(foodRepository.findByRestaurantId(1L)).thenReturn(mockFoods);

        List<FoodDto> result = foodService.getFoodsByRestaurant(1L);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
        verify(foodRepository).findByRestaurantId(1L);
    }



    @Test
    void testGetFoodById_Success() {
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        FoodDto result = foodService.getFoodById(1L);

        assertNotNull(result);
        assertEquals(food.getId(), result.getId());
        assertEquals(food.getName(), result.getName());
        assertEquals(food.getPrice(), result.getPrice());
        assertEquals(food.getFoodCategory(), result.getFoodCategory());
        assertEquals(food.getCuisine().getName(), result.getCuisineName());
        assertEquals(food.getRestaurant().getName(), result.getRestaurantName());

        verify(foodRepository).findById(1L);
    }

    @Test
    void testGetFoodById_FoodNotFound() {
        when(foodRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> foodService.getFoodById(99L));

        assertEquals("Food not found with ID: 99", exception.getMessage());
        verify(foodRepository).findById(99L);
    }

    @Test
    void testGetFoodById_FoodIsDeleted() {
        food.setDeleted(true);
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> foodService.getFoodById(1L));

        assertEquals("Food no longer exist with id: 1", exception.getMessage());
        verify(foodRepository).findById(1L);
    }

}
