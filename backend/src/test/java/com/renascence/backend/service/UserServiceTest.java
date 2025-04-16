package com.renascence.backend.service;

import com.renascence.backend.dtos.delivery.DeliveryDto;
import com.renascence.backend.dtos.deliveryGuy.CreateDeliveryGuyDto;
import com.renascence.backend.dtos.user.UpdateUserDto;
import com.renascence.backend.dtos.user.UserDto;
import com.renascence.backend.entities.*;
import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.enums.PaymentMethod;
import com.renascence.backend.repositories.*;
import com.renascence.backend.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.renascence.backend.enums.PaymentMethod.CARD;
import static com.renascence.backend.enums.PaymentMethod.CASH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private DeliveryGuyRepository deliveryGuyRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("john@example.com");
    }

    @Test
    void testGetUserInformation_successful() {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("123456789");

        City city = new City();
        city.setName("Sofia");
        user.setLocation(city);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // When
        UserDto userDto = userService.getUserInformation();

        // Then
        assertNotNull(userDto);
        assertEquals("John Doe", userDto.getName());
        assertEquals("john@example.com", userDto.getEmail());
        assertEquals("123456789", userDto.getPhoneNumber());
        assertEquals("Sofia", userDto.getCityName());
    }

    @Test
    void testGetUserInformation_userNotFound() {
        // Given
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserInformation());

        assertEquals("User not found", exception.getMessage());
    }



    @Test
    void testUpdateUser_successfulUpdate() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        AccessToken accessToken = new AccessToken();
        City city = new City();
        city.setId(2L);
        city.setName("Plovdiv");

        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("New Name");
        dto.setPhoneNumber("987654321");
        dto.setEmail("john@example.com");
        dto.setLocationId(2L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(accessToken);
        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));

        UserDto result = userService.updateUser(dto);

        assertEquals("New Name", result.getName());
        assertEquals("987654321", result.getPhoneNumber());
        assertEquals("Plovdiv", result.getCityName());

        verify(userRepository).save(user);
        verify(accessTokenRepository).save(accessToken);
    }

    @Test
    void testUpdateUser_changeEmail_shouldRevokeToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("old@example.com");

        AccessToken accessToken = new AccessToken();

        UpdateUserDto dto = new UpdateUserDto();
        dto.setEmail("new@example.com");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(accessToken);

        userService.updateUser(dto);

        assertEquals("new@example.com", user.getEmail());
        assertTrue(accessToken.isRevoked());
    }

    @Test
    void testUpdateUser_validPassword_shouldEncodeAndRevokeToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        AccessToken accessToken = new AccessToken();

        UpdateUserDto dto = new UpdateUserDto();
        dto.setPassword("Valid123");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(accessToken);
        when(passwordEncoder.encode("Valid123")).thenReturn("encoded");

        userService.updateUser(dto);

        assertEquals("encoded", user.getPassword());
        assertTrue(accessToken.isRevoked());
    }

    @Test
    void testUpdateUser_invalidPassword_shouldThrowException() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        UpdateUserDto dto = new UpdateUserDto();
        dto.setPassword("weak");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(new AccessToken());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(dto));

        assertEquals("Password should contain at least: 1 lowercase letter, 1 uppercase letter, 1 digit, no spaces and tabs and should be at least 6 characters long!", ex.getMessage());
    }

    @Test
    void testUpdateUser_cityNotFound_shouldThrowException() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        UpdateUserDto dto = new UpdateUserDto();
        dto.setLocationId(123L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(new AccessToken());
        when(cityRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(dto));
    }

    @Test
    void testUpdateUser_userNotFound_shouldThrowException() {
        UpdateUserDto dto = new UpdateUserDto();
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(dto));
    }



    @Test
    void testApplyToBeDeliveryGuy_successfulApplication() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        dto.setCityId(10L);
        dto.setIban("BG123");

        City city = new City();
        city.setId(10L);
        city.setDeleted(false);

        AccessToken token = new AccessToken();

        Role role = new Role();
        role.setName("ROLE_DELIVERY_GUY");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(deliveryGuyRepository.findAll()).thenReturn(List.of()); // No duplicate IBANs
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.of(role));
        when(accessTokenRepository.findByUserId(1L)).thenReturn(token);

        userService.applyToBeDeliveryGuy(dto);

        verify(deliveryGuyRepository).save(any(DeliveryGuy.class));
        verify(userRepository).save(user);
        verify(accessTokenRepository).save(token);
        assertTrue(user.getRoles().contains(role));
        assertTrue(token.isRevoked());
    }

    @Test
    void testApplyToBeDeliveryGuy_userAlreadyDeliveryGuy_shouldThrow() {
        User user = new User();
        user.setEmail("john@example.com");

        DeliveryGuy deliveryGuy = new DeliveryGuy();
        user.setDeliveryGuy(deliveryGuy); // Already delivery guy

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();

        assertThrows(IllegalStateException.class, () -> userService.applyToBeDeliveryGuy(dto));
    }

    @Test
    void testApplyToBeDeliveryGuy_userWasFiredDeliveryGuy_shouldThrow() {
        User user = new User();
        user.setEmail("john@example.com");

        DeliveryGuy firedGuy = new DeliveryGuy();
        firedGuy.setFired(true);
        user.setDeliveryGuy(firedGuy);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userService.applyToBeDeliveryGuy(dto));
        assertEquals("You cannot become a delivery guy again", ex.getMessage());
    }

    @Test
    void testApplyToBeDeliveryGuy_cityNotFound_shouldThrow() {
        User user = new User();
        user.setEmail("john@example.com");

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        dto.setCityId(10L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cityRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.applyToBeDeliveryGuy(dto));
    }

    @Test
    void testApplyToBeDeliveryGuy_cityIsDeleted_shouldThrow() {
        User user = new User();
        user.setEmail("john@example.com");

        City city = new City();
        city.setId(10L);
        city.setDeleted(true);

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        dto.setCityId(10L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));

        assertThrows(EntityNotFoundException.class, () -> userService.applyToBeDeliveryGuy(dto));
    }

    @Test
    void testApplyToBeDeliveryGuy_ibanAlreadyExists_shouldThrow() {
        User user = new User();
        user.setEmail("john@example.com");

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        dto.setCityId(10L);
        dto.setIban("DUPLICATE_IBAN");

        City city = new City();
        city.setDeleted(false);

        DeliveryGuy existing = new DeliveryGuy();
        existing.setIban("DUPLICATE_IBAN");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(deliveryGuyRepository.findAll()).thenReturn(List.of(existing));

        assertThrows(IllegalArgumentException.class, () -> userService.applyToBeDeliveryGuy(dto));
    }

    @Test
    void testApplyToBeDeliveryGuy_userNotFound_shouldThrow() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        assertThrows(EntityNotFoundException.class, () -> userService.applyToBeDeliveryGuy(dto));
    }

    @Test
    void testApplyToBeDeliveryGuy_roleNotFound_shouldThrow() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");

        CreateDeliveryGuyDto dto = new CreateDeliveryGuyDto();
        dto.setCityId(10L);
        dto.setIban("BG999");

        City city = new City();
        city.setId(10L);
        city.setDeleted(false);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(deliveryGuyRepository.findAll()).thenReturn(List.of()); // No duplicate IBANs
        when(roleRepository.findByName("ROLE_DELIVERY_GUY")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                userService.applyToBeDeliveryGuy(dto)
        );

        assertEquals("Delivery guy role not found???????????????", ex.getMessage());
    }



    @Test
    void testGetActiveDeliveries_shouldReturnPendingAndOutForDelivery() {
        // Arrange
        String email = "john@example.com";
        User user = new User();
        user.setEmail(email);

        Delivery delivery1 = new Delivery();
        delivery1.setStatus(DeliveryStatus.PENDING);
        delivery1.setReceiver(user);
        delivery1.setRestaurant(new Restaurant());
        delivery1.setAddress("Address 1");
        delivery1.setCreationDate(LocalDateTime.now());
        delivery1.setPaymentMethod(CARD);
        delivery1.setDeliveriesFoods(new ArrayList<>());

        Delivery delivery2 = new Delivery();
        delivery2.setStatus(DeliveryStatus.OUT_FOR_DELIVERY);
        delivery2.setReceiver(user);
        delivery2.setRestaurant(new Restaurant());
        delivery2.setAddress("Address 2");
        delivery2.setCreationDate(LocalDateTime.now());
        delivery2.setPaymentMethod(CASH);
        delivery2.setDeliveriesFoods(new ArrayList<>());

        user.setDeliveries(List.of(delivery1, delivery2));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));


        // Act
        List<DeliveryDto> result = userService.getActiveDeliveries();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto ->
                dto.getStatus() == DeliveryStatus.PENDING || dto.getStatus() == DeliveryStatus.OUT_FOR_DELIVERY));
    }


    @Test
    void testGetActiveDeliveries_userNotFound_shouldThrow() {

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                userService.getActiveDeliveries());

        assertEquals("User not found", ex.getMessage());
    }



    @Test
    void testGetPastDeliveries_shouldReturnDeliveredOnly() {
        // Arrange
        String email = "john@example.com";
        User user = new User();
        user.setEmail(email);

        Delivery delivered = new Delivery();
        delivered.setStatus(DeliveryStatus.DELIVERED);
        delivered.setReceiver(user);
        delivered.setRestaurant(new Restaurant());
        delivered.setAddress("Old Address");
        delivered.setCreationDate(LocalDateTime.now().minusDays(2));
        delivered.setPaymentMethod(CARD);
        delivered.setDeliveriesFoods(new ArrayList<>());

        Delivery pending = new Delivery();
        pending.setStatus(DeliveryStatus.PENDING);  // Should be excluded
        pending.setReceiver(user);
        pending.setRestaurant(new Restaurant());
        pending.setDeliveriesFoods(new ArrayList<>());

        user.setDeliveries(List.of(delivered, pending));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        List<DeliveryDto> result = userService.getPastDeliveries();

        // Assert
        assertEquals(1, result.size());
        assertEquals(DeliveryStatus.DELIVERED, result.getFirst().getStatus());
    }


    @Test
    void testGetPastDeliveries_userNotFound_shouldThrow() {

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                userService.getActiveDeliveries());

        assertEquals("User not found", ex.getMessage());
    }
}

