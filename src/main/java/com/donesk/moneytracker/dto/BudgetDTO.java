package com.donesk.moneytracker.dto;

import com.donesk.moneytracker.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class BudgetDTO {
    private Long id;
    private String title;
    private Double currentProgress = 0.0;

    @JsonIgnore
    private List<Transaction> transactionList = new ArrayList<>();
    private boolean status = false;
}
