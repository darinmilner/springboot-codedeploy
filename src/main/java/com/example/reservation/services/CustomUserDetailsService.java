package com.example.reservation.services;

import com.example.reservation.entities.Users;
import com.example.reservation.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Users user = userRepository.findByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException("User does not exist")
        );
        return User.builder().username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

    }
}
