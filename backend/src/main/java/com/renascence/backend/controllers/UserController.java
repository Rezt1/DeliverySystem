package com.renascence.backend.controllers;

import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.User.UserDto;
import com.renascence.backend.entities.User;
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
    public ResponseEntity<UserDto> updateAccount(@RequestBody UserDto dto) {
        UserDto updatedUser = userService.updateUser(dto);
        //UserDto responseDto = mapToUserDto(updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserInformation() {
        UserDto userDto = userService.getUserInformation();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/apply-delivery-guy")
    public ResponseEntity<String> applyToBeDeliveryGuy(@RequestBody @Valid CreateDeliveryGuyDto createDeliveryGuyDto) {
        try {
            userService.applyToBeDeliveryGuy(createDeliveryGuyDto);

            return ResponseEntity.ok("You have successfully applied to be a delivery guy.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
