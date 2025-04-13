package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.dto.response.BudgetCreateDTO;
import com.donesk.moneytracker.dto.response.BudgetDTO;
import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.User;
import com.donesk.moneytracker.service.BudgetService;
import com.donesk.moneytracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
@AutoConfigureMockMvc
public class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetService budgetService;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                    "user",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBudgets_ShouldReturnListOfBudgets() throws Exception {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setTitle("Test");

        BudgetDTO dto = new BudgetDTO();
        dto.setId(1L);
        dto.setTitle("Test");

        Mockito.when(budgetService.getBudgetsByCreatedBy("user")).thenReturn(List.of(budget));
        Mockito.when(modelMapper.map(any(Budget.class), eq(BudgetDTO.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test"));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void addBudget_ShouldReturnCreatedStatus() throws Exception {
        BudgetCreateDTO request = new BudgetCreateDTO();
        request.setTitle("Test Budget");

        Budget budget = new Budget();
        budget.setTitle("Test Budget");
        budget.setCurrentProgress(0.0);

        Mockito.when(modelMapper.map(any(BudgetCreateDTO.class), eq(Budget.class))).thenReturn(budget);
        Mockito.when(budgetService.create(any(Budget.class), any())).thenReturn(1L);

        mockMvc.perform(post("/api/v1/budgets")
                        .with(csrf())  // добавляем CSRF-токен
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBudget_ShouldReturnBudget_WhenUserIsOwner() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setUser(user);
        budget.setTitle("Test");

        BudgetDTO dto = new BudgetDTO();
        dto.setId(10L);
        dto.setTitle("Test");

        Mockito.when(userService.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(budgetService.getBudget(10L)).thenReturn(budget);
        Mockito.when(modelMapper.map(budget, BudgetDTO.class)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/budgets/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.title").value("Test"));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBudget_ShouldReturnUnauthorized_WhenUserIsNotOwner() throws Exception {
        User actualUser = new User();
        actualUser.setId(1L);
        actualUser.setUsername("user");

        User anotherUser = new User();
        anotherUser.setId(99L);
        anotherUser.setUsername("other");

        Budget budget = new Budget();
        budget.setId(10L);
        budget.setUser(anotherUser); // Budget is owned by "other"

        Mockito.when(userService.findByUsername("user")).thenReturn(Optional.of(actualUser));
        Mockito.when(budgetService.getBudget(10L)).thenReturn(budget);

        mockMvc.perform(get("/api/v1/budgets/10"))
                .andExpect(status().isUnauthorized());
    }

}
