package com.renascence.backend.service;

import com.renascence.backend.dtos.authorization.AuthResponseDto;
import com.renascence.backend.dtos.authorization.LoginRequestDto;
import com.renascence.backend.entities.AccessToken;
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
}


