package com.donesk.moneytracker.service;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.entity.User;
import com.donesk.moneytracker.exception.BudgetNotFoundException;
import com.donesk.moneytracker.repository.BudgetRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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
    public Long create(Budget budget, Authentication authentication){

        User authenticatedUser = (User) authentication.getPrincipal();

        budget.setUser(authenticatedUser); // Associate the authenticated user with the budget

        return budgetRepo.save(budget).getId();
    }

    public Budget getBudget(Long id){
        return budgetRepo.findById(id).orElseThrow( () -> new BudgetNotFoundException("Budget with id "+ id + " wasn't found"));
    }

    public List<Budget> getBudgets() {
        return budgetRepo.findAll();
    }

    @Transactional
    public Double addTransactionToBudget(Long budgetId, Long transactionId) {
        Optional<Budget> budget = budgetRepo.findById(budgetId);

        Optional<Transaction> transactionToAdd = transactionRepo.findById(transactionId);

        Double currentProgress = 0.0;

        budget.get().getTransactionList().add(transactionToAdd.get());
        transactionToAdd.get().setBudget(budget.get());

        double totalTransactionAmount = budget.get().getTransactionList()
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

         currentProgress = budget.get().getCurrentProgress();
         budget.get().setCurrentProgress(currentProgress + totalTransactionAmount);

         if(isGoalAchieved(budgetId)){
                budget.get().setStatus(true);
         }

        budgetRepo.save(budget.get());
        transactionRepo.save(transactionToAdd.get());

        return currentProgress;
    }

    private boolean isGoalAchieved(Long budgetId){
        Budget budgetToCheck = budgetRepo.findById(budgetId).get();
        return budgetToCheck.getGoal() <= budgetToCheck.getCurrentProgress();
    }
}
