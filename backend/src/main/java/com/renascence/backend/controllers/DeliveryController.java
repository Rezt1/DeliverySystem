package com.renascence.backend.controllers;

import com.renascence.backend.dtos.delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.services.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create-delivery")
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody @Valid CreateDeliveryDto createDeliveryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deliveryService.createDelivery(createDeliveryDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity
                .ok(deliveryService.getDeliveryById(id));
    }
}
