package com.ums_crm.userManagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums_crm.userManagement.utility.SuccessDTO;
import com.ums_crm.userManagement.utility.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;  // (1) application.yml से secret key
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // (2) Header से token लिया

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // (3) token नहीं मिला → request आगे बढ़ा दी गई
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("token===>"+token);
        // (4) "Bearer " हटाकर actual token निकाला

        try {

//            String usernameFromHeader = request.getHeader("X-Username");
            String userJson = request.getHeader("X-UserDto"); // X-User me poora DTO JSON bheja hai
            UserDTO userDTO = null;
            if (userJson != null && !userJson.isEmpty()) {
                userDTO = objectMapper.readValue(userJson, UserDTO.class);
            }
            System.out.println("==userDTO==>"+userDTO);

            // (6) Payload से username निकाला
//            System.out.println("username==>"+username);
//            System.out.println("usernameFromHeader==>"+usernameFromHeader);
            // (7) Spring Security context में authentication set किया

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDTO, null, null);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // (8) Token invalid → आगे response दे दिया जाता है
            System.out.println("Invalid Token");
        }

        filterChain.doFilter(request, response);
        // (9) Request controller तक भेजा
    }
}
