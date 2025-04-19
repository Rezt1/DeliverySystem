package com.renascence.backend.service;

import com.renascence.backend.entities.User;
import com.renascence.backend.repositories.UserRepository;
import com.renascence.backend.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_userExists_returnsCustomUserDetails() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("securePassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_userDoesNotExist_throwsException() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email)
        );

        assertEquals("Email not found!", thrown.getMessage());
    }
}
