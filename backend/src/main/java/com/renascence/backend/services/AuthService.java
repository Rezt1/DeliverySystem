package com.renascence.backend.services;

import com.renascence.backend.dtos.Authorization.LoginRequestDto;
import com.renascence.backend.dtos.Authorization.LoginResponseDto;
import com.renascence.backend.entities.AccessToken;
import com.renascence.backend.entities.CustomUserDetails;
import com.renascence.backend.exceptionHandlers.ErrorResponse;
import com.renascence.backend.repositories.AccessTokenRepository;
import com.renascence.backend.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(authentication);

            List<AccessToken> accessTokens = accessTokenRepository.findByUser_Email(userDetails.getUsername());

            if (!accessTokens.isEmpty()) {
                for (AccessToken at : accessTokens){
                    at.setToken(accessToken);
                    at.setExpiryDate(LocalDateTime.now().plusSeconds(jwtService.getExpirationInMs() / 1000));
                    at.setRevoked(false);
                }
                accessTokenRepository.saveAll(accessTokens);
            } else {
                AccessToken accessTokenEntity = new AccessToken(
                        LocalDateTime.now().plusSeconds(jwtService.getExpirationInMs() / 1000),
                        accessToken,
                        userRepository.findByEmail(userDetails.getUsername()).get()
                );

                accessTokenRepository.save(accessTokenEntity);
            }

            return ResponseEntity.ok(new LoginResponseDto(accessToken));

        } catch (Exception ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid credentials or login error", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        try {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String email = jwtService.getEmailFromToken(jwt);

                List<AccessToken> accessTokens = accessTokenRepository.findByUser_Email(email);
                for (AccessToken token : accessTokens) {
                    token.setRevoked(true);
                }
                accessTokenRepository.saveAll(accessTokens);
            }

            return ResponseEntity.ok("Logged out successfully !!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Logout failed !!", LocalDateTime.now()));
        }
    }
}
