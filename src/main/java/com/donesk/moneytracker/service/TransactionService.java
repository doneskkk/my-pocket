package com.donesk.moneytracker.service;

import com.donesk.moneytracker.entity.Category;
import com.donesk.moneytracker.entity.Transaction;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.repos.CategoryRepo;
import com.donesk.moneytracker.repos.TransactionRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final CategoryRepo categoryRepo;
    

    public TransactionService(TransactionRepo transactionRepo, CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Transaction> getTransactions(){
        return transactionRepo.findAll();
    }

    public Transaction getTransaction(Long id){
        return transactionRepo.findById(id).orElseThrow( () -> new TransactionNotFoundException("Transaction with id "+ id + " doesn't exist"));
    }

    @Transactional
    public Optional<Transaction> add(Transaction transaction){
        transaction.setDate(LocalDate.now());
        Category category = categoryRepo.findById(transaction.getCategory().getId()).get();
        transaction.setCategory(category);
        transactionRepo.save(transaction);
        return Optional.of(transaction);
    }

    @Transactional
    public void delete(Long id){
        transactionRepo.deleteById(id);
    }

}
