package com.donesk.moneytracker.service;

import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.repository.CategoryRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
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
    public Transaction add(Long budgetId, Transaction transaction){
        //Set Date
        transaction.setDate(LocalDate.now());

        //Find category by transaction
        Category category = categoryRepo.findById(transaction.getCategory().getId()).get();

        //Set category
        transaction.setCategory(category);
        Budget budget = budgetService.getBudget(budgetId);
        budget.getTransactionList().add(transaction);
        transaction.setBudget(budget);
        budget.updateCurrentProgress(transaction.getAmount(), transaction.getCategory());
        budgetService.updateStatus(budget);
        return transactionRepo.save(transaction);
    }

    @Transactional
    public void delete(Long id){
        transactionRepo.deleteById(id);
    }


    public List<Transaction> findTransactionsByBudgetId(Long budgetId){
        return transactionRepo.findTransactionsByBudgetId(budgetId);
    }
}
