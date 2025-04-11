package com.donesk.moneytracker.service;

import com.donesk.moneytracker.exception.BudgetNotFoundException;
import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.User;
import com.donesk.moneytracker.repository.BudgetRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import com.donesk.moneytracker.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock private BudgetRepo budgetRepo;
    @Mock private TransactionRepo transactionRepo;
    @Mock private UserRepo userRepo;

    @InjectMocks private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ShouldSaveBudgetWithUser() {
        // arrange
        Principal principal = () -> "testUser";
        User user = new User();
        user.setUsername("testUser");
        user.setId(1L);
        Budget budget = new Budget();
        budget.setGoal(100.0);

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(budgetRepo.save(any(Budget.class))).thenAnswer(inv -> {
            Budget b = inv.getArgument(0);
            b.setId(99L);
            return b;
        });

        // act
        Long id = budgetService.create(budget, principal);

        // assert
        assertEquals(99L, id);
        assertEquals(user, budget.getUser());
        verify(budgetRepo).save(budget);
    }

    @Test
    void getBudget_ShouldReturn_WhenExists() {
        Budget budget = new Budget();
        budget.setId(1L);

        when(budgetRepo.findById(1L)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudget(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getBudget_ShouldThrow_WhenNotFound() {
        when(budgetRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () ->
                budgetService.getBudget(2L)
        );
    }

    @Test
    void updateStatus_ShouldSetTrue_WhenGoalAchieved() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setGoal(100.0);
        budget.setCurrentProgress(120.0);

        when(budgetRepo.findById(1L)).thenReturn(Optional.of(budget));

        budgetService.updateStatus(budget);

        assertTrue(budget.getStatus());
        verify(budgetRepo).save(budget);
    }

    @Test
    void updateStatus_ShouldNotSetStatus_WhenGoalNotAchieved() {
        Budget budget = new Budget();
        budget.setId(2L);
        budget.setGoal(500.0);
        budget.setCurrentProgress(100.0);

        when(budgetRepo.findById(2L)).thenReturn(Optional.of(budget));

        budgetService.updateStatus(budget);

        assertFalse(budget.getStatus());
        verify(budgetRepo).save(budget);
    }

    @Test
    void getBudgets_ShouldReturnAll() {
        List<Budget> budgets = List.of(new Budget(), new Budget());
        when(budgetRepo.findAll()).thenReturn(budgets);

        List<Budget> result = budgetService.getBudgets();

        assertEquals(2, result.size());
    }

    @Test
    void getBudgetsByCreatedBy_ShouldReturnBudgets() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        List<Budget> budgets = List.of(new Budget(), new Budget());

        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(budgetRepo.findByUserId(1L)).thenReturn(budgets);

        List<Budget> result = budgetService.getBudgetsByCreatedBy("testUser");

        assertEquals(2, result.size());
    }
}
