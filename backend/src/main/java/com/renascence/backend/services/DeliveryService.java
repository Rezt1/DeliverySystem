package com.renascence.backend.services;

import com.renascence.backend.dtos.Delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryGuyRepository deliveryGuyRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, UserRepository userRepository,
                           RestaurantRepository restaurantRepository, DeliveryGuyRepository deliveryGuyRepository) {
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.deliveryGuyRepository = deliveryGuyRepository;
    }

    @Transactional
    public DeliveryDto createDelivery(CreateDeliveryDto dto) {
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Delivery delivery = new Delivery();
        delivery.setReceiver(receiver);
        delivery.setRestaurant(restaurant);
        delivery.setPaymentMethod(dto.getPaymentMethod());
        delivery.setAddress(dto.getAddress());
        delivery.setDate(LocalDateTime.now());
        delivery.setStatus(DeliveryStatus.PENDING);

        deliveryRepository.save(delivery);
        return mapToDto(delivery);
    }

    @Transactional
    public DeliveryDto assignDeliveryGuy(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        if (delivery.getDeliveryGuy() != null) {
            throw new RuntimeException("Delivery already assigned");
        }

        Optional<DeliveryGuy> leastBusyGuy = deliveryGuyRepository.findAll()
                .stream()
                .min((g1, g2) -> Integer.compare(g1.getDeliveries().size(), g2.getDeliveries().size()));

        if (leastBusyGuy.isEmpty()) {
            throw new RuntimeException("No available delivery guys");
        }

        delivery.setDeliveryGuy(leastBusyGuy.get());
        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        deliveryRepository.save(delivery);
        return mapToDto(delivery);
    }

    @Transactional
    public DeliveryDto updateStatus(Long deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setStatus(status);
        deliveryRepository.save(delivery);
        return mapToDto(delivery);
    }

    public List<DeliveryDto> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private DeliveryDto mapToDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setId(delivery.getId());
        dto.setReceiverName(delivery.getReceiver().getName());
        dto.setAddress(delivery.getAddress());
        dto.setStatus(delivery.getStatus());
        dto.setDate(delivery.getDate());
        dto.setPaymentMethod(delivery.getPaymentMethod());
        dto.setRestaurantId(delivery.getRestaurant().getId());
        dto.setRestaurantId(delivery.getRestaurant().getId());
        if (delivery.getDeliveryGuy() != null) {
            dto.setDeliveryGuyId(delivery.getDeliveryGuy().getId());
            dto.setDeliveryGuyName(delivery.getDeliveryGuy().getUser().getName());
        }
        return dto;
    }
}
