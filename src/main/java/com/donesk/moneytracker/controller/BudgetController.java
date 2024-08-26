package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.dto.response.BudgetDTO;
import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Budget Controller")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final ModelMapper modelMapper;
    private final BudgetService budgetService;

    public BudgetController(ModelMapper modelMapper, BudgetService budgetService) {
        this.modelMapper = modelMapper;
        this.budgetService = budgetService;
    }

    @Operation(summary = "Get all budgets", description = "Retrieve a list of budgets created by the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of budgets")
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgets(
            @Parameter(description = "Page number for pagination") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Number of items per page") @RequestParam(value = "size", required = false) Integer size,
            Authentication authentication) {
        List<Budget> budgets = budgetService.getBudgetsByCreatedBy(authentication.getName());
        List<BudgetDTO> budgetsResponse = budgets.stream()
                .map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(budgetsResponse, HttpStatus.OK);
    }

    @Operation(summary = "Add a new budget", description = "Create a new budget for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Budget successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping
    public ResponseEntity<Budget> add(
            @Valid @RequestBody BudgetDTO budgetDTO,
            BindingResult bd,
            Authentication authentication) {
        Budget budget = modelMapper.map(budgetDTO, Budget.class);
        budgetService.create(budget, authentication);
        return new ResponseEntity<>(budget, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a budget by ID", description = "Retrieve a budget by its ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of the budget")
    @ApiResponse(responseCode = "404", description = "Budget not found")
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudget(
            @Parameter(description = "ID of the budget to retrieve") @PathVariable Long id,
            Authentication authentication) {
        Budget budget = budgetService.getBudget(id);
        BudgetDTO budgetResponse = modelMapper.map(budget, BudgetDTO.class);
        return new ResponseEntity<>(budgetResponse, HttpStatus.OK);
    }

}

