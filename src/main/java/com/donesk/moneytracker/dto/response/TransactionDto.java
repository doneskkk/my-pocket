package com.donesk.moneytracker.dto.response;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDto {

    private String description;

    @Min(value = 1,  message = "Amount should be greater than 0")
    private Double amount;
}
