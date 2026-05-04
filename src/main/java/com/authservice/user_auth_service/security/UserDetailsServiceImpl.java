package com.authservice.user_auth_service.security;

import com.authservice.user_auth_service.model.User;
import com.authservice.user_auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String Username) throws UsernameNotFoundException {
         User appUser = userRepository.findByUsername(Username)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + Username));

         return org.springframework.security.core.userdetails.User.builder()
                 .username(appUser.getUsername())
                 .password(appUser.getPassword())
                 .roles(appUser.getRole())
                 .build();
    }
}
