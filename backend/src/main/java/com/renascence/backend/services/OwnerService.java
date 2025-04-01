package com.renascence.backend.services;

import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.DeliveryGuy.DeliveryGuyDto;
import com.renascence.backend.entities.DeliveryGuy;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.entities.User;
import com.renascence.backend.repositories.DeliveryGuyRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import com.renascence.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final DeliveryGuyRepository deliveryGuyRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

//    public DeliveryGuy createDeliveryGuy(CreateDeliveryGuyDto createDeliveryGuyDto) {
//        // Validate and fetch User
//
//
//
//        // Validate and fetch Restaurant
//        Restaurant restaurant = restaurantRepository.findById(createDeliveryGuyDto.getRestaurantId())
//                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
//
//        // Map DTO to Entity
//        DeliveryGuy deliveryGuy = new DeliveryGuy();
//        deliveryGuy.setUser();
//        deliveryGuy.setIban(createDeliveryGuyDto.getIban());
//        deliveryGuy.setWorkPlace(restaurant);
//
//        // Save and return the entity
//        return deliveryGuyRepository.save(deliveryGuy);
//    }

    public List<DeliveryGuy> getAllDeliveryGuys() {
        return deliveryGuyRepository.findAll();
    }

    public DeliveryGuy getDeliveryGuyById(Long id) {
        return deliveryGuyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Guy not found with ID: " + id));
    }
}
