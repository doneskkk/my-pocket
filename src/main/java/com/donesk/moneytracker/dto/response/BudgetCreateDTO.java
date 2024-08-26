package com.donesk.moneytracker.dto.response;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Goal cannot be null")
    @Min(value = 0, message = "Goal must be greater than or equal to 0")
    private Double goal;
}
