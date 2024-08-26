package com.donesk.moneytracker.repository;

import com.donesk.moneytracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    public List<Transaction> findTransactionsByBudgetId(Long budgetId);
}
