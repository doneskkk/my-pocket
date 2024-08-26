package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.service.BudgetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final BudgetService budgetService;

    public UserController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

//    @GetMapping("/{username}/budgets")
//    public ResponseEntity<List<BudgetDTO>> getBudgetsCreatedBy(@PathVariable(value = "username") String username,
//                                                                     @RequestParam(value = "page", required = false, defaultValue = Constants.DEFAULT_PAGE_NUMBER) Integer page,
//                                                                 @RequestParam(value = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer size) {
//        List<BudgetDTO> response = budgetService.getBudgetsByCreatedBy(username, page, size);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
