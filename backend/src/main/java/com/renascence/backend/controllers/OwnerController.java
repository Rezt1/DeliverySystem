package com.renascence.backend.controllers;

import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.DeliveryGuy.DeliveryGuyDto;
import com.renascence.backend.dtos.Food.FoodDto;
import com.renascence.backend.entities.DeliveryGuy;
import com.renascence.backend.services.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/delivery-guys")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/create-delivery-guy")
    public ResponseEntity<DeliveryGuyDto> createDeliveryGuy(@RequestBody @Valid CreateDeliveryGuyDto createDeliveryGuyDto) {
        DeliveryGuy deliveryGuy = ownerService.createDeliveryGuy(createDeliveryGuyDto);

        // Map Entity to DTO
        DeliveryGuyDto deliveryGuyDto = new DeliveryGuyDto();
        deliveryGuyDto.setUserId(deliveryGuy.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryGuyDto);
    }

    @GetMapping("/get-all-delivery-guys")
    public ResponseEntity<List<DeliveryGuyDto>> getAllDeliveryGuys() {
        List<DeliveryGuy> deliveryGuys = ownerService.getAllDeliveryGuys();

        // Map entities to DTOs
        List<DeliveryGuyDto> deliveryGuyDtos = deliveryGuys.stream()
                .map(deliveryGuy -> {
                    DeliveryGuyDto dto = new DeliveryGuyDto();
                    dto.setUserId(deliveryGuy.getUser().getId());
                    dto.setIban(deliveryGuy.getIban());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(deliveryGuyDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryGuy> getDeliveryGuyById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getDeliveryGuyById(id));
    }
}
