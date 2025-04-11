package com.donesk.moneytracker.service;

import com.donesk.moneytracker.exception.UserNotFoundException;
import com.donesk.moneytracker.model.ERole;
import com.donesk.moneytracker.model.User;
import com.donesk.moneytracker.model.Role;
import com.donesk.moneytracker.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock private UserRepo userRepo;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenFound() {
        User user = new User();
        user.setUsername("john");
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotFound() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername("ghost");

        assertTrue(result.isEmpty());
    }

    @Test
    void getUser_ShouldReturnUser_WhenFound() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUser(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getUser_ShouldThrow_WhenNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUser(1L)
        );
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("secret");

        Role role = new Role();
        role.setName(ERole.valueOf("ROLE_ADMIN"));
        user.setRoles(Set.of(role));

        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertEquals("secret", details.getPassword());
    }

    @Test
    void loadUserByUsername_ShouldThrow_WhenUserNotFound() {
        when(userRepo.findByUsername("nope")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("nope")
        );
    }
}
