package com.donesk.moneytracker.dto.response;

import com.donesk.moneytracker.model.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BudgetDTO {

    private Long id;
    private String title;
    private Double currentProgress;

    @JsonIgnore
    private List<Transaction> transactionList = new ArrayList<>();
    private boolean status;
    private Double goal;
}
