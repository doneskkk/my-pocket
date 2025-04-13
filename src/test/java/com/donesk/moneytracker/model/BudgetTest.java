package com.donesk.moneytracker.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class BudgetTest {

    @Test
    void testUpdateCurrentProgress_income() {
        Category incomeCategory = new Category();
        incomeCategory.setType(CategoryType.valueOf("INCOME"));

        Budget budget = new Budget("Test Budget", 1000.0, new User());
        assertEquals(0.0, budget.getCurrentProgress());

        double amount = 200.0;
        budget.updateCurrentProgress(amount, incomeCategory);

        assertEquals(200.0, budget.getCurrentProgress());
    }

    @Test
    void testUpdateCurrentProgress_expense() {
        Category expenseCategory = new Category();
        expenseCategory.setType(CategoryType.valueOf("EXPENSE"));

        Budget budget = new Budget("Test Budget", 1000.0, new User());
        budget.setCurrentProgress(300.0);

        double amount = 100.0;
        budget.updateCurrentProgress(amount, expenseCategory);

        assertEquals(200.0, budget.getCurrentProgress());
    }
}

