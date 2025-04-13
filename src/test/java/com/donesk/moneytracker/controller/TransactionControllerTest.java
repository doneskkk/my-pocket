package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.model.CategoryType;
import com.donesk.moneytracker.model.Transaction;
import com.donesk.moneytracker.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTransactions_ShouldReturnList() throws Exception {
        Transaction tx = new Transaction();
        tx.setId(1L);
        tx.setAmount(100.0);

        Mockito.when(transactionService.findTransactionsByBudgetId(1L))
                .thenReturn(List.of(tx));

        mockMvc.perform(get("/api/v1/budgets/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].amount").value(100.0));
    }
    @Test
    void addTransaction_ShouldReturnTransaction() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setType(CategoryType.valueOf("INCOME"));

        Transaction tx = new Transaction();
        tx.setAmount(200.0);
        tx.setCategory(category);
        tx.setDate(LocalDate.now());

        Transaction saved = new Transaction();
        saved.setId(2L);
        saved.setAmount(200.0);
        saved.setCategory(category);
        saved.setDate(tx.getDate());

        Mockito.when(transactionService.add(eq(1L), any(Transaction.class)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/v1/budgets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tx)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.amount").value(200.0));
    }
    @Test
    void getTransaction_ShouldReturnTransaction() throws Exception {
        Transaction tx = new Transaction();
        tx.setId(3L);
        tx.setAmount(300.0);

        Mockito.when(transactionService.getTransaction(3L)).thenReturn(tx);

        mockMvc.perform(get("/api/v1/budgets/1/transactions/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.amount").value(300.0));
    }

    @Test
    void deleteTransaction_ShouldReturnConfirmation() throws Exception {
        mockMvc.perform(delete("/api/v1/budgets/1/transactions/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with id 4was deleted"));

        Mockito.verify(transactionService).delete(4L);
    }
}
