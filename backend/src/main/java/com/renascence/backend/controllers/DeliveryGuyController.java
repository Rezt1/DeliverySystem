package com.renascence.backend.controllers;

import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.services.DeliveryGuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/my-active-delivery")
    public ResponseEntity<DeliveryDto> getCurrentDeliveryForDeliveryGuy() {
        DeliveryDto dto = deliveryGuyService.getCurrentDeliveryForDeliveryGuy();
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/pending-deliveries")
    public ResponseEntity<List<DeliveryDto>> getPendingDeliveries() {
        List<DeliveryDto> deliveries = deliveryGuyService.getPendingDeliveries();

        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/my-finished-deliveries")
    public ResponseEntity<List<DeliveryDto>> getMyFinishedDeliveries() {
        List<DeliveryDto> deliveries = deliveryGuyService.getFinishedDeliveries();

        return ResponseEntity.ok(deliveries);
    }
}
