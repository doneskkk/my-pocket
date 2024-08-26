package com.donesk.moneytracker.service;

import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.exception.CategoryNotFoundException;
import com.donesk.moneytracker.repository.CategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;


    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Set<Category> getCategories(){
       return (Set<Category>) categoryRepo.findAll();
    }

    public Category getCategory(Long id){
       return categoryRepo.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category with id "+ id + " wasn't found"));
    }

    @Transactional
    public Optional<Category> create(Category category){
        categoryRepo.save(category);
        return Optional.of(category);
    }

    @Transactional
    public void delete(Long id){
        categoryRepo.deleteById(id);
    }




}
