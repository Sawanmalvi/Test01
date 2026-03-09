package com.ums_crm.userManagement.service;

import com.ums_crm.userManagement.dto.LoginResponse;
import com.ums_crm.userManagement.entity.User;
import com.ums_crm.userManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)   // ✅ VERY IMPORTANT
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    //@InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        // Manually create service
        userService = new UserService(userRepository, passwordEncoder);

        // Set JWT secret manually
        ReflectionTestUtils.setField(
                userService,
                "secretKey",
                "mysecretkeymysecretkeymysecretkey12"
        );
    }

    @Test
    void testRegisterUser_Success() {

        // Arrange (Dynamic values)
        String username = "user_" + System.currentTimeMillis();
        String rawPassword = "pass_" + System.nanoTime();
        String encodedPassword = "encoded_" + rawPassword;

        User user = new User();
        user.setUsername(username);
        user.setPassword(rawPassword);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(encodedPassword);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.registerUser(user);

        // Assert
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals("ROLE_USER", result.getRole());

        verify(userRepository).save(any(User.class));
    }
    @Test
    void testLogin_Success() {

        // Arrange
        String username = "user_" + System.currentTimeMillis();
        String rawPassword = "pass_" + System.nanoTime();
        String encodedPassword = "encoded_" + rawPassword;

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);

        // Act
        LoginResponse response = userService.login(username, rawPassword);

        // Assert
        assertNotNull(response.getToken());
        assertEquals(username, response.getUserName());
        assertEquals(1L, response.getUserId());
    }
}
