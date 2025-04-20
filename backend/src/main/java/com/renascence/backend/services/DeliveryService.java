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

        if (restaurant.isDeleted()){
            throw new EntityNotFoundException("Restaurant no longer exists in our system");
        }

        Delivery delivery = new Delivery();

        delivery.setAddress(createDeliveryDto.getAddress());
        delivery.setCreationDate(LocalDateTime.now());
        delivery.setPaymentMethod(createDeliveryDto.getPaymentMethod());
        delivery.setReceiver(user);
        delivery.setRestaurant(restaurant);
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setTotalPrice(createDeliveryDto.getTotalPrice());

        int hour = Integer.parseInt(createDeliveryDto.getHourToBeDelivered().substring(0, 2));
        int minutes = Integer.parseInt(createDeliveryDto.getHourToBeDelivered().substring(3, 5));

        delivery.setToBeDeliveredHour(LocalDateTime.of(LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                hour,
                minutes));

        if (delivery.getToBeDeliveredHour().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot make an order for the past");
        }

        List<DeliveryFood> deliveryFoods = createDeliveryDto.getFoods().stream()
                .map(deliveryFoodDto -> {
                    Food food = foodRepository.findById(deliveryFoodDto.getFoodId())
                            .orElseThrow(() -> new EntityNotFoundException("Food not found: " + deliveryFoodDto.getFoodId()));

                    if (food.isDeleted()) {
                        throw new EntityNotFoundException(String.format("Food %s with id %d no longer exists", food.getName(), food.getId()));
                    }

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getDeliveries().contains(delivery)){
            throw new IllegalStateException("Delivery is not yours");
        }

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
        dto.setTotalPrice(delivery.getTotalPrice());
        dto.setToBeDeliveredTime(String
                .format("%d:%d", delivery.getToBeDeliveredHour().getHour(), delivery.getToBeDeliveredHour().getMinute()));

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