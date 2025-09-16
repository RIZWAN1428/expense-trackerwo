package com.example.expense_tracker.config;

import com.example.expense_tracker.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // auth endpoints open
                        .requestMatchers("/api/auth/**").permitAll()

                        // read allowed to all logged-in roles
                        .requestMatchers(HttpMethod.GET, "/expenses/**").authenticated()


                        // edit allowed if role has canEdit flag
                        .requestMatchers(HttpMethod.PUT, "/expenses/**").access((authentication, ctx) -> {
                            Object principal = authentication.get().getPrincipal();
                            if (principal instanceof com.example.expense_tracker.model.User user) {
                                return new AuthorizationDecision(user.isCanEdit());
                            }
                            return new AuthorizationDecision(false);
                        })

                        // delete allowed if role has canDelete flag
                        .requestMatchers(HttpMethod.DELETE, "/expenses/**").access((authentication, ctx) -> {
                            Object principal = authentication.get().getPrincipal();
                            if (principal instanceof com.example.expense_tracker.model.User user) {
                                return new AuthorizationDecision(user.isCanDelete());
                            }
                            return new AuthorizationDecision(false);
                        })

                        // admin-only user management
                        .requestMatchers(HttpMethod.PUT, "/api/auth/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/users/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
