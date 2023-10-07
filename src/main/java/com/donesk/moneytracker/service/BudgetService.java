package com.donesk.moneytracker.service;

import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.entity.User;
import com.donesk.moneytracker.exception.BudgetNotFoundException;
import com.donesk.moneytracker.repository.BudgetRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepo budgetRepo;
    private final TransactionRepo transactionRepo;
    private final UserService userService;


    public BudgetService(BudgetRepo budgetRepo, TransactionRepo transactionRepo, UserService userService) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
        this.userService = userService;
    }

    @Transactional
    public Long create(Budget budget, Principal principal){
        User authenticatedUser = userService.findByUsername(principal.getName()).get();

        budget.setUser(authenticatedUser); // Associate the authenticated user with the budget

        return budgetRepo.save(budget).getId();
    }

    public Budget getBudget(Long id){
        Budget budget = budgetRepo.findById(id).orElseThrow( () -> new BudgetNotFoundException("Budget with id "+ id + " wasn't found"));

        return budget;
    }

    @Transactional
    public void updateBudget(Long budgetId, Transaction transaction){
        Budget budget = budgetRepo.findById(budgetId).orElseThrow( () -> new BudgetNotFoundException("Budget with id "+ budgetId + " wasn't found"));
        double transactionAmount = transaction.getAmount();
        budget.updateCurrentProgress(transactionAmount);
        System.out.println(budget.getCurrentProgress());
        if(isGoalAchieved(budgetId)){
            budget.setStatus(true);
        }
        budgetRepo.save(budget);
    }

    public List<Budget> getBudgets() {
        return budgetRepo.findAll();
    }




    private boolean isGoalAchieved(Long budgetId){
        Budget budgetToCheck = budgetRepo.findById(budgetId).get();
        return budgetToCheck.getGoal() <= budgetToCheck.getCurrentProgress();
    }
}
