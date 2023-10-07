package com.donesk.moneytracker.service;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.entity.Category;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.repository.BudgetRepo;
import com.donesk.moneytracker.repository.CategoryRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;
    private final BudgetService budgetService;
    private final BudgetRepo budgetRepo;


    public TransactionService(TransactionRepo transactionRepo, CategoryRepo categoryRepo, BudgetService budgetService, BudgetRepo budgetRepo) {
        this.transactionRepo = transactionRepo;
        this.categoryRepo = categoryRepo;
        this.budgetService = budgetService;
        this.budgetRepo = budgetRepo;
    }

    public List<Transaction> getTransactions(){
        return transactionRepo.findAll();
    }

    public Transaction getTransaction(Long id){
        return transactionRepo.findById(id).orElseThrow( () -> new TransactionNotFoundException("Transaction with id "+ id + " wasn't found"));
    }

    @Transactional
    public Optional<Transaction> add(Long budgetId, Transaction transaction){
        transaction.setDate(LocalDate.now());
        Category category = categoryRepo.findById(transaction.getCategory().getId()).get();
        transaction.setCategory(category);
        Budget budget = budgetService.getBudget(budgetId);
        transaction.setBudget(budget);
        budgetService.updateBudget(budgetId, transaction);
        transactionRepo.save(transaction);
        return Optional.of(transaction);
    }

    @Transactional
    public void delete(Long id){
        transactionRepo.deleteById(id);
    }


    public List<Transaction> findTransactionsByBudgetId(Long budgetId){
        return transactionRepo.findTransactionsByBudgetId(budgetId);
    }
}
