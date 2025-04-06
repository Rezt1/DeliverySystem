package com.renascence.backend.controllers;

import com.renascence.backend.dtos.Delivery.DeliveryDto;
import com.renascence.backend.services.DeliveryGuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-guys")
@RequiredArgsConstructor
public class DeliveryGuyController {

    private final DeliveryGuyService deliveryGuyService;

    @PutMapping("/deliveries/{id}/assign")
    public ResponseEntity<DeliveryDto> assignDelivery(@PathVariable Long id) {
        DeliveryDto delivery = deliveryGuyService.assignDelivery(id);
        return ResponseEntity.ok(delivery);
    }

    @PutMapping("/deliveries/{id}/delivered")
    public ResponseEntity<DeliveryDto> markAsDelivered(@PathVariable Long id) {
        DeliveryDto delivery = deliveryGuyService.markAsDelivered(id);
        return ResponseEntity.ok(delivery);
    }
}
