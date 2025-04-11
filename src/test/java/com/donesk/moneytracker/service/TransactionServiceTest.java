package com.donesk.moneytracker.service;

import com.donesk.moneytracker.exception.CategoryNotFoundException;
import com.donesk.moneytracker.exception.TransactionNotFoundException;
import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.repository.CategoryRepo;
import com.donesk.moneytracker.repository.TransactionRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactions_ShouldReturnList() {
        List<Transaction> list = List.of(new Transaction(), new Transaction());
        when(transactionRepo.findAll()).thenReturn(list);

        List<Transaction> result = transactionService.getTransactions();

        assertEquals(2, result.size());
    }

    @Test
    void getTransaction_ShouldReturn_WhenFound() {
        Transaction t = new Transaction();
        t.setId(1L);
        when(transactionRepo.findById(1L)).thenReturn(Optional.of(t));

        Transaction result = transactionService.getTransaction(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getTransaction_ShouldThrow_WhenNotFound() {
        when(transactionRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
                transactionService.getTransaction(1L)
        );
    }

    @Test
    void add_ShouldSaveTransaction_WhenAllValid() {
        Transaction t = new Transaction();
        Category category = new Category();
        category.setId(10L);
        t.setCategory(category);
        t.setAmount(100.0);

        Budget budget = mock(Budget.class);

        when(categoryRepo.findById(10L)).thenReturn(Optional.of(category));
        when(budgetService.getBudget(1L)).thenReturn(budget);
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = transactionService.add(1L, t);

        assertNotNull(result.getDate());
        verify(budget).updateCurrentProgress(100.0, category);
        verify(budgetService).updateStatus(budget);
        verify(transactionRepo).save(t);
    }

    @Test
    void add_ShouldThrow_WhenCategoryNotFound() {
        Transaction t = new Transaction();
        Category category = new Category();
        category.setId(999L);
        t.setCategory(category);

        when(categoryRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                transactionService.add(1L, t)
        );
    }

    @Test
    void delete_ShouldCallDeleteById() {
        transactionService.delete(42L);
        verify(transactionRepo).deleteById(42L);
    }

    @Test
    void findTransactionsByBudgetId_ShouldReturnList() {
        List<Transaction> txs = List.of(new Transaction(), new Transaction());
        when(transactionRepo.findByBudgetId(1L)).thenReturn(txs);

        List<Transaction> result = transactionService.findTransactionsByBudgetId(1L);
        assertEquals(2, result.size());
    }

    @Test
    void findByCategory_ShouldReturnList() {
        List<Transaction> txs = List.of(new Transaction());
        when(transactionRepo.findByCategoryId(5L)).thenReturn(txs);

        List<Transaction> result = transactionService.findByCategory(5L);
        assertEquals(1, result.size());
    }
}
