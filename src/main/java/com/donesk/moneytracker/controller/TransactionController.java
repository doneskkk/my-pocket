package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.entity.Category;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransaction(){
        List<Transaction> transactionList = transactionService.getTransactions();
        return new ResponseEntity<List<Transaction>>(transactionList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> add(@RequestBody Transaction transaction){
        transactionService.add(transaction);

       return new ResponseEntity<Transaction>(transaction, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Long> delete(@RequestParam Long id){
        transactionService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id){
        Transaction transaction = transactionService.getTransaction(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
