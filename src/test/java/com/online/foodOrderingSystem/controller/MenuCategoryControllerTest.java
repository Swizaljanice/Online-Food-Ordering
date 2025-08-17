package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.service.MenuCategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuCategoryControllerTest {

    @Mock
    private MenuCategoryService categoryService;

    @InjectMocks
    private MenuCategoryController categoryController;

    @Test
    void addCategory_ReturnsSavedCategory() {
        MenuCategory category = new MenuCategory();
        category.setId(1L);

        when(categoryService.addCategory(2L, category)).thenReturn(category);

        ResponseEntity<MenuCategory> response = categoryController.addCategory(2L, category);

        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateCategory_ReturnsUpdated() {
        MenuCategory updated = new MenuCategory();
        updated.setId(5L);
        updated.setName("Updated");

        when(categoryService.updateCategory(5L, updated)).thenReturn(updated);

        ResponseEntity<MenuCategory> response = categoryController.updateCategory(5L, updated);
        assertEquals("Updated", response.getBody().getName());
    }

    @Test
    void getCategoriesByRestaurant_ReturnsList() {
        MenuCategory cat = new MenuCategory();
        cat.setId(3L);

        when(categoryService.getCategoriesByRestaurant(1L)).thenReturn(List.of(cat));

        ResponseEntity<List<MenuCategory>> response = categoryController.getCategoriesByRestaurant(1L);

        assertEquals(1, response.getBody().size());
        assertEquals(3L, response.getBody().get(0).getId());
    }

    @Test
    void deleteCategory_ReturnsSuccessMessage() {
        ResponseEntity<String> response = categoryController.deleteCategory(9L);

        assertEquals("Category with ID 9 deleted successfully.", response.getBody());
        verify(categoryService).deleteCategory(9L);
    }
}
