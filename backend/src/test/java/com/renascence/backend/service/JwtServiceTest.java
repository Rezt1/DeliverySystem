package com.renascence.backend.service;

import com.renascence.backend.entities.AccessToken;
import com.renascence.backend.repositories.AccessTokenRepository;
import com.renascence.backend.services.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @InjectMocks
    private JwtService jwtService;

    private final String testSecret = "mySuperSecretKeyThatIsLongEnoughForHS512mySuperSecretKeyThatIsLongEnoughForHS512";
    private final long testExpiration = 86400000; // 1 day
    private final String testEmail = "test@example.com";
    private final Collection<? extends GrantedAuthority> testAuthorities =
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

    private SecretKey testKey;

    @BeforeEach
    void setUp() {
        // Initialize the secret key
        byte[] keyBytes = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(testSecret.getBytes()));
        testKey = Keys.hmacShaKeyFor(keyBytes);

        // Initialize service
        jwtService = new JwtService(accessTokenRepository);
        jwtService.jwtSecret = Base64.getEncoder().encodeToString(testSecret.getBytes());
        jwtService.jwtExpirationInMs = testExpiration;
        jwtService.init();
    }


    @Test
    void generateToken_ShouldCreateValidToken() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(testEmail);

        // This is the key change - explicit casting
        when(auth.getAuthorities()).thenAnswer(invocation -> testAuthorities);

        String token = jwtService.generateToken(auth);

        assertNotNull(token);
        assertEquals(testEmail, jwtService.getEmailFromToken(token));
    }



    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String validToken = createValidToken();
        when(accessTokenRepository.findByToken(validToken))
                .thenReturn(Optional.of(new AccessToken(validToken, false)));

        assertTrue(jwtService.validateToken(validToken));
    }

    @Test
    void validateToken_ShouldReturnFalseForRevokedToken() {
        String validToken = createValidToken();
        when(accessTokenRepository.findByToken(validToken))
                .thenReturn(Optional.of(new AccessToken(validToken, true)));

        assertFalse(jwtService.validateToken(validToken));
    }

    @Test
    void validateToken_ShouldReturnFalseForMissingToken() {
        String validToken = createValidToken();
        when(accessTokenRepository.findByToken(validToken))
                .thenReturn(Optional.empty());

        assertFalse(jwtService.validateToken(validToken));
    }

    @Test
    void validateToken_ShouldHandleExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject(testEmail)
                .issuedAt(new Date(System.currentTimeMillis() - testExpiration - 1000))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(testKey)
                .compact();

        assertFalse(jwtService.validateToken(expiredToken));
    }

    @Test
    void validateToken_ShouldHandleMalformedToken() {
        assertFalse(jwtService.validateToken("malformed.token.here"));
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        String validToken = createValidToken();
        assertEquals(testEmail, jwtService.getEmailFromToken(validToken));
    }

    @Test
    void getExpirationInMs_ShouldReturnCorrectValue() {
        assertEquals(testExpiration, jwtService.getExpirationInMs());
    }

    private String createValidToken() {
        return Jwts.builder()
                .subject(testEmail)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + testExpiration))
                .signWith(testKey)
                .compact();
    }
}