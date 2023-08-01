package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Budget> add(@RequestBody Budget budget){
        budget.setCurrentProgress(0.0);
        budget.setTransactionList(new ArrayList<>());
        budgetService.create(budget);

        return new ResponseEntity<Budget>(budget, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Budget>> getBudget(@PathVariable Long id){
        Optional<Budget> budget = budgetService.getBudget(id);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @PatchMapping("/{id}/transactions/{transaction_id}")
    public ResponseEntity<Double> addTransactionToBudget(@PathVariable Long id,
                                                         @PathVariable("transaction_id") Long transaction_id){
        return new ResponseEntity<Double>(budgetService.addTransactionToBudget(id, transaction_id), HttpStatus.OK);

    }
}
