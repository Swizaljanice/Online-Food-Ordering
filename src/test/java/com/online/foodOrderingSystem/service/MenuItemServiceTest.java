package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.entity.MenuItem;
import com.online.foodOrderingSystem.repository.MenuCategoryRepository;
import com.online.foodOrderingSystem.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository itemRepository;
    @Mock
    private MenuCategoryRepository categoryRepository;

    @InjectMocks
    private MenuItemService itemService;

    @Test
    void addMenuItem_Success() {
        MenuCategory category = new MenuCategory();
        category.setId(1L);

        MenuItem item = new MenuItem();
        item.setName("Burger");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(itemRepository.save(any(MenuItem.class))).thenReturn(item);

        MenuItem result = itemService.addMenuItem(1L, item);

        assertEquals("Burger", result.getName());
        assertEquals(category, result.getCategory());
    }

    @Test
    void addMenuItem_CategoryNotFound_Throws() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> itemService.addMenuItem(1L, new MenuItem()));
    }

    @Test
    void updateMenuItem_Success() {
        MenuItem existing = new MenuItem();
        existing.setId(5L);
        existing.setName("Old");

        MenuItem updated = new MenuItem();
        updated.setId(5L);
        updated.setName("New");
        updated.setDescription("Desc");
        updated.setPrice(new BigDecimal(10.0));
        updated.setImageUrl("img.jpg");
        updated.setAvailable(true);

        when(itemRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(itemRepository.save(any(MenuItem.class))).thenAnswer(inv -> inv.getArgument(0));

        MenuItem result = itemService.updateMenuItem(updated);

        assertEquals("New", result.getName());
        assertEquals("Desc", result.getDescription());
        assertTrue(result.isAvailable());
    }

    @Test
    void updateMenuItem_NotFound_Throws() {
        when(itemRepository.findById(5L)).thenReturn(Optional.empty());
        MenuItem item = new MenuItem();
        item.setId(5L);
        assertThrows(RuntimeException.class, () -> itemService.updateMenuItem(item));
    }

    @Test
    void deleteMenuItem_Success() {
        MenuItem existing = new MenuItem();
        existing.setId(3L);

        when(itemRepository.findById(3L)).thenReturn(Optional.of(existing));

        itemService.deleteMenuItem(3L);

        verify(itemRepository).delete(existing);
    }

    @Test
    void deleteMenuItem_NotFound_Throws() {
        when(itemRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> itemService.deleteMenuItem(3L));
    }

    @Test
    void getMenuItemById_Success() {
        MenuItem existing = new MenuItem();
        existing.setId(7L);

        when(itemRepository.findById(7L)).thenReturn(Optional.of(existing));

        MenuItem result = itemService.getMenuItemById(7L);
        assertEquals(7L, result.getId());
    }

    @Test
    void getMenuItemById_NotFound_Throws() {
        when(itemRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> itemService.getMenuItemById(7L));
    }

    @Test
    void getAllMenuItems_ReturnsList() {
        when(itemRepository.findAll()).thenReturn(List.of(new MenuItem()));
        assertEquals(1, itemService.getAllMenuItems().size());
    }
}
