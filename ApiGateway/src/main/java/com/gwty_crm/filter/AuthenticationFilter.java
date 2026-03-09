package com.gwty_crm.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gwty_crm.security.Jwtutil;
import com.gwty_crm.security.SecurityConfig;
import com.gwty_crm.util.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
//import org.springframework.web.server.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    @Qualifier("jwtUtilSecurity")
    private Jwtutil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper; // for JSON conversion

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // Allow CORS preflight requests
        if (CorsUtils.isPreFlightRequest(exchange.getRequest()) ||
                exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().value();
        for (String publicPath : SecurityConfig.PUBLIC_ENDPOINTS) {
            if (path.startsWith(publicPath)) return chain.filter(exchange);
        }

        // Get Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
//        System.out.println("token ===> " + token);

        // Validate JWT
        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String username = jwtUtil.extractUsername(token);
        long userId = Long.parseLong(jwtUtil.extractUserId(token));

        UserDTO userDTO = new UserDTO();
        userDTO.setIuser_id(userId);
        userDTO.setVuser_name(username);
//        System.out.println("userDTO=="+userDTO.getVuser_name());
        String userJson = null;
        try {
            userJson = objectMapper.writeValueAsString(userDTO);
        } catch (Exception e) {
            log.error("Error converting UserDTO to JSON", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

        if (username != null) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, List.of()); // roles empty
//            System.out.println("jwtUtil.extractUserId(token)==>"+jwtUtil.extractUserId(token));
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-UserDto", userJson)
//                    .header("X-Userid", userId+"")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // forward JWT
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }

        // 8️⃣ Fallback
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
