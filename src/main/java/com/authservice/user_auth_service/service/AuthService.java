package com.authservice.user_auth_service.service;



import com.authservice.user_auth_service.dto.AuthResponse;
import com.authservice.user_auth_service.dto.RegisterRequest;
import com.authservice.user_auth_service.model.User;
import com.authservice.user_auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register (RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .enabled(true)
                .build();

        userRepository.save(user);
        return new AuthResponse("User registered successfully", user.getUsername(), user.getRole());

    }

}
