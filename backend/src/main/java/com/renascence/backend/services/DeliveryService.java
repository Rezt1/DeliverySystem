package com.renascence.backend.services;

import com.renascence.backend.dtos.delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryFood.DeliveryFoodDto;
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

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Food firstFood = foodRepository.findById(createDeliveryDto.getFoods().getFirst().getFoodId())
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));
        Restaurant restaurant = firstFood.getRestaurant();

        Delivery delivery = new Delivery();

        delivery.setAddress(createDeliveryDto.getAddress());
        delivery.setCreationDate(LocalDateTime.now());
        delivery.setPaymentMethod(createDeliveryDto.getPaymentMethod());
        delivery.setReceiver(user);
        delivery.setRestaurant(restaurant);
        delivery.setStatus(DeliveryStatus.PENDING);

        List<DeliveryFood> deliveryFoods = createDeliveryDto.getFoods().stream()
                .map(deliveryFoodDto -> {
                    Food food = foodRepository.findById(deliveryFoodDto.getFoodId())
                            .orElseThrow(() -> new EntityNotFoundException("Food not found: " + deliveryFoodDto.getFoodId()));

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
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
        return mapToDto(delivery);
    }

    private DeliveryDto mapToDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getId());
        dto.setUsername(delivery.getReceiver().getName());
        dto.setUserPhoneNumber(delivery.getReceiver().getPhoneNumber());
        dto.setDeliveryGuyName(delivery.getDeliveryGuy() != null ? delivery.getDeliveryGuy().getUser().getName() : "No delivery guy yet");
        dto.setRestaurantName(delivery.getRestaurant().getName());
        dto.setAddress(delivery.getAddress());
        dto.setCreationDate(delivery.getCreationDate());
        dto.setStatus(delivery.getStatus());
        dto.setPaymentMethod(delivery.getPaymentMethod());

        // Map the related DeliveryFood entities to DeliveryFoodDto
        List<DeliveryFoodDto> foodDtos = new ArrayList<>();
        for (DeliveryFood deliveryFood : delivery.getDeliveriesFoods()) {
            DeliveryFoodDto foodDto = new DeliveryFoodDto();

            foodDto.setDeliveryFoodId(deliveryFood.getFood().getId());
            foodDto.setFoodName(deliveryFood.getFood().getName());
            foodDto.setQuantity(deliveryFood.getFoodCount());

            foodDtos.add(foodDto);
        }

        dto.setFoods(foodDtos);

        return dto;
    }
}