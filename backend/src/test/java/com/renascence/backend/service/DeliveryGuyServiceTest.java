package com.renascence.backend.service;


import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.repositories.*;
import com.renascence.backend.services.DeliveryGuyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryGuyServiceTest {

    @InjectMocks
    private DeliveryGuyService deliveryGuyService;

    @Mock
    private DeliveryGuySalaryRepository deliveryGuySalaryRepository;
    @Mock
    private DeliveryGuyRepository deliveryGuyRepository;
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private User setupUserWithDeliveryGuy(Long cityId) {
        City city = new City();
        city.setId(cityId);

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setWorkCity(city);
        deliveryGuy.setId(1L);

        User user = new User();
        user.setEmail("guy@example.com");
        user.setDeliveryGuy(deliveryGuy);

        when(authentication.getName()).thenReturn("guy@example.com");
        when(userRepository.findByEmail("guy@example.com")).thenReturn(Optional.of(user));
        return user;
    }



    @Test
    void testGetPendingDeliveries_shouldReturnDeliveries() {
        User user = setupUserWithDeliveryGuy(100L);

        Restaurant restaurant = new Restaurant();
        City city = new City();
        city.setId(100L);
        restaurant.setCity(city);

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setReceiver(user);
        delivery.setRestaurant(restaurant);
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setAddress("Test Address");

        when(deliveryRepository.findByStatusAndRestaurant_City_Id(DeliveryStatus.PENDING, 100L))
                .thenReturn(List.of(delivery));

        List<DeliveryDto> result = deliveryGuyService.getPendingDeliveries();
        assertEquals(1, result.size());
        assertEquals("Test Address", result.getFirst().getAddress());
    }

    @Test
    void testAssignDelivery_shouldThrowWhenDeliveryNotInSameCity() {
        User user = setupUserWithDeliveryGuy(1L);

        City otherCity = new City();
        otherCity.setId(2L);
        Restaurant restaurant = new Restaurant();
        restaurant.setCity(otherCity);

        Delivery delivery = new Delivery();
        delivery.setId(10L);
        delivery.setRestaurant(restaurant);

        when(deliveryRepository.findById(10L)).thenReturn(Optional.of(delivery));

        Exception exception = assertThrows(IllegalStateException.class, () -> deliveryGuyService.assignDelivery(10L));
        assertTrue(exception.getMessage().contains("not made in your city area"));
    }

    @Test
    void testGetPendingDeliveries_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("noGuy@example.com");

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.getPendingDeliveries());
    }

    @Test
    void testAssignDelivery_shouldThrowWhenAlreadyAssigned() {
        User user = setupUserWithDeliveryGuy(1L);

        Delivery delivery = new Delivery();
        delivery.setId(20L);
        delivery.setRestaurant(new Restaurant());
        delivery.getRestaurant().setCity(user.getDeliveryGuy().getWorkCity());
        delivery.setDeliveryGuy(new DeliveryGuy());

        when(deliveryRepository.findById(20L)).thenReturn(Optional.of(delivery));

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.assignDelivery(20L));
    }

    @Test
    void testMarkAsDelivered_shouldThrowWhenDeliveryIdMismatch() {
        User user = setupUserWithDeliveryGuy(1L);

        Delivery requested = new Delivery();
        requested.setId(77L);
        Delivery correct = new Delivery();
        correct.setId(99L);

        when(deliveryRepository.findById(77L)).thenReturn(Optional.of(requested));
        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(1L, DeliveryStatus.OUT_FOR_DELIVERY))
                .thenReturn(Optional.of(correct));

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.markAsDelivered(77L));
    }

    @Test
    void testGetCurrentDeliveryForDeliveryGuy_shouldReturnNullIfNone() {
        User user = setupUserWithDeliveryGuy(1L);

        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(1L, DeliveryStatus.OUT_FOR_DELIVERY))
                .thenReturn(Optional.empty());

        assertNull(deliveryGuyService.getCurrentDeliveryForDeliveryGuy());
    }

    @Test
    void testQuit_shouldThrowWhenRoleMissing() {
        User user = setupUserWithDeliveryGuy(1L);

        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.quit());
    }

}
