package com.example.reservation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.anyRequest().permitAll();
                    //auth.requestMatchers("/api/busRoute/create").hasRole("ADMIN");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/api/bus/**").permitAll()
//                .requestMatchers("/api/busRoute/create").hasRole("ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .csrf().disable()
//                .httpBasic();
//        return http.build();
//    }
}
