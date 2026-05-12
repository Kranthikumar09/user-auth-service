package com.authservice.user_auth_service.service;



import com.authservice.user_auth_service.dto.AuthResponse;
import com.authservice.user_auth_service.dto.LoginRequest;
import com.authservice.user_auth_service.dto.RegisterRequest;
import com.authservice.user_auth_service.exception.InvalidCredentialsException;
import com.authservice.user_auth_service.exception.TooManyRequestsException;
import com.authservice.user_auth_service.exception.UserAlreadyExistsException;
import com.authservice.user_auth_service.model.User;
import com.authservice.user_auth_service.repository.UserRepository;
import com.authservice.user_auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final RateLimitService rateLimitService;

    public AuthResponse register (RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .enabled(true)
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token, user.getUsername(), user.getRole());

    }

    public AuthResponse login(LoginRequest request, String ipAddress) {
        if( rateLimitService.isRateLimited(ipAddress)){
            throw new TooManyRequestsException("Too many login attempts. Please try again in 1 minute");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            rateLimitService.recordFailedAttempt(ipAddress);
            throw new InvalidCredentialsException("Invalid username or password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        rateLimitService.clearAttempts(ipAddress);
        return new AuthResponse(token, user.getUsername(), user.getRole());
    }

    public String logout(String token) {
        tokenBlacklistService.blacklistToken(token);
        return "Logged out successfully";
    }



}
