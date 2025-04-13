package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.dto.request.LoginRequest;
import com.donesk.moneytracker.dto.request.RegisterRequest;
import com.donesk.moneytracker.jwt.JwtUtils;
import com.donesk.moneytracker.model.ERole;
import com.donesk.moneytracker.model.Role;
import com.donesk.moneytracker.repository.RoleRepo;
import com.donesk.moneytracker.repository.UserRepo;
import com.donesk.moneytracker.service.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private RoleRepo roleRepo;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticateUser_ShouldReturnJwtResponse() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setUsername("john");
        login.setPassword("pass");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "john", "john@email.com", "encoded", List.of(() -> "ROLE_USER"));

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtils.generateJwtToken(auth)).thenReturn("mocked-jwt");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mocked-jwt"))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@email.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void registerUser_ShouldCreateUserSuccessfully() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("john");
        register.setEmail("john@email.com");
        register.setPassword("123");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        when(userRepo.existsByUsername("john")).thenReturn(false);
        when(userRepo.existsByEmail("john@email.com")).thenReturn(false);
        when(roleRepo.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(encoder.encode("123")).thenReturn("encoded-password");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void registerUser_ShouldFail_WhenUsernameExists() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("john");
        register.setEmail("john@email.com");
        register.setPassword("123");

        when(userRepo.existsByUsername("john")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));
    }

    @Test
    void registerUser_ShouldFail_WhenEmailExists() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("john");
        register.setEmail("john@email.com");
        register.setPassword("123");

        when(userRepo.existsByUsername("john")).thenReturn(false);
        when(userRepo.existsByEmail("john@email.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Email is already in use!"));
    }
}

