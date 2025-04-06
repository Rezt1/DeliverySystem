package com.renascence.backend.services;

import com.renascence.backend.dtos.Delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.dtos.DeliveryFood.DeliveryFoodDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryFoodRepository deliveryFoodRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public DeliveryDto createDelivery(CreateDeliveryDto createDeliveryDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        Optional<User> deliveryGuyOpt = userRepository.findByEmail(auth.getName());
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(createDeliveryDto.getRestaurantId());

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        if (deliveryGuyOpt.isEmpty()) {
            throw new IllegalArgumentException("Delivery guy not found");
        }
        if (restaurantOpt.isEmpty()) {
            throw new IllegalArgumentException("Restaurant not found");
        }

        Delivery delivery = new Delivery();

        delivery.setAddress(createDeliveryDto.getAddress());
        delivery.setCreationDate(LocalDateTime.now());
        delivery.setDeliveryGuy(deliveryGuyOpt.get().getDeliveryGuy());
        delivery.setPaymentMethod(createDeliveryDto.getPaymentMethod());
        delivery.setReceiver(userOpt.get());
        delivery.setRestaurant(restaurantOpt.get());
        delivery.setStatus(DeliveryStatus.PENDING);

        List<DeliveryFood> deliveryFoods = createDeliveryDto.getFoods().stream()
                .map(deliveryFoodDto -> {
                    Food food = foodRepository.findById(deliveryFoodDto.getFoodId())
                            .orElseThrow(() -> new IllegalArgumentException("Food not found: " + deliveryFoodDto.getFoodId()));

                    DeliveryFood deliveryFood = new DeliveryFood();
                    deliveryFood.setDelivery(delivery);
                    deliveryFood.setFood(food);
                    deliveryFood.setFoodCount(deliveryFoodDto.getQuantity());

                    return deliveryFood;
                })
                .collect(Collectors.toList());

        delivery.setDeliveriesFoods(deliveryFoods);

        deliveryRepository.save(delivery);
        deliveryFoodRepository.saveAll(deliveryFoods);
        return mapToDto(delivery);
    }

    public DeliveryDto getDeliveryById(Long id) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
        return deliveryOpt.map(this::mapToDto).orElse(null);
    }

    private DeliveryDto mapToDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getId());
        dto.setUserId(delivery.getReceiver().getId());
        dto.setDeliveryGuyId(delivery.getDeliveryGuy().getId());
        dto.setRestaurantId(delivery.getRestaurant().getId());
        dto.setAddress(delivery.getAddress());
        dto.setDate(delivery.getCreationDate());
        dto.setStatus(delivery.getStatus());
        dto.setPaymentMethod(delivery.getPaymentMethod());

        // Map the related DeliveryFood entities to DeliveryFoodDto
        List<DeliveryFoodDto> foodDtos = new ArrayList<>();
        for (DeliveryFood deliveryFood : delivery.getDeliveriesFoods()) {
            DeliveryFoodDto foodDto = new DeliveryFoodDto();
            foodDto.setDeliveryFoodId(deliveryFood.getFood().getId());
            foodDto.setQuantity(deliveryFood.getFoodCount());
            foodDtos.add(foodDto);
        }
        dto.setFoods(foodDtos);

        return dto;
    }
}