package com.donesk.moneytracker.service;


import com.donesk.moneytracker.model.Category;
import com.donesk.moneytracker.repository.CategoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategory_ShouldReturn_WhenFound() {
        Category category = new Category();
        category.setId(1L);

        when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(1L);

        assertEquals(1L, result.getId());
    }


    @Test
    void delete_ShouldCallDeleteById() {
        categoryService.delete(1L);
        verify(categoryRepo).deleteById(1L);
    }
}
