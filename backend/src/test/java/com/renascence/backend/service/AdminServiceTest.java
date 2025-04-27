package com.renascence.backend.service;

import com.renascence.backend.dtos.city.CityDto;
import com.renascence.backend.dtos.city.CreateCityDto;
import com.renascence.backend.dtos.cuisine.CreateCuisineDto;
import com.renascence.backend.dtos.cuisine.CuisineDto;
import com.renascence.backend.dtos.deliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.deliveryGuySalary.CreateDeliveryGuySalaryDto;
import com.renascence.backend.dtos.deliveryGuySalary.DeliveryGuySalaryDto;
import com.renascence.backend.dtos.food.CreateFoodDto;
import com.renascence.backend.dtos.food.EditFoodDto;
import com.renascence.backend.dtos.food.FoodDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.report.DeliverySystemStatistics;
import com.renascence.backend.dtos.report.IncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.restaurant.EditRestaurantDto;
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
    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurant Name");
        restaurant.setDeleted(false);
    }

    @Test
    void testCreateCity() {
        CreateCityDto createDto = new CreateCityDto();
        createDto.setName("Sofia");
        createDto.setSalary(1200.00);

        City savedCity = new City();
        savedCity.setId(1L);
        savedCity.setName("Sofia");
        savedCity.setSalary(1200.00);

        when(cityRepository.save(any(City.class))).thenReturn(savedCity);

        CityDto result = adminService.createCity(createDto);

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
        CreateCuisineDto createDto = new CreateCuisineDto();
        createDto.setName("Italian");

        Cuisine savedCuisine = new Cuisine();
        savedCuisine.setId(1L);
        savedCuisine.setName("Italian");

        when(cuisineRepository.save(any(Cuisine.class))).thenReturn(savedCuisine);

        CuisineDto result = adminService.createCuisine(createDto);

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

        FoodDto result = adminService.createFood(createDto);
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pizza Margherita", result.getName());
        assertEquals(15.99, result.getPrice());
        assertEquals("Classic Italian pizza", result.getDescription());
        assertEquals(FoodCategory.MAIN_COURSE, result.getFoodCategory());

        verify(cuisineRepository).findById(1L);
        verify(restaurantRepository).findById(1L);

        verify(foodRepository).save(any(Food.class));
    }



    @Test
    void testCreateRestaurant() {
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
        when(restaurantRepository.findAll()).thenReturn(Collections.singletonList(new Restaurant()));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        RestaurantDto result = adminService.createRestaurant(createDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pasta Paradise", result.getName());
        assertEquals(4.5F, result.getRating());
        assertEquals("Sofia", result.getCityName());

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

        List<DeliveryGuyDto> result = adminService.getAllDeliveryGuys();

        assertEquals(2, result.size());

        assertEquals("Ivan Todorov", result.get(0).getDeliveryGuyName());
        assertEquals("+359 877908142", result.get(0).getDeliveryGuyPhoneNumber());

        assertEquals("Kristiyan Nedelkov", result.get(1).getDeliveryGuyName());
        assertEquals("+359 882356700", result.get(1).getDeliveryGuyPhoneNumber());

        verify(deliveryGuyRepository, times(1)).findAll();
    }



    @Test
    void testGetDeliveryGuyById_Success() {
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

        DeliveryGuyDto result = adminService.getDeliveryGuyById(1L);

        assertNotNull(result);
        assertEquals("Ivan Petrov", result.getDeliveryGuyName());
        assertEquals("Sofia", result.getWorkCity());
        assertEquals("BG12BANK0000000001", result.getIban());

        verify(deliveryGuyRepository).findById(1L);
    }

    @Test
    void testGetDeliveryGuyById_NotFound() {
        when(deliveryGuyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.getDeliveryGuyById(2L));

        verify(deliveryGuyRepository).findById(2L);
    }



    @Test
    void testPayDeliveryGuy_Success() {
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

        DeliveryGuySalaryDto result = adminService.payDeliveryGuy(dto, 1L);

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
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setStartWorkDate(LocalDate.of(2024, 2, 1));
        deliveryGuy.setDeliveries(List.of());
        deliveryGuy.setWorkCity(new City());

        CreateDeliveryGuySalaryDto dto = new CreateDeliveryGuySalaryDto();
        dto.setSalaryStartDate(LocalDate.of(2024, 1, 1));
        dto.setSalaryEndDate(LocalDate.of(2024, 1, 31));

        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.of(deliveryGuy));

        assertThrows(IllegalArgumentException.class, () -> adminService.payDeliveryGuy(dto, 1L));
    }

    @Test
    void testPayDeliveryGuy_DeliveryGuyNotFound() {
        when(deliveryGuyRepository.findById(1L)).thenReturn(Optional.empty());

        CreateDeliveryGuySalaryDto dto = new CreateDeliveryGuySalaryDto();
        dto.setSalaryStartDate(LocalDate.now());
        dto.setSalaryEndDate(LocalDate.now());

        assertThrows(EntityNotFoundException.class, () -> adminService.payDeliveryGuy(dto, 1L));
    }



    @Test
    void testGetIncome() {
//        IncomeForPeriodOfTimeDto inputDto = new IncomeForPeriodOfTimeDto();
//        inputDto.setStartDate(LocalDate.of(2024, 1, 1));
//        inputDto.setEndDate(LocalDate.of(2024, 12, 31));
//
//        Delivery delivery1 = new Delivery();
//        delivery1.setStatus(DeliveryStatus.DELIVERED);
//        delivery1.setDeliveredDate(LocalDate.of(2024, 3, 5).atStartOfDay());
//        delivery1.setTotalPrice(20.00); // (2 * 10)
//
//        Food food1 = new Food();
//        food1.setPrice(10.00);
//
//        DeliveryFood deliveryFood1 = new DeliveryFood();
//        deliveryFood1.setFood(food1);
//        deliveryFood1.setFoodCount(2);
//
//        delivery1.setDeliveriesFoods(List.of(deliveryFood1));
//
//        Delivery delivery2 = new Delivery();
//        delivery2.setStatus(DeliveryStatus.DELIVERED);
//        delivery2.setDeliveredDate(LocalDate.of(2024, 5, 10).atStartOfDay());
//        delivery2.setTotalPrice(45.00); // (3 * 15)
//
//        Food food2 = new Food();
//        food2.setPrice(15.00);
//
//        DeliveryFood deliveryFood2 = new DeliveryFood();
//        deliveryFood2.setFood(food2);
//        deliveryFood2.setFoodCount(3);
//
//        delivery2.setDeliveriesFoods(List.of(deliveryFood2));
//
//        when(deliveryRepository.findAll()).thenReturn(List.of(delivery1, delivery2));
//
//        IncomeForPeriodOfTimeDto result = adminService.getIncome(inputDto);
//
//        assertNotNull(result);
//        assertEquals(65.00, result.getAmount(), 0.01);
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
        delivery1.setTotalPrice(2 * 10.00);

        Delivery delivery2 = new Delivery();
        delivery2.setStatus(DeliveryStatus.DELIVERED);
        delivery2.setDeliveredDate(LocalDate.of(2024, 5, 10).atStartOfDay());

        Food food2 = new Food();
        food2.setPrice(15.00);

        DeliveryFood deliveryFood2 = new DeliveryFood();
        deliveryFood2.setFood(food2);
        deliveryFood2.setFoodCount(3);

        delivery2.setDeliveriesFoods(List.of(deliveryFood2));
        delivery2.setTotalPrice(3 * 15.00);

        when(deliveryRepository.findAll()).thenReturn(List.of(delivery1, delivery2));

        IncomeForPeriodOfTimeDto result = adminService.getIncome(inputDto);

        assertNotNull(result);
        assertEquals(65.00, result.getAmount(), 0.01);
    }



    @Test
    void testGetIncomeByDeliveryGuy() {
        DeliveryGuy deliveryGuy1 = new DeliveryGuy();
        DeliveryGuy deliveryGuy2 = new DeliveryGuy();

        when(deliveryGuyRepository.findAll()).thenReturn(List.of(deliveryGuy1, deliveryGuy2));

        DeliveryGuyIncomeForPeriodOfTimeDto dto = new DeliveryGuyIncomeForPeriodOfTimeDto();
        dto.setStartDate(LocalDate.now().minusDays(7));
        dto.setEndDate(LocalDate.now());

        List<DeliveryGuyIncomeDto> result = adminService.getIncomeByDeliveryGuy(dto);

        assertEquals(2, result.size());
    }



    @Test
    void removeRestaurant_shouldMarkRestaurantAndFoodsAsDeleted() {
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

        String result = adminService.removeRestaurant(restaurantId);

        assertTrue(restaurant.isDeleted());
        assertTrue(food1.isDeleted());
        assertTrue(food2.isDeleted());
        verify(restaurantRepository).save(restaurant);
        assertEquals("restaurant Pizza Place with id 1 has been removed successfully", result);
    }

    @Test
    void removeRestaurant_shouldThrowException_whenRestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeRestaurant(99L));
        assertEquals("restaurant not found", exception.getMessage());
    }

    @Test
    void removeRestaurant_shouldThrowException_whenRestaurantAlreadyDeleted() {
        Restaurant deletedRestaurant = new Restaurant();
        deletedRestaurant.setId(2L);
        deletedRestaurant.setDeleted(true);
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(deletedRestaurant));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeRestaurant(2L));
        assertEquals("restaurant has already been removed", exception.getMessage());
    }



    @Test
    void removeCity_shouldCascadeDeleteRestaurantsAndFoodsAndFireDeliveryGuys() {
        Long cityId = 1L;
        City city = new City();
        city.setId(cityId);
        city.setName("Test City");
        city.setDeleted(false);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setDeleted(false);

        Food food = new Food();
        food.setId(1L);
        food.setDeleted(false);
        restaurant.setFoods(List.of(food));

        city.setRestaurants(List.of(restaurant));

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        deliveryGuy.setFired(false);
        deliveryGuy.setUser(new User());
        city.setDeliveryGuys(List.of(deliveryGuy));

        Role role = new Role();
        role.setName("ROLE_DELIVERY_GUY");

        AccessToken accessToken = new AccessToken();
        accessToken.setRevoked(false);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.of(role));
        when(accessTokenRepository.findByUserId(deliveryGuy.getId())).thenReturn(accessToken);

        String result = adminService.removeCity(cityId);

        assertTrue(city.isDeleted());
        assertTrue(restaurant.isDeleted());
        assertTrue(food.isDeleted());
        assertTrue(deliveryGuy.isFired());
        assertEquals(LocalDate.now(), deliveryGuy.getEndWorkDate());
        assertTrue(accessToken.isRevoked());
        assertEquals("city Test City with id 1 has been removed successfully", result);
    }

    @Test
    void removeCity_shouldThrowException_whenCityNotFound() {
        when(cityRepository.findById(10L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeCity(10L));
        assertEquals("city not found", exception.getMessage());
    }

    @Test
    void removeCity_shouldThrowException_whenCityAlreadyDeleted() {
        City city = new City();
        city.setId(2L);
        city.setName("Plovdiv");
        city.setDeleted(true);

        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.removeCity(2L));
        assertEquals("city has already been removed", exception.getMessage());
    }

    @Test
    void removeFood_shouldMarkFoodAsDeleted() {
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);
        food.setName("Test Food");
        food.setDeleted(false);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

        String result = adminService.removeFood(foodId);

        assertTrue(food.isDeleted());
        assertEquals("food Test Food with id 1 has been removed", result);
        verify(foodRepository).save(food); // Verify that save was called
    }

    @Test
    void removeFood_shouldThrowEntityNotFoundException_whenFoodNotFound() {
        Long foodId = 1L;

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.removeFood(foodId));
    }

    @Test
    void removeFood_shouldThrowEntityNotFoundException_whenFoodAlreadyDeleted() {
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);
        food.setName("Test Food");
        food.setDeleted(true);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

        assertThrows(EntityNotFoundException.class, () -> adminService.removeFood(foodId));
    }



    @Test
    void fireDeliveryGuy_shouldFireDeliveryGuyAndRevokeAccessToken() {
        long userId = 1L;
        long deliveryGuyId = 1L;

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(deliveryGuyId);
        deliveryGuy.setFired(false);
        deliveryGuy.setEndWorkDate(null);

        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        deliveryGuy.setUser(user);

        Role role = new Role();
        role.setName("ROLE_DELIVERY_GUY");

        AccessToken accessToken = new AccessToken();
        accessToken.setRevoked(false);

        when(deliveryGuyRepository.findById(deliveryGuyId)).thenReturn(Optional.of(deliveryGuy));
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.of(role));
        when(accessTokenRepository.findByUserId(deliveryGuyId)).thenReturn(accessToken);

        String result = adminService.fireDeliveryGuy(deliveryGuyId);

        assertTrue(deliveryGuy.isFired());
        assertNotNull(deliveryGuy.getEndWorkDate());
        assertTrue(accessToken.isRevoked());
        assertEquals("delivery guy John Doe with id " + deliveryGuyId + " has been successfully fired", result);

        verify(deliveryGuyRepository).save(deliveryGuy);
        verify(accessTokenRepository).save(accessToken);
    }

    @Test
    void fireDeliveryGuy_shouldThrowEntityNotFoundException_whenDeliveryGuyNotFound() {
        Long deliveryGuyId = 1L;

        when(deliveryGuyRepository.findById(deliveryGuyId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.fireDeliveryGuy(deliveryGuyId));
    }

    @Test
    void fireDeliveryGuy_shouldThrowEntityNotFoundException_whenRoleNotFound() {
        Long deliveryGuyId = 1L;
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(deliveryGuyId);
        deliveryGuy.setFired(false);
        deliveryGuy.setEndWorkDate(null);
        deliveryGuy.setUser(new User());

        when(deliveryGuyRepository.findById(deliveryGuyId)).thenReturn(Optional.of(deliveryGuy));
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> adminService.fireDeliveryGuy(deliveryGuyId));
    }

    @Test
    void fireDeliveryGuy_shouldHandleAccessTokenNotFound() {
        Long deliveryGuyId = 1L;
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(deliveryGuyId);
        deliveryGuy.setFired(false);
        deliveryGuy.setEndWorkDate(null);

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        deliveryGuy.setUser(user);

        Role role = new Role();
        role.setName("ROLE_DELIVERY_GUY");

        when(deliveryGuyRepository.findById(deliveryGuyId)).thenReturn(Optional.of(deliveryGuy));
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.of(role));
        when(accessTokenRepository.findByUserId(deliveryGuyId)).thenReturn(null);

        String result = adminService.fireDeliveryGuy(deliveryGuyId);

        assertTrue(deliveryGuy.isFired());
        assertNotNull(deliveryGuy.getEndWorkDate());
        assertEquals("delivery guy John Doe with id " + deliveryGuyId + " has been successfully fired", result);

        verify(deliveryGuyRepository).save(deliveryGuy);
        verify(accessTokenRepository, never()).save(any());
    }



    @Test
    void editCuisine_shouldUpdateCuisineSuccessfully() {
        Long cuisineId = 1L;
        CreateCuisineDto createDto = new CreateCuisineDto();
        createDto.setName("Updated Cuisine");

        Cuisine cuisine = new Cuisine();
        cuisine.setId(cuisineId);
        cuisine.setName("Old Cuisine");

        when(cuisineRepository.findById(cuisineId)).thenReturn(Optional.of(cuisine));
        when(cuisineRepository.save(any(Cuisine.class))).thenReturn(cuisine);

        CuisineDto result = adminService.editCuisine(createDto, cuisineId);

        assertEquals(cuisineId, result.getId());
        assertEquals("Updated Cuisine", result.getName());

        verify(cuisineRepository).findById(cuisineId);
        verify(cuisineRepository).save(cuisine);
    }

    @Test
    void editCuisine_shouldThrowExceptionWhenCuisineNotFound() {
        Long cuisineId = 99L;
        CreateCuisineDto createDto = new CreateCuisineDto();
        createDto.setName("Whatever");

        when(cuisineRepository.findById(cuisineId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.editCuisine(createDto, cuisineId);
        });

        verify(cuisineRepository).findById(cuisineId);
        verify(cuisineRepository, never()).save(any());
    }



    @Test
    void editCity_shouldUpdateCitySuccessfully() {
        Long cityId = 1L;
        CreateCityDto dto = new CreateCityDto();
        dto.setName("New City");
        dto.setSalary(2500.0);

        City city = new City();
        city.setId(cityId);
        city.setName("Old City");
        city.setSalary(2000.0);
        city.setDeleted(false);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));
        when(cityRepository.save(any(City.class))).thenReturn(city);

        CityDto result = adminService.editCity(dto, cityId);

        assertEquals(cityId, result.getId());
        assertEquals("New City", result.getName());
        assertEquals(2500.0, result.getSalary());

        verify(cityRepository).findById(cityId);
        verify(cityRepository).save(city);
    }

    @Test
    void editCity_shouldThrowExceptionWhenCityNotFound() {
        Long cityId = 99L;
        CreateCityDto dto = new CreateCityDto();
        dto.setName("Doesn't Matter");
        dto.setSalary(3000.0);

        when(cityRepository.findById(cityId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.editCity(dto, cityId);
        });

        verify(cityRepository).findById(cityId);
        verify(cityRepository, never()).save(any());
    }

    @Test
    void editCity_shouldThrowExceptionWhenCityIsDeleted() {
        Long cityId = 1L;
        CreateCityDto dto = new CreateCityDto();
        dto.setName("Another Name");
        dto.setSalary(3200.0);

        City city = new City();
        city.setId(cityId);
        city.setDeleted(true);

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(city));

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.editCity(dto, cityId);
        });

        verify(cityRepository).findById(cityId);
        verify(cityRepository, never()).save(any());
    }



    @Test
    void editFood_shouldUpdateFoodSuccessfully() {
        Long foodId = 1L;

        EditFoodDto dto = new EditFoodDto();
        dto.setName("Updated Pizza");
        dto.setPrice(15.99);
        dto.setDescription("Spicy and crispy");
        dto.setFoodCategory(FoodCategory.MAIN_COURSE);

        Cuisine cuisine = new Cuisine();
        cuisine.setId(1L);
        cuisine.setName("Italian");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Pizza Palace");

        Food existingFood = new Food();
        existingFood.setId(foodId);
        existingFood.setDeleted(false);
        existingFood.setCuisine(cuisine);
        existingFood.setRestaurant(restaurant);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(existingFood));
        when(foodRepository.save(any(Food.class))).thenReturn(existingFood);

        FoodDto expectedDto = new FoodDto();
        expectedDto.setId(foodId);
        expectedDto.setName("Updated Pizza");
        expectedDto.setPrice(15.99);
        expectedDto.setDescription("Spicy and crispy");
        expectedDto.setFoodCategory(FoodCategory.MAIN_COURSE);

        FoodDto result = adminService.editFood(dto, foodId);

        assertEquals("Updated Pizza", result.getName());
        assertEquals(15.99, result.getPrice());
        assertEquals("Spicy and crispy", result.getDescription());
        assertEquals(FoodCategory.MAIN_COURSE, result.getFoodCategory());

        verify(foodRepository).findById(foodId);
        verify(foodRepository).save(existingFood);
    }

    @Test
    void editFood_shouldThrowExceptionWhenFoodNotFound() {
        Long foodId = 99L;
        EditFoodDto dto = new EditFoodDto();
        dto.setName("Whatever");

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.editFood(dto, foodId);
        });

        verify(foodRepository).findById(foodId);
        verify(foodRepository, never()).save(any());
    }

    @Test
    void editFood_shouldThrowExceptionWhenFoodIsDeleted() {
        Long foodId = 1L;
        EditFoodDto dto = new EditFoodDto();
        dto.setName("Tacos");

        Food deletedFood = new Food();
        deletedFood.setId(foodId);
        deletedFood.setDeleted(true);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(deletedFood));

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.editFood(dto, foodId);
        });

        verify(foodRepository).findById(foodId);
        verify(foodRepository, never()).save(any());
    }



    @Test
    void editRestaurant_shouldUpdateRestaurantSuccessfully() {
        Long restaurantId = 1L;

        City city = new City();
        city.setId(10L);
        city.setName("Sofia");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setDeleted(false);
        restaurant.setIban("OLD_IBAN");
        restaurant.setCity(city);

        EditRestaurantDto dto = new EditRestaurantDto();
        dto.setName("Updated Name");
        dto.setRating(4.7F);
        dto.setIban("NEW_IBAN");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        RestaurantDto result = adminService.editRestaurant(dto, restaurantId);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getIban(), result.getIban());
        assertEquals(dto.getRating(), result.getRating());
        assertEquals("Sofia", result.getCityName());

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void editRestaurant_shouldThrowExceptionIfRestaurantNotFound() {
        Long id = 1L;
        when(restaurantRepository.findById(id)).thenReturn(Optional.empty());

        EditRestaurantDto dto = new EditRestaurantDto();
        dto.setName("New Name");
        dto.setIban("NEW_IBAN");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.editRestaurant(dto, id));

        assertEquals("Restaurant not found", exception.getMessage());
    }

    @Test
    void editRestaurant_shouldThrowExceptionIfRestaurantIsDeleted() {
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setDeleted(true);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        EditRestaurantDto dto = new EditRestaurantDto();
        dto.setName("Test");
        dto.setIban("IBAN");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> adminService.editRestaurant(dto, id));

        assertEquals("Restaurant no longer exists", exception.getMessage());
    }

    @Test
    void editRestaurant_shouldThrowExceptionIfIbanAlreadyExists() {
        Long id = 1L;
        Restaurant current = new Restaurant();
        current.setId(id);
        current.setDeleted(false);

        Restaurant other = new Restaurant();
        other.setId(2L);
        other.setIban("DUPLICATE_IBAN");

        EditRestaurantDto dto = new EditRestaurantDto();
        dto.setName("Some Name");
        dto.setIban("DUPLICATE_IBAN");

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(current));
        when(restaurantRepository.findAll()).thenReturn(List.of(current, other));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adminService.editRestaurant(dto, id));

        assertEquals("Such iban already exists in our system", exception.getMessage());
    }



    @Test
    public void testGetStatistics() {
        // Mock the data for repositories
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User(), new User())); // 3 users
        when(cityRepository.findAll()).thenReturn(Arrays.asList(new City(), new City())); // 2 cities
        when(cityRepository.findAll()).thenReturn(Arrays.asList(new City(), new City())); // 2 cities
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(new Restaurant(), new Restaurant())); // 2 restaurants
        when(deliveryGuyRepository.findAll()).thenReturn(Arrays.asList(new DeliveryGuy(), new DeliveryGuy(), new DeliveryGuy())); // 3 delivery guys

        // Assume that some of the entities are deleted or fired
        // For example, setting one city as deleted:
        List<City> cities = Arrays.asList(new City(), new City());
        cities.getFirst().setDeleted(true); // Mark the first city as deleted
        when(cityRepository.findAll()).thenReturn(cities); // This should count as only 1 city

        // Execute the method
        DeliverySystemStatistics stats = adminService.getStatistics();

        // Validate the result
        assertEquals(3, stats.getUserCount()); // 3 users
        assertEquals(1, stats.getCitiesCount()); // Only 1 city is not deleted
        assertEquals(2, stats.getRestaurantsCount()); // 2 restaurants
        assertEquals(3, stats.getDeliveryGuysCount()); // 3 delivery guys
    }
}