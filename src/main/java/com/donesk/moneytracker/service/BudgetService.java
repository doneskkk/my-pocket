package com.donesk.moneytracker.service;

import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.User;
import com.donesk.moneytracker.exception.BudgetNotFoundException;
import com.donesk.moneytracker.repository.BudgetRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import com.donesk.moneytracker.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepo budgetRepo;
    private final TransactionRepo transactionRepo;
    private final UserRepo userRepo;


    public BudgetService(BudgetRepo budgetRepo, TransactionRepo transactionRepo, UserService userService, UserRepo userRepo) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public Long create(Budget budget, Principal principal){
        User authenticatedUser = userRepo.findByUsername(principal.getName()).get();
        budget.setUser(authenticatedUser); // Associate the authenticated user with the budget
        return budgetRepo.save(budget).getId();
    }

    public Budget getBudget(Long id){
        Budget budget = budgetRepo.findById(id).orElseThrow( () -> new BudgetNotFoundException("Budget with id "+ id + " wasn't found"));
        return budget;
    }

    @Transactional
    public void updateStatus(Budget budget){
        if(isGoalAchieved(budget.getId())){
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


    public List<Budget> getBudgetsByCreatedBy(String username) {
        User user = userRepo.findByUsername(username).get();
        List<Budget> budgets = budgetRepo.findByUserId(user.getId());
        return budgets;
    }

}
