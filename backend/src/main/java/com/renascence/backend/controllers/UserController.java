package com.renascence.backend.controllers;

import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.user.UpdateUserDto;
import com.renascence.backend.dtos.user.UserDto;
import com.renascence.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/my-active-orders")
    public ResponseEntity<List<DeliveryDto>> getActiveDeliveries() {
        List<DeliveryDto> deliveries = userService.getActiveDeliveries();

        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/my-past-orders")
    public ResponseEntity<List<DeliveryDto>> getPastDeliveries() {
        List<DeliveryDto> deliveries = userService.getPastDeliveries();

        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInformation() {
        UserDto userDto = userService.getUserInformation();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/apply-delivery-guy")
    public ResponseEntity<String> applyToBeDeliveryGuy(@RequestBody @Valid CreateDeliveryGuyDto createDeliveryGuyDto) {
        userService.applyToBeDeliveryGuy(createDeliveryGuyDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("You have successfully become a delivery guy.");
    }

    @PutMapping("/update-account")
    public ResponseEntity<UserDto> updateAccount(@RequestBody @Valid UpdateUserDto dto) {
        UserDto updatedUser = userService.updateUser(dto);

        return ResponseEntity.ok(updatedUser);
    }
}
