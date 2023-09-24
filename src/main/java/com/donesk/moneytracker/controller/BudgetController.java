package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets(){
        List<Budget> budgets = budgetService.getBudgets();
        return new ResponseEntity<List<Budget>>(budgets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Budget> add(@RequestBody Budget budget, Authentication authentication){


        budgetService.create(budget, authentication);

        return new ResponseEntity<Budget>(budget, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget(@PathVariable Long id){
        Budget budget = budgetService.getBudget(id);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PatchMapping("/{id}/transactions/{transaction_id}")
    public ResponseEntity<Double> addTransactionToBudget(@PathVariable Long id,
                                                         @PathVariable("transaction_id") Long transaction_id){

        return new ResponseEntity<Double>(budgetService.addTransactionToBudget(id, transaction_id), HttpStatus.OK);

    }
}
