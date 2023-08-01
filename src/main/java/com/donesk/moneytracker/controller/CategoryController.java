package com.donesk.moneytracker.controller;

import com.donesk.moneytracker.entity.Category;
import com.donesk.moneytracker.entity.CategoryType;
import com.donesk.moneytracker.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category){
        categoryService.create(category);

       return new ResponseEntity<Category>(category, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Long> delete(@RequestParam Long id){
        categoryService.delete(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id){
        Category category = categoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
