package com.renascence.backend.services;

import com.renascence.backend.dtos.DeliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.User.UserDto;
import com.renascence.backend.entities.City;
import com.renascence.backend.entities.DeliveryGuy;
import com.renascence.backend.entities.User;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.repositories.DeliveryGuyRepository;
import com.renascence.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DeliveryGuyRepository deliveryGuyRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserInformation() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return convertToDto(user);
    }

    @Transactional
    public User updateUser(UserDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void applyToBeDeliveryGuy(CreateDeliveryGuyDto createDeliveryGuyDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user is already a delivery guy
        if (user.getDeliveryGuy() != null) {
            throw new IllegalStateException("You are already a delivery guy");
        }

        City applyCity = cityRepository.findById(createDeliveryGuyDto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        // Create a new delivery guy record and associate it with the user
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setUser(user); // Associate the delivery guy with the user
        deliveryGuy.setWorkCity(applyCity);
        deliveryGuy.setIban(createDeliveryGuyDto.getIban());
        deliveryGuy.setStartWorkDate(LocalDate.now());

        deliveryGuyRepository.save(deliveryGuy);

//        user.setDeliveryGuy(deliveryGuy); MAYBE NOT NEEDED, TESTING REQUIRED
//
//        userRepository.save(user);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getPhoneNumber(), user.getLocation().getId(), user.getPassword());
    }
}
