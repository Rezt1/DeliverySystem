package com.renascence.backend.controllers;

import com.renascence.backend.dtos.deliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.user.UpdateUserDto;
import com.renascence.backend.dtos.user.UserDto;
import com.renascence.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/update-account")
    public ResponseEntity<UserDto> updateAccount(@RequestBody @Valid UpdateUserDto dto) {
        UserDto updatedUser = userService.updateUser(dto);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInformation() {
        UserDto userDto = userService.getUserInformation();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/apply-delivery-guy")
    public ResponseEntity<String> applyToBeDeliveryGuy(@RequestBody @Valid CreateDeliveryGuyDto createDeliveryGuyDto) {
        userService.applyToBeDeliveryGuy(createDeliveryGuyDto);

        return ResponseEntity.ok("You have successfully become a delivery guy.");
    }
}
