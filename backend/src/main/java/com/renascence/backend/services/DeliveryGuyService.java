package com.renascence.backend.services;


import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.dtos.DeliveryFood.DeliveryFoodDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.DeliveryGuySalaryRepository;
import com.renascence.backend.repositories.DeliveryRepository;
import com.renascence.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class DeliveryGuyService {

    private final DeliveryGuySalaryRepository deliveryGuySalaryRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    public List<DeliveryDto> getPendingDeliveries() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User does not exists"));

        DeliveryGuy deliveryGuy = user.getDeliveryGuy();

        if (deliveryGuy == null){
            throw new IllegalStateException("User is not a delivery guy");
        }

        if (deliveryGuy.getWorkCity() == null){
            throw new IllegalStateException("Cannot take a delivery from another city");
        }

        List<Delivery> deliveries = deliveryRepository
                .findByStatusAndRestaurant_City_Id(DeliveryStatus.PENDING, deliveryGuy.getWorkCity().getId());

        return deliveries.stream().map(this::mapToDeliveryDto).collect(Collectors.toList());
    }


    @Transactional
    public DeliveryDto assignDelivery(Long deliveryId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Get the current authenticated user
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DeliveryGuy deliveryGuy = user.getDeliveryGuy();
        if (deliveryGuy == null) {
            throw new IllegalStateException("This user is not a delivery guy");
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));

        if (deliveryGuy.getWorkCity().getId() != delivery.getRestaurant().getCity().getId()){
            throw new IllegalStateException("Delivery is not made in your city area!");
        }

        // Check if already assigned
        if (delivery.getDeliveryGuy() != null) {
            throw new IllegalStateException("This delivery is already assigned");
        }

        // Assign the delivery to the delivery guy
        delivery.setDeliveryGuy(deliveryGuy);
        delivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
        delivery.setTakenByDeliveryGuyDate(LocalDateTime.now());
        deliveryRepository.save(delivery);

        return mapToDeliveryDto(delivery);
    }

    public DeliveryDto getCurrentDeliveryForDeliveryGuy() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DeliveryGuy deliveryGuy = user.getDeliveryGuy();
        if (deliveryGuy == null) {
            throw new IllegalStateException("User is not a delivery guy");
        }

        Optional<Delivery> activeDeliveryOpt = deliveryRepository
                .findFirstByDeliveryGuyIdAndStatusOrderByCreationDateAsc(
                        deliveryGuy.getId(), DeliveryStatus.OUT_FOR_DELIVERY
                );

        return activeDeliveryOpt
                .map(this::mapToDeliveryDto)
                .orElse(null);
    }

    @Transactional
    public DeliveryDto markAsDelivered(Long deliveryId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Get the authenticated user (delivery guy)
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DeliveryGuy deliveryGuy = user.getDeliveryGuy();
        if (deliveryGuy == null) {
            throw new IllegalStateException("User is not a delivery guy");
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));

        Delivery correctDeliveryToFinish = deliveryRepository
                .findFirstByDeliveryGuyIdAndStatusOrderByCreationDateAsc(deliveryGuy.getId(), DeliveryStatus.OUT_FOR_DELIVERY)
                .orElseThrow(() -> new EntityNotFoundException("Your deliveries have been made"));

        if (correctDeliveryToFinish.getId() != deliveryId){
            throw new IllegalStateException("Already delivered, still not its turn, not taken or not in your city area");
        }

        // Mark as delivered
        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredDate(LocalDateTime.now());
        deliveryRepository.save(delivery);

        return mapToDeliveryDto(delivery);
    }


    private DeliveryDto mapToDeliveryDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getId());
        dto.setUserId(delivery.getReceiver().getId());
        //dto.setDeliveryGuyId(delivery.getDeliveryGuy().getId());
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