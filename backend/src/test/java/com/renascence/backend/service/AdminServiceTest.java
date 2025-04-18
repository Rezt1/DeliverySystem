package com.renascence.backend.service;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.dtos.city.CreateCityDto;
import com.renascence.backend.dtos.cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.dtos.deliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import com.renascence.backend.dtos.deliveryGuySalary.DeliveryGuySalaryDto;
import com.renascence.backend.dtos.food.CreateFoodDto;
import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.report.IncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.enums.FoodCategory;
import com.renascence.backend.repositories.*;
import com.renascence.backend.services.AdminService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private CityRepository cityRepository;
    @Mock
    private CuisineRepository cuisineRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private DeliveryGuyRepository deliveryGuyRepository;
    @Mock
    private DeliveryGuySalaryRepository deliveryGuySalaryRepository;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AccessTokenRepository accessTokenRepository;


    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurant Name");
        restaurant.setDeleted(false);
    }

    @Test
    void testCreateCity() {
        // Arrange
        CreateCityDto createDto = new CreateCityDto();
        createDto.setName("Sofia");
        createDto.setSalary(1200.00);

        City savedCity = new City();
        savedCity.setId(1L);
        savedCity.setName("Sofia");
        savedCity.setSalary(1200.00);

        when(cityRepository.save(any(City.class))).thenReturn(savedCity);

        // Act
        CityDto result = adminService.createCity(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sofia", result.getName());
        assertEquals(1200.00, result.getSalary());

        ArgumentCaptor<City> cityCaptor = ArgumentCaptor.forClass(City.class);
        verify(cityRepository).save(cityCaptor.capture());

        City captured = cityCaptor.getValue();
        assertEquals("Sofia", captured.getName());
        assertEquals(1200.00, captured.getSalary());
    }



    @Test
    void testCreateCuisine() {
        // Arrange
        CreateCuisineDto createDto = new CreateCuisineDto();
        createDto.setName("Italian");

        Cuisine savedCuisine = new Cuisine();
        savedCuisine.setId(1L);
        savedCuisine.setName("Italian");

        when(cuisineRepository.save(any(Cuisine.class))).thenReturn(savedCuisine);

        // Act
        CuisineDto result = adminService.createCuisine(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Italian", result.getName());

        ArgumentCaptor<Cuisine> cuisineCaptor = ArgumentCaptor.forClass(Cuisine.class);
        verify(cuisineRepository).save(cuisineCaptor.capture());

        Cuisine captured = cuisineCaptor.getValue();
        assertEquals("Italian", captured.getName());
    }



    @Test
    void testCreateFood() {
        // Arrange
        CreateFoodDto createDto = new CreateFoodDto();
        createDto.setName("Pizza Margherita");
        createDto.setPrice(15.99);
        createDto.setDescription("Classic Italian pizza");
        createDto.setFoodCategory(FoodCategory.MAIN_COURSE);
        createDto.setCuisineId(1L);
        createDto.setRestaurantId(1L);

        Cuisine mockCuisine = new Cuisine();
        mockCuisine.setId(1L);
        mockCuisine.setName("Italian");

        Restaurant mockRestaurant = new Restaurant();
        mockRestaurant.setId(1L);
        mockRestaurant.setName("La Pizzeria");

        Food savedFood = new Food();
        savedFood.setId(1L);
        savedFood.setName("Pizza Margherita");
        savedFood.setPrice(15.99);
        savedFood.setDescription("Classic Italian pizza");
        savedFood.setFoodCategory(FoodCategory.MAIN_COURSE);
        savedFood.setCuisine(mockCuisine);
        savedFood.setRestaurant(mockRestaurant);

        when(cuisineRepository.findById(anyLong())).thenReturn(Optional.of(mockCuisine));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(mockRestaurant));
        when(foodRepository.save(any(Food.class))).thenReturn(savedFood);

        // Act
        FoodDto result = adminService.createFood(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pizza Margherita", result.getName());
        assertEquals(15.99, result.getPrice());
        assertEquals("Classic Italian pizza", result.getDescription());
        assertEquals(FoodCategory.MAIN_COURSE, result.getFoodCategory());

        // Verify that findById was called for both Cuisine and Restaurant
        verify(cuisineRepository).findById(1L);
        verify(restaurantRepository).findById(1L);

        // Verify that the save method was called for Food
        verify(foodRepository).save(any(Food.class));
    }



    @Test
    void testCreateRestaurant() {
        // Arrange
        CreateRestaurantDto createDto = new CreateRestaurantDto();
        createDto.setName("Pasta Paradise");
        createDto.setCityId(1L);
        createDto.setIban("BG123456789");
        createDto.setRating(4.5F);

        City mockCity = new City();
        mockCity.setId(1L);
        mockCity.setName("Sofia");
        mockCity.setSalary(1200.00);

        Set<String> existingIbans = new HashSet<>();
        existingIbans.add("BG987654321");  // Simulate an existing IBAN.

        Restaurant savedRestaurant = new Restaurant();
        savedRestaurant.setId(1L);
        savedRestaurant.setName("Pasta Paradise");
        savedRestaurant.setCity(mockCity);
        savedRestaurant.setIban("BG123456789");
        savedRestaurant.setRating(4.5F);

        when(cityRepository.findById(1L)).thenReturn(Optional.of(mockCity));
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(new Restaurant()));  // Simulate no conflict with IBAN
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        // Act
        RestaurantDto result = adminService.createRestaurant(createDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pasta Paradise", result.getName());
        assertEquals(4.5F, result.getRating());
        assertEquals("Sofia", result.getCityName()); // Assuming RestaurantDto has city name.

        // Verify interactions
        verify(cityRepository).findById(1L);
        verify(restaurantRepository).findAll();
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void testCreateRestaurant_IbanAlreadyExists() {
        CreateRestaurantDto createDto = new CreateRestaurantDto();
        createDto.setName("Pasta Paradise");
        createDto.setCityId(1L);
        createDto.setIban("BG987654321");
        createDto.setRating(4.5F);

        City mockCity = new City();
        mockCity.setId(1L);
        mockCity.setName("Sofia");
        mockCity.setSalary(1200.00);

        Restaurant existing = new Restaurant();
        existing.setIban("BG987654321");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(mockCity));
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(existing));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createRestaurant(createDto);
        });

        assertEquals("Such iban already exists in our system", thrown.getMessage());

        verify(cityRepository).findById(1L);
        verify(restaurantRepository).findAll();
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }



    @Test
    void testGetAllDeliveryGuys() {
        City city = new City();
        city.setName("Sofia");

        User deliveryUser1 = new User();
        deliveryUser1.setEmail("deliveryGuy1@gmail.com");
        deliveryUser1.setName("Ivan Todorov");
        deliveryUser1.setPhoneNumber("+359 877908142");

        DeliveryGuy deliveryGuy1 = new DeliveryGuy();
        deliveryGuy1.setUser(deliveryUser1);
        deliveryGuy1.setIban("BG00094883");
        deliveryGuy1.setWorkCity(city);

        User deliveryUser2 = new User();
        deliveryUser2.setEmail("deliveryGuy2@gmail.com");
        deliveryUser2.setName("Kristiyan Nedelkov");
        deliveryUser2.setPhoneNumber("+359 882356700");

        DeliveryGuy deliveryGuy2 = new DeliveryGuy();
        deliveryGuy2.setUser(deliveryUser2);
        deliveryGuy2.setIban("BG0008T883");
        deliveryGuy2.setWorkCity(city);

        when(deliveryGuyRepository.findAll()).thenReturn(List.of(deliveryGuy1, deliveryGuy2));

        // Act
        List<DeliveryGuyDto> result = adminService.getAllDeliveryGuys();

        // Assert
        assertEquals(2, result.size());

        assertEquals("Ivan Todorov", result.get(0).getDeliveryGuyName());
        assertEquals("+359 877908142", result.get(0).getDeliveryGuyPhoneNumber());

        assertEquals("Kristiyan Nedelkov", result.get(1).getDeliveryGuyName());
        assertEquals("+359 882356700", result.get(1).getDeliveryGuyPhoneNumber());

        verify(deliveryGuyRepository, times(1)).findAll();
    }



    @Test
    void testGetDeliveryGuyById_Success() {
        // Arrange
        User user = new User();
        user.setName("Ivan Petrov");
        user.setPhoneNumber("+359 888123456");
        user.setEmail("ivan.petrov@example.com");

        City city = new City();
        city.setName("Sofia");

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        deliveryGuy.setIban("BG12BANK0000000001");
        deliveryGuy.setUser(user);
        deliveryGuy.setWorkCity(city);

        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.of(deliveryGuy));

        // Act
        DeliveryGuyDto result = adminService.getDeliveryGuyById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Ivan Petrov", result.getDeliveryGuyName());
        assertEquals("Sofia", result.getWorkCity());
        assertEquals("BG12BANK0000000001", result.getIban());

        verify(deliveryGuyRepository).findById(1L);
    }

    @Test
    void testGetDeliveryGuyById_NotFound() {
        // Arrange
        when(deliveryGuyRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.getDeliveryGuyById(2L));

        verify(deliveryGuyRepository).findById(2L);
    }



    @Test
    void testPayDeliveryGuy_Success() {
        // Arrange
        City city = new City();
        city.setSalary(1200.00);

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setPhoneNumber("+359 899123456");

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        deliveryGuy.setStartWorkDate(LocalDate.of(2024, 1, 1));
        deliveryGuy.setWorkCity(city);
        deliveryGuy.setUser(user); // ← this is what was missing
        deliveryGuy.setDeliveries(List.of(
                createDeliveredDelivery(LocalDate.of(2024, 3, 1)),
                createDeliveredDelivery(LocalDate.of(2024, 3, 5))
        ));

        CreateDeliveryGuySalaryDto dto = new CreateDeliveryGuySalaryDto();
        dto.setSalaryStartDate(LocalDate.of(2024, 3, 1));
        dto.setSalaryEndDate(LocalDate.of(2024, 3, 31));

        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.of(deliveryGuy));
        when(deliveryGuySalaryRepository.save(any(DeliveryGuySalary.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DeliveryGuySalaryDto result = adminService.payDeliveryGuy(dto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1200.00 + 2 * 1.5, result.getAmount());
        assertEquals(dto.getSalaryStartDate(), result.getStartDate());
        assertEquals(dto.getSalaryEndDate(), result.getEndDate());

        verify(deliveryGuyRepository).findById(1L);
        verify(deliveryGuySalaryRepository).save(any(DeliveryGuySalary.class));
    }

    private Delivery createDeliveredDelivery(LocalDate deliveredDate) {
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredDate(deliveredDate.atStartOfDay());
        return delivery;
    }

    @Test
    void testPayDeliveryGuy_InvalidStartDate() {
        // Arrange
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setStartWorkDate(LocalDate.of(2024, 2, 1));
        deliveryGuy.setDeliveries(List.of());
        deliveryGuy.setWorkCity(new City());

        CreateDeliveryGuySalaryDto dto = new CreateDeliveryGuySalaryDto();
        dto.setSalaryStartDate(LocalDate.of(2024, 1, 1));
        dto.setSalaryEndDate(LocalDate.of(2024, 1, 31));

        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.of(deliveryGuy));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminService.payDeliveryGuy(dto, 1L));
    }

    @Test
    void testPayDeliveryGuy_DeliveryGuyNotFound() {
        // Arrange
        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.empty());

        CreateDeliveryGuySalaryDto dto = new CreateDeliveryGuySalaryDto();
        dto.setSalaryStartDate(LocalDate.now());
        dto.setSalaryEndDate(LocalDate.now());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adminService.payDeliveryGuy(dto, 1L));
    }



    @Test
    void testGetIncome() {
        // Arrange
        IncomeForPeriodOfTimeDto inputDto = new IncomeForPeriodOfTimeDto();
        inputDto.setStartDate(LocalDate.of(2024, 1, 1));
        inputDto.setEndDate(LocalDate.of(2024, 12, 31));

        Delivery delivery1 = new Delivery();
        delivery1.setStatus(DeliveryStatus.DELIVERED);
        delivery1.setDeliveredDate(LocalDate.of(2024, 3, 5).atStartOfDay());

        Food food1 = new Food();
        food1.setPrice(10.00);

        DeliveryFood deliveryFood1 = new DeliveryFood();
        deliveryFood1.setFood(food1);
        deliveryFood1.setFoodCount(2);

        delivery1.setDeliveriesFoods(List.of(deliveryFood1));

        Delivery delivery2 = new Delivery();
        delivery2.setStatus(DeliveryStatus.DELIVERED);
        delivery2.setDeliveredDate(LocalDate.of(2024, 5, 10).atStartOfDay());

        Food food2 = new Food();
        food2.setPrice(15.00);

        DeliveryFood deliveryFood2 = new DeliveryFood();
        deliveryFood2.setFood(food2);
        deliveryFood2.setFoodCount(3);

        delivery2.setDeliveriesFoods(List.of(deliveryFood2));

        when(deliveryRepository.findAll()).thenReturn(List.of(delivery1, delivery2));

        // Act
        IncomeForPeriodOfTimeDto result = adminService.getIncome(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(65.00, result.getAmount(), 0.01); // (2 * 10) + (3 * 15)
    }



    @Test
    void testGetIncomeByDeliveryGuy() {
        // Arrange
        DeliveryGuy deliveryGuy1 = new DeliveryGuy();
        DeliveryGuy deliveryGuy2 = new DeliveryGuy();

        when(deliveryGuyRepository.findAll()).thenReturn(List.of(deliveryGuy1, deliveryGuy2));

        DeliveryGuyIncomeForPeriodOfTimeDto dto = new DeliveryGuyIncomeForPeriodOfTimeDto();
        dto.setStartDate(LocalDate.now().minusDays(7));
        dto.setEndDate(LocalDate.now());

        // Act
        List<DeliveryGuyIncomeDto> result = adminService.getIncomeByDeliveryGuy(dto);

        // Assert
        assertEquals(2, result.size());
    }



    @Test
    void removeRestaurant_shouldMarkRestaurantAndFoodsAsDeleted() {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setName("Pizza Place");
        restaurant.setDeleted(false);

        Food food1 = new Food();
        food1.setName("Pepperoni");
        food1.setDeleted(false);
        Food food2 = new Food();
        food2.setName("Margarita");
        food2.setDeleted(false);
        restaurant.setFoods(List.of(food1, food2));

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        String result = adminService.removeRestaurant(restaurantId);

        // Assert
        assertTrue(restaurant.isDeleted());
        assertTrue(food1.isDeleted());
        assertTrue(food2.isDeleted());
        verify(restaurantRepository).save(restaurant);
        assertEquals("restaurant Pizza Place with id 1 has been removed successfully", result);
    }

    @Test
    void removeRestaurant_shouldThrowException_whenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeRestaurant(99L));
        assertEquals("restaurant not found", exception.getMessage());
    }

    @Test
    void removeRestaurant_shouldThrowException_whenRestaurantAlreadyDeleted() {
        // Arrange
        Restaurant deletedRestaurant = new Restaurant();
        deletedRestaurant.setId(2L);
        deletedRestaurant.setDeleted(true);
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(deletedRestaurant));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeRestaurant(2L));
        assertEquals("restaurant has already been removed", exception.getMessage());
    }

    


    @Test
    void removeCity_shouldThrowException_whenCityNotFound() {
        // Arrange
        when(cityRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeCity(10L));
        assertEquals("city not found", exception.getMessage());
    }

    @Test
    void removeCity_shouldThrowException_whenCityAlreadyDeleted() {
        // Arrange
        City city = new City();
        city.setId(2L);
        city.setName("Plovdiv");
        city.setDeleted(true);
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeCity(2L));
        assertEquals("city has already been removed", exception.getMessage());
    }
}
