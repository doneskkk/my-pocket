package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transaction Controller")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/budgets/{budgetId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get all transactions for a budget", description = "Retrieve a list of transactions associated with a specific budget")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(
            @Parameter(description = "ID of the budget to retrieve transactions for") @PathVariable("budgetId") Long id) {
        List<Transaction> transactionList = transactionService.findTransactionsByBudgetId(id);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @Operation(summary = "Add a new transaction", description = "Add a new transaction to a specific budget")
    @ApiResponse(responseCode = "200", description = "Transaction successfully added")
    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid transaction data")
    @PostMapping
    public ResponseEntity<Transaction> addTransaction(
            @Parameter(description = "ID of the budget to which the transaction will be added") @PathVariable Long budgetId,
            @Parameter(description = "Transaction details to be added") @RequestBody @Valid Transaction transaction) {
        Transaction transactionToAdd = transactionService.add(budgetId, transaction);
        return new ResponseEntity<>(transactionToAdd, HttpStatus.OK);
    }

    @Operation(summary = "Delete a transaction", description = "Delete a specific transaction from a budget")
    @ApiResponse(responseCode = "200", description = "Transaction successfully deleted")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the transaction to be deleted") @PathVariable Long transactionId,
            @Parameter(description = "ID of the budget containing the transaction") @PathVariable String budgetId) {
        transactionService.delete(transactionId);
        return new ResponseEntity<>("Transaction with id " + transactionId + "was deleted", HttpStatus.OK);
    }

    @Operation(summary = "Get a specific transaction", description = "Retrieve details of a specific transaction by ID")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of the transaction")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(
            @Parameter(description = "ID of the transaction to retrieve") @PathVariable Long id,
            @Parameter(description = "ID of the budget containing the transaction") @PathVariable String budgetId) {
        Transaction transaction = transactionService.getTransaction(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
