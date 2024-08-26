package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.dto.response.BudgetDTO;
import com.donesk.moneytracker.model.Budget;
import com.donesk.moneytracker.service.BudgetService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final ModelMapper modelMapper;
    private final BudgetService budgetService;

    public BudgetController(ModelMapper modelMapper, BudgetService budgetService) {
        this.modelMapper = modelMapper;
        this.budgetService = budgetService;
    }

//    @GetMapping
//    public ResponseEntity<List<BudgetDTO>> getBudgets(@RequestParam(value = "page", required = false) Integer page,
//                                                      @RequestParam(value = "size", required = false) Integer size,Authentication authentication){
//        List<Budget> budgets = budgetService.getBudgetsByCreatedBy(authentication.getName());
//        List<BudgetDTO> budgetsResponse = budgets.stream().map( budget -> modelMapper.map(budget, BudgetDTO.class)).collect(Collectors.toList());
//        return new ResponseEntity<>(budgetsResponse, HttpStatus.OK);
//    }



    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getBudgets(@RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "size", required = false) Integer size,Authentication authentication){
        List<Budget> budgets = budgetService.getBudgets();
        List<BudgetDTO> budgetsResponse = budgets.stream().map( budget -> modelMapper.map(budget, BudgetDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(budgetsResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Budget> add(@Valid @RequestBody Budget budget, BindingResult bd, Authentication authentication){
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
