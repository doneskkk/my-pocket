package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.service.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final BudgetService budgetService;

    public UserController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

}
