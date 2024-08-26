package com.donesk.moneytracker.service;

import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.repository.CategoryRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;
    private final BudgetService budgetService;

    public TransactionService(TransactionRepo transactionRepo, CategoryRepo categoryRepo, BudgetService budgetService) {
        this.transactionRepo = transactionRepo;
        this.categoryRepo = categoryRepo;
        this.budgetService = budgetService;
    }


    public List<Transaction> getTransactions(){
        return transactionRepo.findAll();
    }

    public Transaction getTransaction(Long id){
        return transactionRepo.findById(id).orElseThrow( () -> new TransactionNotFoundException("Transaction with id "+ id + " wasn't found"));
    }

    @Transactional
    public Transaction add(Long budgetId, Transaction transaction) {
        log.info("Processing transaction: {} for budgetId: {}", transaction, budgetId);

        transaction.setDate(LocalDate.now());

        Category category = categoryRepo.findById(transaction.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        transaction.setCategory(category);

        Budget budget = budgetService.getBudget(budgetId);
        transaction.setBudget(budget);

        log.info("Before updating budget: {}", budget);
        log.info("Transaction details before saving: {}", transaction);

        budget.updateCurrentProgress(transaction.getAmount(), transaction.getCategory());
        budgetService.updateStatus(budget);

        Transaction savedTransaction = transactionRepo.save(transaction);

        log.info("Transaction saved successfully: {}", savedTransaction);
        return savedTransaction;
    }



    @Transactional
    public void delete(Long id){
        transactionRepo.deleteById(id);
    }


    public List<Transaction> findTransactionsByBudgetId(Long budgetId){
        return transactionRepo.findByBudgetId(budgetId);
    }
}
