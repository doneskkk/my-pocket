package com.donesk.moneytracker;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;

import com.donesk.moneytracker.jwt.AuthTokenFilter;
import com.donesk.moneytracker.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.donesk.moneytracker.service.UserDetailsServiceImpl;

class AuthTokenFilterTest {

    private AuthTokenFilter authTokenFilter;
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);

        authTokenFilter = new AuthTokenFilter();

        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsService);

        filterChain = new MockFilterChain();

        SecurityContextHolder.clearContext();
    }

    @Test
    void testNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication должна быть null, когда отсутствует заголовок Authorization");
    }

    @Test
    void testAuthorizationHeaderWithoutBearer() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic sometoken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication не должна устанавливаться, если заголовок не начинается с Bearer");
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalidToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("invalidToken")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication не должна устанавливаться для невалидного токена");
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("testuser");

        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication должна быть установлена для валидного токена");
        assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getName(),
                "Username в аутентификации должен быть testuser");
        assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size(),
                "Должна быть установлена одна роль");
    }

    @Test
    void testExceptionDuringValidation() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer someToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("someToken")).thenThrow(new RuntimeException("Test exception"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication не должна устанавливаться, если происходит исключение при валидации");
    }

    @Test
    void testExceptionDuringLoadUserByUsername() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("testuser");
        when(userDetailsService.loadUserByUsername("testuser")).thenThrow(new RuntimeException("User not found"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication не должна устанавливаться, если происходит исключение при загрузке UserDetails");
    }
}

