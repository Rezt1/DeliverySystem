package com.renascence.backend.service;

import com.renascence.backend.dtos.authorization.AuthResponseDto;
import com.renascence.backend.dtos.authorization.LoginRequestDto;
import com.renascence.backend.dtos.authorization.RegisterDto;
import com.renascence.backend.entities.AccessToken;
import com.renascence.backend.entities.City;
import com.renascence.backend.entities.CustomUserDetails;
import com.renascence.backend.entities.User;
import com.renascence.backend.exceptionHandlers.ErrorResponse;
import com.renascence.backend.repositories.AccessTokenRepository;
import com.renascence.backend.repositories.CityRepository;
import com.renascence.backend.services.AuthService;
import com.renascence.backend.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;

import com.renascence.backend.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private AccessTokenRepository accessTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }



    @Test
    void login_whenAlreadyAuthenticated_shouldReturnForbidden() {
        Authentication mockAuth = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(mockAuth);
        when(mockAuth.isAuthenticated()).thenReturn(true);

        LoginRequestDto loginDto = new LoginRequestDto("test@example.com", "password");
        ResponseEntity<?> response = authService.login(loginDto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are logged in", response.getBody());
    }

    @Test
    void login_whenValidCredentials_shouldReturnAuthResponse() {
        String email = "test@example.com";
        String password = "password";
        String token = "mocked-jwt-token";

        LoginRequestDto loginDto = new LoginRequestDto(email, password);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setName("Test User");
        user.setPhoneNumber("123456789");

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication)).thenReturn(token);

        when(accessTokenRepository.findByUser_Email(email)).thenReturn(List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authService.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        AuthResponseDto responseDto = (AuthResponseDto) response.getBody();
        assertNotNull(responseDto);
        assertEquals(token, responseDto.accessToken());
        assertEquals(email, responseDto.email());
        assertEquals(user.getName(), responseDto.username());
        assertEquals(user.getPhoneNumber(), responseDto.phoneNumber());
    }

    @Test
    void login_whenAuthenticationFails_shouldReturnBadRequest() {
        when(securityContext.getAuthentication()).thenReturn(null);
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Bad credentials"));

        LoginRequestDto loginDto = new LoginRequestDto("invalid@example.com", "wrongpass");

        ResponseEntity<?> response = authService.login(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertEquals("Invalid credentials or login error", error.getMessage());
    }



    @Test
    void logout_whenValidToken_shouldRevokeTokens() {
        String token = "Bearer mocked-jwt";
        String jwt = "mocked-jwt";
        String email = "user@example.com";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(token);

        when(jwtService.getEmailFromToken(jwt)).thenReturn(email);

        AccessToken accessToken1 = new AccessToken();
        accessToken1.setToken(jwt);
        accessToken1.setRevoked(false);

        AccessToken accessToken2 = new AccessToken();
        accessToken2.setToken("another-token");
        accessToken2.setRevoked(false);

        List<AccessToken> tokens = List.of(accessToken1, accessToken2);
        when(accessTokenRepository.findByUser_Email(email)).thenReturn(tokens);

        ResponseEntity<?> response = authService.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully !!", response.getBody());

        assertTrue(tokens.stream().allMatch(AccessToken::isRevoked));
        verify(accessTokenRepository).saveAll(tokens);
    }

    @Test
    void logout_whenNoAuthHeader_shouldReturnOk() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<?> response = authService.logout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully !!", response.getBody());
    }



    @Test
    void logout_whenErrorOccurs_shouldReturnInternalServerError() {
        String token = "Bearer mocked-jwt";
        String jwt = "mocked-jwt";
        String email = "user@example.com";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(token);

        when(jwtService.getEmailFromToken(jwt)).thenReturn(email);

        when(accessTokenRepository.findByUser_Email(email)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = authService.logout(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Logout failed !!", errorResponse.getMessage());
    }



    @Test
    void testRegister_whenAuthenticated_returnsForbidden() {
        // Create mock of a non-anonymous token (e.g., UsernamePasswordAuthenticationToken)
        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);

        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(context);

        RegisterDto dto = new RegisterDto("John", "john@example.com", "123456", "123456", "0897665234",null);
        ResponseEntity<?> response = authService.register(dto);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are logged in", response.getBody());
    }

    @Test
    void testRegister_withDuplicateEmail_returnsBadRequest() {
        SecurityContextHolder.clearContext(); // simulate unauthenticated

        RegisterDto dto = new RegisterDto("John", "john@example.com", "123456", "123456", "0897665234", null);

        User existingUser = new User();
        existingUser.setName("John");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("123456");

        when(userRepository.findAll()).thenReturn(List.of(existingUser));

        ResponseEntity<?> response = authService.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assert error != null; // error.getMassage() may produce a null pointer exception
        assertEquals("Email is already taken!", error.getMessage());
    }

    @Test
    void testRegister_withInvalidCity_returnsBadRequest() {
        SecurityContextHolder.clearContext(); // simulate unauthenticated

        RegisterDto dto = new RegisterDto("John", "john@example.com", "123456", "123456", "0897665234", 99L);

        when(userRepository.findAll()).thenReturn(List.of());
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = authService.register(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assert error != null;
        assertEquals("City does not exist!", error.getMessage());
    }

    @Test
    void testRegister_successWithoutCity_returnsCreated() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        SecurityContextHolder.clearContext(); // simulate unauthenticated

        RegisterDto dto = new RegisterDto("John", "john@example.com", "123456", "123456", "0897665234", null);

        when(userRepository.findAll()).thenReturn(List.of());
        when(jwtService.generateToken(any())).thenReturn("fake-token");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        ResponseEntity<?> response = authService.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        AuthResponseDto body = (AuthResponseDto) response.getBody();
        assertNotNull(body);
        assertEquals("john@example.com", body.email());
        assertEquals("fake-token", body.accessToken());
    }

    @Test
    void testRegister_successWithCity_returnsCreated() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        SecurityContextHolder.clearContext(); // simulate unauthenticated

        RegisterDto dto = new RegisterDto("Anna", "anna@example.com", "pass123", "pass123", "0888888888", 5L);
        City city = new City();
        city.setId(5L);
        city.setName("Sofia");

        when(userRepository.findAll()).thenReturn(List.of());
        when(cityRepository.findById(5L)).thenReturn(Optional.of(city));
        when(jwtService.generateToken(any())).thenReturn("city-token");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(2L);
            return u;
        });

        ResponseEntity<?> response = authService.register(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        AuthResponseDto body = (AuthResponseDto) response.getBody();
        assertNotNull(body);
        assertEquals("anna@example.com", body.email());
        assertEquals("city-token", body.accessToken());
    }

}


