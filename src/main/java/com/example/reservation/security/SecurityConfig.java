package com.example.reservation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/").permitAll();
//                    auth.anyRequest().permitAll();
//                    //auth.requestMatchers("/api/busRoute/create").hasRole("ADMIN");
//                })
//                .csrf(AbstractHttpConfigurer::disable)
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }
}
