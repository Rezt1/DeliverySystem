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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryGuyService {

    private final DeliveryGuySalaryRepository deliveryGuySalaryRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public DeliveryDto acceptOrder(Long id, Long deliveryGuyId) {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
//        Optional<User> deliveryGuyOpt = userRepository.findByEmail(auth.getName());
//
//        if (deliveryOpt.isPresent() && deliveryGuyOpt.isPresent()) {
//            Delivery delivery = deliveryOpt.get();
//            delivery.setDeliveryGuy(deliveryGuyOpt.get().getDeliveryGuy());
//            delivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
//            deliveryRepository.save(delivery);
//            return mapToDeliveryGuyDto(delivery);
//        }
//        return null;
//    }

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

        // Check if already assigned
        if (delivery.getDeliveryGuy() != null) {
            throw new IllegalStateException("This delivery is already assigned");
        }

        // Assign the delivery to the delivery guy
        delivery.setDeliveryGuy(deliveryGuy);
        delivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
        delivery.setTakenByDeliveryGuyDate(LocalDateTime.now());
        deliveryRepository.save(delivery);

        return mapToDeliveryGuyDto(delivery);
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

        // Mark as delivered
        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredDate(LocalDateTime.now());
        deliveryRepository.save(delivery);

        // Give bonus
//        DeliveryGuySalary deliveryGuySalary = new DeliveryGuySalary();
//        deliveryGuySalary.setAmount(1.50);
//        deliveryGuySalary.setDeliveryGuy(deliveryGuy);
//        deliveryGuySalaryRepository.save(deliveryGuySalary);

        return mapToDeliveryGuyDto(delivery);
    }


    private DeliveryDto mapToDeliveryGuyDto(Delivery delivery) {
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
