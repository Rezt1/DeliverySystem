package com.renascence.backend.services;

import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryFood.DeliveryFoodDto;
import com.renascence.backend.dtos.deliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.user.UpdateUserDto;
import com.renascence.backend.dtos.user.UserDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DeliveryGuyRepository deliveryGuyRepository;
    private final CityRepository cityRepository;
    private final RoleRepository roleRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserInformation() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapToUserDto(user);
    }

    @Transactional
    public UserDto updateUser(UpdateUserDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());

        AccessToken accessToken = accessTokenRepository.findByUserId(user.getId());

        if (!user.getEmail().equals(dto.getEmail())){
            user.setEmail(dto.getEmail());
            accessToken.setRevoked(true);
        }

        if (dto.getPassword() != null
                && dto.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            accessToken.setRevoked(true);
        } else if (dto.getPassword() != null
                && !dto.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")) {
            throw new IllegalArgumentException("Password should contain at least: 1 lowercase letter, 1 uppercase letter, 1 digit, no spaces and tabs and should be at least 6 characters long!");
        }

        if (dto.getLocationId() != null){
            City city = cityRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("City not found"));

            user.setLocation(city);
        }

        userRepository.save(user);
        accessTokenRepository.save(accessToken);
        return mapToUserDto(user);
    }

    @Transactional
    public void applyToBeDeliveryGuy(CreateDeliveryGuyDto createDeliveryGuyDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getDeliveryGuy() != null && user.getDeliveryGuy().isFired()) {
            throw new IllegalStateException("You cannot become a delivery guy again");
        }

        // Check if user is already a delivery guy
        if (user.getDeliveryGuy() != null) {
            throw new IllegalStateException("You are already a delivery guy");
        }

        City applyCity = cityRepository.findById(createDeliveryGuyDto.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));

        if (applyCity.isDeleted()) {
            throw new EntityNotFoundException("City no longer exists in our system");
        }

        Set<String> ibans = deliveryGuyRepository
                .findAll()
                .stream()
                .map(DeliveryGuy::getIban)
                .collect(Collectors.toSet());

        if (ibans.contains(createDeliveryGuyDto.getIban())) {
            throw new IllegalArgumentException("Such iban already exists in our system");
        }

        // Create a new delivery guy record and associate it with the user
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setUser(user); // Associate the delivery guy with the user
        deliveryGuy.setWorkCity(applyCity);
        deliveryGuy.setIban(createDeliveryGuyDto.getIban());
        deliveryGuy.setStartWorkDate(LocalDate.now());

        Role role = roleRepository.findByName("ROLE_" + com.renascence.backend.enums.Role.DELIVERY_GUY.toString())
                .orElseThrow(() -> new EntityNotFoundException("Delivery guy role not found???????????????"));
        user.getRoles().add(role);

        AccessToken accessToken = accessTokenRepository.findByUserId(user.getId());
        accessToken.setRevoked(true);

        deliveryGuyRepository.save(deliveryGuy);
        userRepository.save(user);
        accessTokenRepository.save(accessToken);
    }

    public List<DeliveryDto> getActiveDeliveries() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getDeliveries()
                .stream()
                .filter(d -> d.getStatus() == DeliveryStatus.PENDING
                    || d.getStatus() == DeliveryStatus.OUT_FOR_DELIVERY)
                .map(this::mapToDeliveryDto)
                .toList();
    }

    public List<DeliveryDto> getPastDeliveries() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getDeliveries()
                .stream()
                .filter(d -> d.getStatus() == DeliveryStatus.DELIVERED)
                .map(this::mapToDeliveryDto)
                .toList();
    }

    private UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCityName(user.getLocation() != null ? user.getLocation().getName() : "No city chosen");
        return dto;
    }

    private DeliveryDto mapToDeliveryDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getId());
        dto.setUsername(delivery.getReceiver().getName());
        dto.setUserPhoneNumber(delivery.getReceiver().getPhoneNumber());
        dto.setDeliveryGuyName(delivery.getDeliveryGuy() != null ? delivery.getDeliveryGuy().getUser().getName() : "No delivery guy yet");
        dto.setRestaurantName(delivery.getRestaurant().getName());
        dto.setAddress(delivery.getAddress());
        dto.setCreationDate(delivery.getCreationDate());
        dto.setStatus(delivery.getStatus());
        dto.setPaymentMethod(delivery.getPaymentMethod());
        dto.setTotalPrice(delivery.getTotalPrice());
        dto.setToBeDeliveredTime(String
                .format("%d:%d", delivery.getToBeDeliveredHour().getHour(), delivery.getToBeDeliveredHour().getMinute()));

        // Map the related DeliveryFood entities to DeliveryFoodDto
        List<DeliveryFoodDto> foodDtos = new ArrayList<>();
        for (DeliveryFood deliveryFood : delivery.getDeliveriesFoods()) {
            DeliveryFoodDto foodDto = new DeliveryFoodDto();

            foodDto.setDeliveryFoodId(deliveryFood.getFood().getId());
            foodDto.setFoodName(deliveryFood.getFood().getName());
            foodDto.setQuantity(deliveryFood.getFoodCount());

            foodDtos.add(foodDto);
        }

        dto.setFoods(foodDtos);

        return dto;
    }
}
