package com.renascence.backend.controllers;

import com.renascence.backend.dtos.delivery.CreateDeliveryDto;
import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.services.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create-delivery")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDto createDelivery(@RequestBody @Valid CreateDeliveryDto createDeliveryDto) {
        return deliveryService.createDelivery(createDeliveryDto);
    }

    @GetMapping("/{id}")
    public DeliveryDto getDeliveryById(@PathVariable Long id) {
        return deliveryService.getDeliveryById(id);
    }

}
