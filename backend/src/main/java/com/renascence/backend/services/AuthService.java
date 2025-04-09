package com.renascence.backend.services;

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
import com.renascence.backend.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager
                  .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(authentication);

            List<AccessToken> accessTokens = accessTokenRepository.findByUser_Email(userDetails.getUsername());

            User currUser = userRepository.findByEmail(userDetails.getUsername()).get();

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
                        currUser
                );

                accessTokenRepository.save(accessTokenEntity);
            }

            return ResponseEntity.ok(new AuthResponseDto(accessToken, currUser.getEmail(), currUser.getName(), currUser.getPhoneNumber()));

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

    public ResponseEntity<?> register(RegisterDto registerDto) {
        List<User> users = userRepository.findAll();

        for (User user : users){
            if (user.getEmail().equalsIgnoreCase(registerDto.getEmail())){
                return ResponseEntity.badRequest().body(new ErrorResponse("Email is already taken!", LocalDateTime.now()));
            }
        }

        Optional<City> city = cityRepository.findById(registerDto.getLocationId());

        if (city.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorResponse("City does not exist!", LocalDateTime.now()));
        }

        User newUser = new User();
        newUser.setName(registerDto.getName());
        newUser.setEmail(registerDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        newUser.setPhoneNumber(registerDto.getPhoneNumber());
        newUser.setLocation(city.get());

        userRepository.save(newUser);

        String jwt = jwtService.generateToken(new UsernamePasswordAuthenticationToken(newUser.getEmail(), registerDto.getPassword()));

        AccessToken accessToken = new AccessToken(
                LocalDateTime.now().plusSeconds(jwtService.getExpirationInMs() / 1000),
                jwt,
                newUser
        );

        accessTokenRepository.save(accessToken);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(new AuthResponseDto(jwt, newUser.getEmail(), newUser.getName(), newUser.getPhoneNumber()));
    }
}
