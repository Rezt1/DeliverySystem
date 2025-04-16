package com.renascence.backend.service;

import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryGuySalary.DeliveryGuySalaryDto;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

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

        deliveryGuyService = Mockito.spy(new DeliveryGuyService(
                deliveryGuySalaryRepository,
                deliveryGuyRepository,
                deliveryRepository,
                userRepository,
                roleRepository,
                accessTokenRepository
        ));

        User receiver = new User();
        receiver.setName("Receiver Name");

        Delivery delivery = new Delivery();
        delivery.setReceiver(receiver);
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
    void testGetPendingDeliveries_shouldThrowWhenUserIsNotFound() {

        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.getPendingDeliveries());
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
    void testAssignDelivery_shouldSuccessfullyAssignDelivery() {
        User user = new User();
        user.setEmail("guy@example.com");

        City city = new City();
        city.setId(1L);
        city.setName("Sofia");

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setWorkCity(city);
        user.setDeliveryGuy(deliveryGuy);

        Restaurant restaurant = new Restaurant();
        restaurant.setCity(city);

        Delivery delivery = new Delivery();
        delivery.setRestaurant(restaurant);

        DeliveryDto expectedDto = new DeliveryDto();
        expectedDto.setAddress("Assigned address");

        when(authentication.getName()).thenReturn("guy@example.com");
        when(userRepository.findByEmail("guy@example.com")).thenReturn(Optional.of(user));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(delivery)).thenReturn(delivery);

        doReturn(expectedDto).when(deliveryGuyService).mapToDeliveryDto(delivery);

        DeliveryDto result = deliveryGuyService.assignDelivery(1L);

        assertEquals(expectedDto, result);
        assertEquals(deliveryGuy, delivery.getDeliveryGuy());
        assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, delivery.getStatus());
        assertNotNull(delivery.getTakenByDeliveryGuyDate());
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
    void testAssignDelivery_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("missing@example.com");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deliveryGuyService.assignDelivery(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAssignDelivery_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("noguy@example.com");
        user.setDeliveryGuy(null); // no delivery guy

        when(authentication.getName()).thenReturn("noguy@example.com");
        when(userRepository.findByEmail("noguy@example.com")).thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deliveryGuyService.assignDelivery(1L);
        });

        assertEquals("This user is not a delivery guy", exception.getMessage());
    }

    @Test
    void testAssignDelivery_shouldThrowWhenDeliveryNotFound() {
        User user = new User();
        user.setEmail("guy@example.com");

        City city = new City();
        city.setId(1L);

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setWorkCity(city);
        user.setDeliveryGuy(deliveryGuy);

        when(authentication.getName()).thenReturn("guy@example.com");
        when(userRepository.findByEmail("guy@example.com")).thenReturn(Optional.of(user));
        when(deliveryRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deliveryGuyService.assignDelivery(999L);
        });

        assertEquals("Delivery not found", exception.getMessage());
    }


//    @Test
//    void getCurrentDelivery_shouldReturnOldestActiveDelivery() {
//        Delivery activeDelivery;
//        Delivery secondActiveDelivery;
//
//        User user = new User();
//        user.setEmail("guy@example.com");
//        DeliveryGuy deliveryGuy = new DeliveryGuy();
//        user.setDeliveryGuy(deliveryGuy);
//
//        // Setup an active delivery
//        activeDelivery = new Delivery();
//        activeDelivery.setId(1L);
//        activeDelivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
//        activeDelivery.setTakenByDeliveryGuyDate(LocalDateTime.now().minusHours(1));
//        activeDelivery.setDeliveryGuy(deliveryGuy);
//
//        // Setup a second active delivery (should be returned second)
//        secondActiveDelivery = new Delivery();
//        secondActiveDelivery.setId(2L);
//        secondActiveDelivery.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
//        secondActiveDelivery.setTakenByDeliveryGuyDate(LocalDateTime.now());
//        secondActiveDelivery.setDeliveryGuy(deliveryGuy);
//
//        // Given - two active deliveries, expect the older one first
//        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(
//                eq(deliveryGuy.getId()),
//                eq(DeliveryStatus.OUT_FOR_DELIVERY)
//        )).thenReturn(Optional.of(activeDelivery));
//
//        // When
//        DeliveryDto result = deliveryGuyService.getCurrentDeliveryForDeliveryGuy();
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1L, result.getDeliveryId());
//    }

//    @Test
//    void getCurrentDelivery_shouldReturnNullWhenNoActiveDeliveries() {
//        // Given - no active deliveries
//        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(
//                anyLong(),
//                any()
//        )).thenReturn(Optional.empty());
//
//        // When
//        DeliveryDto result = deliveryGuyService.getCurrentDeliveryForDeliveryGuy();
//
//        // Then
//        assertNull(result);
//    }

//    @Test
//    void getCurrentDelivery_shouldOnlyReturnOutForDeliveryStatus() {
//        // Given - delivery in wrong status
//        Delivery wrongStatusDelivery = new Delivery();
//        wrongStatusDelivery.setStatus(DeliveryStatus.PENDING);
//
//        User user = new User();
//        user.setEmail("guy@example.com");
//        DeliveryGuy deliveryGuy = new DeliveryGuy();
//        user.setDeliveryGuy(deliveryGuy);
//
//        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(
//                eq(deliveryGuy.getId()),
//                eq(DeliveryStatus.OUT_FOR_DELIVERY)
//        )).thenReturn(Optional.empty());
//
//        // When
//        DeliveryDto result = deliveryGuyService.getCurrentDeliveryForDeliveryGuy();
//
//        // Then
//        assertNull(result);
//    }

    @Test
    void testGetCurrentDeliveryForDeliveryGuy_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("missing@example.com");
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deliveryGuyService.assignDelivery(1L);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetCurrentDeliveryForDeliveryGuy_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("noguy@example.com");
        user.setDeliveryGuy(null); // no delivery guy

        when(authentication.getName()).thenReturn("noguy@example.com");
        when(userRepository.findByEmail("noguy@example.com")).thenReturn(Optional.of(user));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deliveryGuyService.assignDelivery(1L);
        });

        assertEquals("This user is not a delivery guy", exception.getMessage());
    }

    @Test
    void testGetCurrentDeliveryForDeliveryGuy_shouldReturnNullIfNone() {
        User user = setupUserWithDeliveryGuy(1L);

        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(1L, DeliveryStatus.OUT_FOR_DELIVERY))
                .thenReturn(Optional.empty());

        assertNull(deliveryGuyService.getCurrentDeliveryForDeliveryGuy());
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
    void testMarkAsDelivered_shouldThrowWhenUserNotFound() {
        User user = new User();
        user.setEmail("unknown@user.com");
        user.setDeliveryGuy(null); // no delivery guy

        when(authentication.getName()).thenReturn("unknown@user.com");
        when(userRepository.findByEmail("unknown@user.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.markAsDelivered(1L));
    }

    @Test
    void testMarkAsDelivered_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("user@nomoto.com");
        user.setDeliveryGuy(null);

        when(userRepository.findByEmail("user@nomoto.com")).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("user@nomoto.com");

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.markAsDelivered(1L));
    }

    @Test
    void testMarkAsDelivered_shouldThrowWhenDeliveryNotFound() {
        User user = new User();
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        user.setDeliveryGuy(deliveryGuy);
        user.setEmail("guy@deli.com");

        when(userRepository.findByEmail("guy@deli.com")).thenReturn(Optional.of(user));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());
        when(authentication.getName()).thenReturn("guy@deli.com");

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.markAsDelivered(1L));
    }

    @Test
    void testMarkAsDelivered_shouldThrowWhenNoActiveDelivery() {
        User user = new User();
        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        user.setDeliveryGuy(deliveryGuy);
        user.setEmail("guy@deli.com");

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setRestaurant(new Restaurant());
        delivery.setReceiver(new User());

        when(userRepository.findByEmail("guy@deli.com")).thenReturn(Optional.of(user));
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.findFirstByDeliveryGuyIdAndStatusOrderByTakenByDeliveryGuyDateAsc(deliveryGuy.getId(), DeliveryStatus.OUT_FOR_DELIVERY))
                .thenReturn(Optional.empty());

        when(authentication.getName()).thenReturn("guy@deli.com");

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.markAsDelivered(1L));
    }

    @Test
    void testGetFinishedDeliveries_shouldReturnDeliveredDeliveries() {
        String email = "delivery@user.com";

        User user = new User();
        user.setEmail(email);

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);

        Delivery delivered1 = new Delivery();
        delivered1.setId(1L);
        delivered1.setStatus(DeliveryStatus.DELIVERED);
        delivered1.setRestaurant(new Restaurant());
        delivered1.setReceiver(new User());

        Delivery delivered2 = new Delivery();
        delivered2.setId(2L);
        delivered2.setStatus(DeliveryStatus.DELIVERED);
        delivered2.setRestaurant(new Restaurant());
        delivered2.setReceiver(new User());

        Delivery notDelivered = new Delivery();
        notDelivered.setId(3L);
        notDelivered.setStatus(DeliveryStatus.PENDING);

        List<Delivery> allDeliveries = Arrays.asList(delivered1, delivered2, notDelivered);
        deliveryGuy.setDeliveries(allDeliveries);
        user.setDeliveryGuy(deliveryGuy);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn(email);

        List<DeliveryDto> result = deliveryGuyService.getFinishedDeliveries();

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(d -> d.getDeliveryId().equals(1L) || d.getDeliveryId().equals(2L)));
    }

    @Test
    void testGetFinishedDeliveries_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("unknown@user.com");

        when(userRepository.findByEmail("unknown@user.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.getFinishedDeliveries());
    }

    @Test
    void testGetFinishedDeliveries_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("not@delivery.com");
        user.setDeliveryGuy(null);

        when(userRepository.findByEmail("not@delivery.com")).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("not@delivery.com");

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.getFinishedDeliveries());
    }



    @Test
    void testGetSalaries_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("ghost@user.com");
        when(userRepository.findByEmail("ghost@user.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.getSalaries());
    }

    @Test
    void testGetSalaries_shouldThrowWhenUserIsNotDeliveryGuy() {
        User user = new User();
        user.setEmail("not@delivery.com");
        user.setDeliveryGuy(null);

        when(userRepository.findByEmail("not@delivery.com")).thenReturn(Optional.of(user));
        when(authentication.getName()).thenReturn("not@delivery.com");

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.getSalaries());
    }



    @Test
    void testQuit_shouldSucceedAndReturnMessage() {
        String email = "delivery@user.com";

        Role deliveryGuyRole = new Role();
        deliveryGuyRole.setName("ROLE_DELIVERY_GUY");

        User user = new User();
        user.setEmail(email);
        user.setName("John Doe");
        user.setRoles(new HashSet<>(Collections.singleton(deliveryGuyRole)));

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        deliveryGuy.setId(1L);
        deliveryGuy.setUser(user);
        user.setDeliveryGuy(deliveryGuy);

        AccessToken token = new AccessToken();
        token.setId(deliveryGuy.getId());

        when(authentication.getName()).thenReturn(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.of(deliveryGuyRole));
        when(accessTokenRepository.findByUserId(deliveryGuy.getId())).thenReturn(token);

        String result = deliveryGuyService.quit();

        assertTrue(result.contains("Delivery guy John Doe with id 1 has quit successfully"));
        assertTrue(deliveryGuy.isFired());
        assertNotNull(deliveryGuy.getEndWorkDate());
        assertTrue(token.isRevoked());
        assertFalse(user.getRoles().contains(deliveryGuyRole));

        verify(accessTokenRepository).save(token);
        verify(deliveryGuyRepository).save(deliveryGuy);
    }

    @Test
    void testQuit_shouldThrowWhenUserNotFound() {
        when(authentication.getName()).thenReturn("ghost@user.com");
        when(userRepository.findByEmail("ghost@user.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.quit());
    }

    @Test
    void testQuit_shouldThrowWhenNotDeliveryGuy() {
        User user = new User();
        user.setEmail("not@delivery.com");
        user.setDeliveryGuy(null);

        when(authentication.getName()).thenReturn("not@delivery.com");
        when(userRepository.findByEmail("not@delivery.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () -> deliveryGuyService.quit());
    }


    @Test
    void testQuit_shouldThrowWhenRoleMissing() {
        User user = setupUserWithDeliveryGuy(1L);

        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deliveryGuyService.quit());
    }


}
