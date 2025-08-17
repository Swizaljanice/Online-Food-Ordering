package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.repository.MenuCategoryRepository;
import com.online.foodOrderingSystem.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuCategoryServiceTest {

    @Mock
    private MenuCategoryRepository categoryRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuCategoryService categoryService;

    @Test
    void addCategory_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        MenuCategory category = new MenuCategory();
        category.setName("Starters");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(categoryRepository.save(any(MenuCategory.class))).thenReturn(category);

        MenuCategory result = categoryService.addCategory(1L, category);

        assertEquals("Starters", result.getName());
        assertEquals(restaurant, result.getRestaurant());
    }

    @Test
    void addCategory_RestaurantNotFound_Throws() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryService.addCategory(1L, new MenuCategory()));
    }

    @Test
    void getCategoriesByRestaurant_Success() {
        Restaurant restaurant = new Restaurant();
        MenuCategory mc = new MenuCategory();
        mc.setId(10L);
        restaurant.setCategories(List.of(mc));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        List<MenuCategory> result = categoryService.getCategoriesByRestaurant(1L);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }

    @Test
    void getCategoriesByRestaurant_RestaurantNotFound_Throws() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryService.getCategoriesByRestaurant(1L));
    }

    @Test
    void updateCategory_Success() {
        MenuCategory existing = new MenuCategory();
        existing.setId(5L);
        MenuCategory updated = new MenuCategory();
        updated.setName("New Name");
        updated.setDescription("New Desc");

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(MenuCategory.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuCategory result = categoryService.updateCategory(5L, updated);
        assertEquals("New Name", result.getName());
        assertEquals("New Desc", result.getDescription());
    }

    @Test
    void updateCategory_NotFound_Throws() {
        when(categoryRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> categoryService.updateCategory(5L, new MenuCategory()));
    }

    @Test
    void deleteCategory_Success() {
        MenuCategory existing = new MenuCategory();
        existing.setId(7L);

        when(categoryRepository.findById(7L)).thenReturn(Optional.of(existing));

        boolean result = categoryService.deleteCategory(7L);
        verify(categoryRepository).delete(existing);
        assertTrue(result);
    }

    @Test
    void deleteCategory_NotFound_Throws() {
        when(categoryRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(7L));
    }
}
