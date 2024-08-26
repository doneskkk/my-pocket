package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/budgets/{budgetId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("budgetId") Long id){
        List<Transaction> transactionList = transactionService.findTransactionsByBudgetId(id);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@PathVariable Long budgetId, @RequestBody @Valid Transaction transaction){
       Transaction transactionToAdd =  transactionService.add(budgetId, transaction);
       return new ResponseEntity<>(transactionToAdd, HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<Long> delete(@RequestParam Long id, @PathVariable String budgetId){
        transactionService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id, @PathVariable String budgetId){
        Transaction transaction = transactionService.getTransaction(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
