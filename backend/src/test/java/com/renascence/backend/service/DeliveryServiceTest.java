package com.renascence.backend.service;

import com.renascence.backend.dtos.delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryFood.CreateDeliveryFoodDto;
import com.renascence.backend.dtos.deliveryFood.DeliveryFoodDto;
import com.renascence.backend.entities.Delivery;
import com.renascence.backend.entities.Food;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.entities.User;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.enums.PaymentMethod;
import com.renascence.backend.repositories.*;
import com.renascence.backend.services.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.renascence.backend.enums.PaymentMethod.CARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private DeliveryFoodRepository deliveryFoodRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private FoodRepository foodRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("user@example.com");
    }



    @Test
    void createDelivery_shouldCreateDeliverySuccessfully() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setName("John Doe");

        Food food = new Food();
        food.setId(1L);
        food.setName("Pizza");
        food.setDeleted(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setName("FoodPlace");
        restaurant.setDeleted(false);
        food.setRestaurant(restaurant);

        CreateDeliveryFoodDto foodDto = new CreateDeliveryFoodDto();
        foodDto.setFoodId(1L);
        foodDto.setQuantity(2);

        CreateDeliveryDto createDto = new CreateDeliveryDto();
        createDto.setAddress("123 Main St");
        createDto.setPaymentMethod(CARD);
        createDto.setFoods(List.of(foodDto));
        createDto.setTotalPrice(25.0);
        createDto.setHourToBeDelivered("18:30");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(deliveryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(deliveryFoodRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        DeliveryDto result = deliveryService.createDelivery(createDto);

        assertEquals("123 Main St", result.getAddress());
        assertEquals("FoodPlace", result.getRestaurantName());
        assertEquals(1, result.getFoods().size());
        assertEquals("Pizza", result.getFoods().getFirst().getFoodName());
        assertEquals("John Doe", result.getUsername());
        assertEquals(CARD, result.getPaymentMethod());
        assertEquals("18:30", result.getToBeDeliveredTime());
    }

    @Test
    void createDelivery_shouldThrowIfUserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        CreateDeliveryDto dto = new CreateDeliveryDto();
        CreateDeliveryFoodDto deliveryFoodDto = new CreateDeliveryFoodDto();
        deliveryFoodDto.setFoodId(1L);
        deliveryFoodDto.setQuantity(1);
        dto.setFoods(List.of(deliveryFoodDto));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.createDelivery(dto));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void createDelivery_shouldThrowIfFirstFoodNotFound() {
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        CreateDeliveryDto dto = new CreateDeliveryDto();
        CreateDeliveryFoodDto deliveryFoodDto = new CreateDeliveryFoodDto();
        deliveryFoodDto.setFoodId(1L);
        deliveryFoodDto.setQuantity(1);
        dto.setFoods(List.of(deliveryFoodDto));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.createDelivery(dto));
        assertEquals("Food not found", exception.getMessage());
    }

    @Test
    void createDelivery_shouldThrowIfRestaurantIsDeleted() {
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Restaurant restaurant = new Restaurant();
        restaurant.setDeleted(true);

        Food food = new Food();
        food.setId(1L);
        food.setRestaurant(restaurant);
        food.setDeleted(false);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        CreateDeliveryDto dto = new CreateDeliveryDto();
        CreateDeliveryFoodDto deliveryFoodDto = new CreateDeliveryFoodDto();
        deliveryFoodDto.setFoodId(1L);
        deliveryFoodDto.setQuantity(1);
        dto.setFoods(List.of(deliveryFoodDto));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.createDelivery(dto));
        assertEquals("Restaurant no longer exists in our system", exception.getMessage());
    }

    @Test
    void createDelivery_shouldThrowIfFoodNotFoundInLoop() {
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Restaurant restaurant = new Restaurant();
        restaurant.setDeleted(false);

        Food firstFood = new Food();
        firstFood.setId(1L);
        firstFood.setRestaurant(restaurant);
        firstFood.setDeleted(false);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(firstFood)); // first food exists
        when(foodRepository.findById(2L)).thenReturn(Optional.empty()); // second doesn't

        CreateDeliveryDto dto = getCreateDeliveryDto();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.createDelivery(dto));

        assertEquals("Food not found: 2", exception.getMessage());
    }

    @Test
    void createDelivery_shouldThrowIfFoodDeletedInLoop() {
        User user = new User();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Restaurant restaurant = new Restaurant();
        restaurant.setDeleted(false);

        Food firstFood = new Food();
        firstFood.setId(1L);
        firstFood.setRestaurant(restaurant);
        firstFood.setDeleted(false);

        Food secondFood = new Food();
        secondFood.setId(2L);
        secondFood.setName("Burger");
        secondFood.setDeleted(true);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(firstFood));
        when(foodRepository.findById(2L)).thenReturn(Optional.of(secondFood));

        CreateDeliveryDto dto = getCreateDeliveryDto();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.createDelivery(dto));
        assertEquals("Food Burger with id 2 no longer exists", exception.getMessage());
    }

    @Test
    void testCreateDelivery_withPastTime_throwsIllegalArgumentException() {
        String pastTime = LocalDateTime.now().minusHours(1).toLocalTime().toString();

        CreateDeliveryDto createDeliveryDto = getDto(pastTime);

        // Mock food repository
        Food mockFood = mock(Food.class);
        when(foodRepository.findById(1L)).thenReturn(Optional.of(mockFood));

        // Mock user repository
        User mockUser = mock(User.class);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockUser));

        // Mock restaurant repository
        Restaurant mockRestaurant = mock(Restaurant.class);
        when(mockRestaurant.isDeleted()).thenReturn(false);
        when(mockFood.getRestaurant()).thenReturn(mockRestaurant);

        // Run the test and assert the exception
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            deliveryService.createDelivery(createDeliveryDto);
        });

        assertEquals("Cannot make an order for the past", thrown.getMessage());
    }

    private static CreateDeliveryDto getDto(String pastTime) {
        CreateDeliveryDto createDeliveryDto = new CreateDeliveryDto();
        createDeliveryDto.setHourToBeDelivered(pastTime);
        createDeliveryDto.setAddress("123 Test St");
        createDeliveryDto.setPaymentMethod(CARD);
        createDeliveryDto.setTotalPrice(100.0);

        // Initialize a list with one DeliveryFoodDto
        CreateDeliveryFoodDto foodDto = new CreateDeliveryFoodDto();
        foodDto.setFoodId(1L);
        foodDto.setQuantity(2);

        List<CreateDeliveryFoodDto> foodList = List.of(foodDto);
        createDeliveryDto.setFoods(foodList); // Set the foods list
        return createDeliveryDto;
    }

    private static CreateDeliveryDto getCreateDeliveryDto() {
        CreateDeliveryFoodDto deliveryFoodDto = new CreateDeliveryFoodDto();
        deliveryFoodDto.setFoodId(1L);
        deliveryFoodDto.setQuantity(1);

        CreateDeliveryFoodDto deliveryFoodDto2 = new CreateDeliveryFoodDto();
        deliveryFoodDto2.setFoodId(2L);
        deliveryFoodDto2.setQuantity(2);

        CreateDeliveryDto dto = new CreateDeliveryDto();
        dto.setFoods(List.of(deliveryFoodDto, deliveryFoodDto2));
        dto.setTotalPrice(30.0); // Set the total price to avoid NullPointerException
        dto.setHourToBeDelivered("15:30"); // Set the hourToBeDelivered to a valid value
        return dto;
    }



    @Test
    void getDeliveryById_shouldReturnDeliveryDtoIfValid() {
        User user = new User();
        user.setName("John");
        user.setPhoneNumber("123456");
        user.setDeliveries(new ArrayList<>());

        Restaurant restaurant = new Restaurant();
        restaurant.setName("FoodPlace");

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setReceiver(user);
        delivery.setRestaurant(restaurant);
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setAddress("Somewhere");
        delivery.setPaymentMethod(CARD);
        delivery.setCreationDate(LocalDateTime.now());
        delivery.setToBeDeliveredHour(LocalDateTime.now().plusHours(1));
        delivery.setDeliveriesFoods(new ArrayList<>());

        user.getDeliveries().add(delivery);

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        DeliveryDto result = deliveryService.getDeliveryById(1L);

        assertEquals(1L, result.getDeliveryId());
        assertEquals("John", result.getUsername());
        assertEquals("123456", result.getUserPhoneNumber());
        assertEquals("FoodPlace", result.getRestaurantName());
        assertEquals("Somewhere", result.getAddress());
        assertEquals(DeliveryStatus.PENDING, result.getStatus());
        assertEquals(PaymentMethod.CARD, result.getPaymentMethod());
    }

    @Test
    void getDeliveryById_shouldThrowIfDeliveryNotFound() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.getDeliveryById(1L));

        assertEquals("Delivery not found", exception.getMessage());
    }

    @Test
    void getDeliveryById_shouldThrowIfUserNotFound() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                deliveryService.getDeliveryById(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getDeliveryById_shouldThrowIfDeliveryNotOwnedByUser() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);

        User user = new User();
        user.setDeliveries(new ArrayList<>()); // does not contain this delivery

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                deliveryService.getDeliveryById(1L));

        assertEquals("Delivery is not yours", exception.getMessage());
    }
}
