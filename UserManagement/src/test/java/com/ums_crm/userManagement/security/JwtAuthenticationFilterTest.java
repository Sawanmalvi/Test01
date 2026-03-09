package com.ums_crm.userManagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums_crm.userManagement.utility.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JwtAuthenticationFilterTest {
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter();

        // Secret manually set
        ReflectionTestUtils.setField(
                jwtAuthenticationFilter,
                "secretKey",
                "mysecretkeymysecretkeymysecretkey12"
        );

        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_WithValidHeaders() throws Exception {

        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        String dynamicUsername = "user_" + System.currentTimeMillis();

        UserDTO userDTO = new UserDTO();
        userDTO.setVuser_name(dynamicUsername);

        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(userDTO);

        request.addHeader("Authorization", "Bearer dummy-token");
        request.addHeader("X-UserDto", userJson);

        // Act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Assert
        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(dynamicUsername,
                ((UserDTO) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getVuser_name());
    }

    @Test
    void testDoFilterInternal_WithoutAuthorizationHeader() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithInvalidUserDtoHeader() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer dummy-token");
        request.addHeader("X-UserDto", "invalid-json");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        // Invalid JSON → authentication should remain null
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
