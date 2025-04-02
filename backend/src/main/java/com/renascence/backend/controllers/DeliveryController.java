package com.renascence.backend.controllers;

import com.renascence.backend.dtos.Delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.entities.Delivery;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.services.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDto createDelivery(@RequestBody @Valid CreateDeliveryDto createDeliveryDto) {
        return deliveryService.createDelivery(createDeliveryDto);
    }

    @GetMapping("/{id}")
    public DeliveryDto getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

    @PutMapping("/{id}/status")
    public DeliveryDto updateDeliveryStatus(@PathVariable Long id, @RequestParam DeliveryStatus status) {
        return deliveryService.updateDeliveryStatus(id, status);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<DeliveryDto> assignDeliveryGuy(@PathVariable Long id, @RequestParam Long deliveryGuyId) {
        DeliveryDto updatedDelivery = deliveryService.assignDeliveryGuy(id, deliveryGuyId);
        return updatedDelivery != null ? ResponseEntity.ok(updatedDelivery) : ResponseEntity.notFound().build();
    }
}
