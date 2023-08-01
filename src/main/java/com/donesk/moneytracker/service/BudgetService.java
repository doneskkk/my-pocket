package com.donesk.moneytracker.service;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.repos.BudgetRepo;
import com.donesk.moneytracker.repos.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepo budgetRepo;
    private final TransactionRepo transactionRepo;


    public BudgetService(BudgetRepo budgetRepo, TransactionRepo transactionRepo) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public Long create(Budget budget){
        return budgetRepo.save(budget).getId();
    }

    public Optional<Budget> getBudget(Long id){
        return budgetRepo.findById(id);
    }

    public List<Budget> getBudgets() {
        return budgetRepo.findAll();
    }

    @Transactional
    public Double addTransactionToBudget(Long budget_id, Long transaction_id){
        Optional<Budget> budget =
                budgetRepo.findById(budget_id);

        Optional<Transaction> transactionToAdd =
                transactionRepo.findById(transaction_id);

        budget.get().getTransactionList().add(transactionToAdd.get());
        transactionToAdd.get().setBudget(budget.get());

        double totalTransactionAmount = budget.get().getTransactionList()
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
          Double currentProgress = budget.get().getCurrentProgress();
        budget.get().setCurrentProgress(currentProgress + totalTransactionAmount);
        budgetRepo.save(budget.get());
        transactionRepo.save(transactionToAdd.get());


        return currentProgress;
    }
}
