package com.renascence.backend.services;

import com.renascence.backend.dtos.Authorization.LoginRequestDto;
import com.renascence.backend.dtos.Authorization.LoginResponseDto;
import com.renascence.backend.dtos.Authorization.RefreshTokenRequestDto;
import com.renascence.backend.entities.CustomUserDetails;
import com.renascence.backend.entities.RefreshToken;
import com.renascence.backend.entities.User;
import com.renascence.backend.exceptionHandlers.ErrorResponse;
import com.renascence.backend.repositories.RefreshTokenRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(authentication);

            String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());
            RefreshToken refreshTokenEntity = new RefreshToken(
                    LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationInMs() / 1000), //might cause trouble
                    refreshToken,
                    userRepository.findByEmail(userDetails.getUsername()).get()
            );

            refreshTokenRepository.save(refreshTokenEntity);

            return ResponseEntity.ok(new LoginResponseDto(jwt, refreshToken));

        } catch (Exception ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Invalid credentials or login error", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            String refreshToken = refreshTokenRequestDto.refreshToken();
            Optional<RefreshToken> refreshTokenFromDb = refreshTokenRepository.findByToken(refreshToken);

            if(refreshTokenFromDb.isEmpty() || refreshTokenFromDb.get().getExpiryDate().isBefore(LocalDateTime.now())
                    || refreshTokenFromDb.get().isRevoked()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("Invalid refresh token", LocalDateTime.now()));
            }

            RefreshToken validRefreshToken = refreshTokenFromDb.get();
            User user = validRefreshToken.getUser();
            CustomUserDetails userDetails = new CustomUserDetails(user);

            String newJwt = jwtService
                    .generateToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            String newRefreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

            validRefreshToken.setToken(newRefreshToken);
            validRefreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(jwtService.getRefreshExpirationInMs() / 1000));

            refreshTokenRepository.save(validRefreshToken);

            return ResponseEntity.ok(new LoginResponseDto(newJwt, newRefreshToken));
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid refresh token", LocalDateTime.now()));
        }
    }

    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        try {
            String authHeader = httpServletRequest.getHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String email = jwtService.getEmailFromToken(jwt);

                List<RefreshToken> refreshTokens = refreshTokenRepository.findByUser_Email(email);
                for (RefreshToken token : refreshTokens) {
                    token.setRevoked(true);
                }
                refreshTokenRepository.saveAll(refreshTokens);
            }

            return ResponseEntity.ok("Logged out successfully !!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Logout failed !!", LocalDateTime.now()));
        }
    }
}
