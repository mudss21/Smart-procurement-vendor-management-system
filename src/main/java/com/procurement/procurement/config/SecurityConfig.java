package com.procurement.procurement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF (demo project)
                .csrf(csrf -> csrf.disable())

                // Session based authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth

                        // Public
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**")
                        .permitAll()

                        // Dashboard
                        .requestMatchers("/dashboard")
                        .authenticated()

                        // Vendor
                        .requestMatchers("/vendor/**")
                        .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                        // Requisition
                        .requestMatchers("/requisition/**")
                        .hasAnyRole("ADMIN", "EMPLOYEE", "PROCUREMENT_MANAGER")

                        // Approval
                        .requestMatchers("/approval/**")
                        .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                        // Purchase Order
                        .requestMatchers("/purchase-order/**")
                        .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                        // ✅ REPORT API (IMPORTANT)
                        .requestMatchers("/api/reports/**")
                        .hasAnyRole("ADMIN", "PROCUREMENT_MANAGER")

                        // Everything else
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )

                .userDetailsService(userDetailsService);

        return http.build();
    }

    // Required for AuthenticationManager injection
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}