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
import com.renascence.backend.dtos.report.DeliveryGuyIncomeDto;
import com.renascence.backend.dtos.report.DeliveryGuyIncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.report.IncomeForPeriodOfTimeDto;
import com.renascence.backend.dtos.restaurant.CreateRestaurantDto;
import com.renascence.backend.dtos.restaurant.RestaurantDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccessTokenRepository accessTokenRepository;

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

    public IncomeForPeriodOfTimeDto getIncome(IncomeForPeriodOfTimeDto dto) {
        List<Delivery> deliveries = deliveryRepository
                .findAll()
                .stream()
                .filter(d -> d.getStatus() == DeliveryStatus.DELIVERED
                        && !d.getDeliveredDate().toLocalDate().isAfter(dto.getEndDate())
                        && !d.getDeliveredDate().toLocalDate().isBefore(dto.getStartDate()))
                .toList();

        double totalIncome = deliveries
                .stream()
                .flatMap(d -> d.getDeliveriesFoods().stream())
                .mapToDouble(df -> df.getFood().getPrice() * df.getFoodCount())
                .sum();

        dto.setAmount(totalIncome);

        return dto;
    }

    public List<DeliveryGuyIncomeDto> getIncomeByDeliveryGuy(DeliveryGuyIncomeForPeriodOfTimeDto dto) {
        List<DeliveryGuy> deliveryGuys = deliveryGuyRepository.findAll();

        return deliveryGuys
                .stream()
                .map(dg -> {
                   DeliveryGuyIncomeDto incomeDto = new DeliveryGuyIncomeDto();
                   incomeDto.setDeliveryGuyId(dg.getId());
                   incomeDto.setDeliveryGuyName(dg.getUser().getName());
                   incomeDto.setPhoneNumber(dg.getUser().getPhoneNumber());
                   incomeDto.setStartDate(dto.getStartDate());
                   incomeDto.setEndDate(dto.getEndDate());

                   double amount = dg.getDeliveries()
                           .stream()
                           .filter(d -> d.getStatus() == DeliveryStatus.DELIVERED
                                   && !d.getDeliveredDate().toLocalDate().isAfter(dto.getEndDate())
                                   && !d.getDeliveredDate().toLocalDate().isBefore(dto.getStartDate()))
                           .flatMap(d -> d.getDeliveriesFoods().stream())
                           .mapToDouble(df -> df.getFood().getPrice() * df.getFoodCount())
                           .sum();

                   incomeDto.setAmount(amount);

                   return incomeDto;
                })
                .toList();
    }

    public String removeRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant not found"));

        if (restaurant.isDeleted()) {
            throw new EntityNotFoundException("restaurant has already been removed");
        }

        restaurant
                .getFoods()
                .stream()
                .filter(f -> !f.isDeleted())
                .forEach(f -> f.setDeleted(true));

        restaurant.setDeleted(true);

        restaurantRepository.save(restaurant);

        return String.format("restaurant %s with id %d has been removed successfully", restaurant.getName(), restaurant.getId());
    }

    public String removeCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("city not found"));

        if (city.isDeleted()) {
            throw new EntityNotFoundException("city has already been removed");
        }

        city
            .getRestaurants()
            .stream()
            .flatMap(r -> r.getFoods().stream())
            .filter(f -> !f.isDeleted())
            .forEach(f -> f.setDeleted(true));

        city
            .getRestaurants()
            .stream()
            .filter(r -> !r.isDeleted())
            .forEach(r -> r.setDeleted(true));

        Role role = roleRepository.findByName("ROLE_" + com.renascence.backend.enums.Role.DELIVERY_GUY)
                .orElseThrow(() -> new EntityNotFoundException("delivery guy role not found?????????"));

        List<DeliveryGuy> deliveryGuys = city.getDeliveryGuys();
        deliveryGuys.forEach(dg -> {
            dg.setFired(true);
            dg.setEndWorkDate(LocalDate.now());
            dg.getUser().getRoles().remove(role);
        });

        List<AccessToken> accessTokens = deliveryGuys
                .stream()
                .map(dg -> accessTokenRepository.findByUserId(dg.getId()))
                .toList();

        accessTokens.forEach(at -> at.setRevoked(true));

        city.setDeleted(true);

        deliveryGuyRepository.saveAll(deliveryGuys);
        accessTokenRepository.saveAll(accessTokens);
        cityRepository.save(city);

        return String.format("city %s with id %d has been removed successfully", city.getName(), city.getId());
    }

    public String removeFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("food not found"));

        if (food.isDeleted()) {
            throw new EntityNotFoundException("food has already been removed");
        }

        food.setDeleted(true);

        foodRepository.save(food);

        return String.format("food %s with id %d has been removed", food.getName(), food.getId());
    }

    public String fireDeliveryGuy(Long id) {
        DeliveryGuy deliveryGuy = deliveryGuyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("delivery guy not found"));

        Role role = roleRepository.findByName("ROLE_" + com.renascence.backend.enums.Role.DELIVERY_GUY)
                .orElseThrow(() -> new EntityNotFoundException("delivery guy role not found?????????"));

        deliveryGuy.getUser().getRoles().remove(role);
        deliveryGuy.setFired(true);
        deliveryGuy.setEndWorkDate(LocalDate.now());

        AccessToken accessToken = accessTokenRepository.findByUserId(deliveryGuy.getId());
        accessToken.setRevoked(true);

        deliveryGuyRepository.save(deliveryGuy);
        accessTokenRepository.save(accessToken);

        return String.format("delivery guy %s with id %d has been successfully fired",
                deliveryGuy.getUser().getName(), deliveryGuy.getUser().getId());
    }

    public CuisineDto editCuisine(CreateCuisineDto dto, Long id) {
        Cuisine cuisine = cuisineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuisine not found"));

        cuisine.setName(dto.getName());

        cuisineRepository.save(cuisine);

        CuisineDto cuisineDto = new CuisineDto();
        cuisineDto.setId(id);
        cuisineDto.setName(dto.getName());

        return cuisineDto;
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
