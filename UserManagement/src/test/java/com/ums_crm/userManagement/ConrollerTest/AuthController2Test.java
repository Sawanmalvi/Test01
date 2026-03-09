package com.ums_crm.userManagement.ConrollerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums_crm.userManagement.controller.AuthController2;
import com.ums_crm.userManagement.dto.LoginResponse;
import com.ums_crm.userManagement.dto.RegisterRequest;
import com.ums_crm.userManagement.entity.User;
import com.ums_crm.userManagement.service.AuthServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AuthController2.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthController2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceInterface authService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= REGISTER TEST =================

    @Test
    void testRegisterUser_Success() throws Exception {

        String username = "dynamicUser";
        String password = "dynamicPass";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setPassword(password);

        Mockito.when(authService.registerUser(Mockito.any(User.class)))
                .thenReturn(savedUser);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())   // 👈 THIS IS IMPORTANT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"dynamicUser\",\"password\":\"dynamicPass\"}"))
                .andExpect(status().isOk());
    }
    @Test
    void testLogin_Success() throws Exception {

        String username = "testUser";
        String password = "testPass";
        String token = "mock-jwt-token";

        LoginResponse mockResponse = new LoginResponse(token);

        Mockito.when(authService.login(username, password))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "username": "%s",
                              "password": "%s"
                            }
                            """.formatted(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }
}
