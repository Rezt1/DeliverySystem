package com.renascence.backend.services;

import com.renascence.backend.dtos.Delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.entities.Delivery;
import com.renascence.backend.entities.Restaurant;
import com.renascence.backend.entities.User;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.DeliveryRepository;
import com.renascence.backend.repositories.RestaurantRepository;
import com.renascence.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public DeliveryDto createDelivery(CreateDeliveryDto createDeliveryDto) {
        Optional<User> userOpt = userRepository.findById(createDeliveryDto.getUserId());
        Optional<User> deliveryGuyOpt = userRepository.findById(createDeliveryDto.getDeliveryGuyId());
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
        delivery.setDate(LocalDateTime.now());
        delivery.setDeliveryGuy(deliveryGuyOpt.get().getDeliveryGuy());
        delivery.setPaymentMethod(createDeliveryDto.getPaymentMethod());
        delivery.setReceiver(userOpt.get());
        delivery.setRestaurant(restaurantOpt.get());
        delivery.setStatus(DeliveryStatus.PENDING);
        deliveryRepository.save(delivery);

        return mapToDto(delivery);
    }

    public DeliveryDto getDeliveryById(Long id) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
        return deliveryOpt.map(this::mapToDto).orElse(null);
    }

    @Transactional
    public DeliveryDto updateDeliveryStatus(Long id, DeliveryStatus status) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
        if (deliveryOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            delivery.setStatus(status);
            deliveryRepository.save(delivery);
            return mapToDto(delivery);
        }
        return null;
    }

    @Transactional
    public DeliveryDto assignDeliveryGuy(Long id, Long deliveryGuyId) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
        Optional<User> deliveryGuyOpt = userRepository.findById(deliveryGuyId);

        if (deliveryOpt.isPresent() && deliveryGuyOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            delivery.setDeliveryGuy(deliveryGuyOpt.get().getDeliveryGuy());
            deliveryRepository.save(delivery);
            return mapToDto(delivery);
        }
        return null;
    }

    private DeliveryDto mapToDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getId());
        dto.setUserId(delivery.getReceiver().getId());
        dto.setDeliveryGuyId(delivery.getDeliveryGuy().getId());
        dto.setAddress(delivery.getAddress());
        dto.setDate(delivery.getDate());
        dto.setStatus(delivery.getStatus());
        dto.setPaymentMethod(delivery.getPaymentMethod());
        return dto;
    }
}