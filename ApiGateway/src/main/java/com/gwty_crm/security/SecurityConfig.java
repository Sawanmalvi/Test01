package com.gwty_crm.security;

import com.gwty_crm.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationFilter authenticationFilter;

    // Public endpoints (login, register, etc.)
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/login",
            "/auth/register"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http
                .cors().and()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll() // Allow OPTIONS
                .pathMatchers(PUBLIC_ENDPOINTS).permitAll()  // Allow public
                .anyExchange().authenticated()               // All others require auth
                .and()
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }
}
