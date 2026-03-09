package com.ums_crm.userManagement.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {

        jwtTokenProvider = new JwtTokenProvider();

        // 🔐 VERY IMPORTANT: secret set karo
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "myverystrongsecretkeymyverystrongsecretkeymyverystrongsecretkey1234567890123456");
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 3600000);

        // init AFTER setting fields
        jwtTokenProvider.init();
    }

    @Test
    void testGenerateToken_Success() {

        String username = "dynamicUser_" + System.currentTimeMillis();

        String token = jwtTokenProvider.generateToken(username);

        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromJWT_Success() {

        String username = "dynamicUser_" + System.currentTimeMillis();

        String token = jwtTokenProvider.generateToken(username);

        String extractedUsername = jwtTokenProvider.getUsernameFromJWT(token);

        Assertions.assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        // Strong secret for HS512 (min 64 chars recommended)
        String secret = "myverystrongsecretkeymyverystrongsecretkeymyverystrongsecretkey1234567890123456";
        int expiration = 3600000;

        // Set secret & expiration BEFORE generating token
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", secret);
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", expiration);

        // Generate token
        String token = jwtTokenProvider.generateToken("testuser");
        System.out.println("Generated Token: " + token);

        // Validate token
        boolean isValid = jwtTokenProvider.validateToken(token);
        System.out.println("Is Valid: " + isValid);

        Assertions.assertTrue(isValid);
    }
    @Test
    void testValidateToken_InvalidToken() {

        String invalidToken = "invalid.token.value";

        Assertions.assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}
