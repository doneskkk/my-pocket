package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.dto.BudgetDTO;
import com.donesk.moneytracker.entity.Budget;
import com.donesk.moneytracker.service.BudgetService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/budgets")
public class BudgetController {

    private final ModelMapper modelMapper;
    private final BudgetService budgetService;

    public BudgetController(ModelMapper modelMapper, BudgetService budgetService) {
        this.modelMapper = modelMapper;
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgets(){
        List<BudgetDTO> budgetsDto = budgetService.getBudgets().stream().map(budget -> modelMapper.map(budget, BudgetDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<List<BudgetDTO>>(budgetsDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Budget> add(@RequestBody Budget budget, Authentication authentication){


        budgetService.create(budget, authentication);

        return new ResponseEntity<Budget>(budget, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudget(@PathVariable Long id){
        Budget budget = budgetService.getBudget(id);
        BudgetDTO budgetResponse = modelMapper.map(budget, BudgetDTO.class);
        return new ResponseEntity<>(budgetResponse, HttpStatus.OK);
    }
}
