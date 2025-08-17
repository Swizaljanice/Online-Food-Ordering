package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.MenuItem;
import com.online.foodOrderingSystem.service.MenuItemService;
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
class MenuItemControllerTest {

    @Mock
    private MenuItemService itemService;

    @InjectMocks
    private MenuItemController itemController;

    @Test
    void addMenuItem_ReturnsSavedItem() {
        MenuItem item = new MenuItem();
        item.setId(1L);

        when(itemService.addMenuItem(2L, item)).thenReturn(item);

        ResponseEntity<MenuItem> response = itemController.addMenuItem(2L, item);

        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateMenuItem_ReturnsUpdatedItem() {
        MenuItem updated = new MenuItem();
        updated.setId(5L);
        updated.setName("Updated");

        when(itemService.updateMenuItem(updated)).thenReturn(updated);

        ResponseEntity<MenuItem> response = itemController.updateMenuItem(5L, updated);
        assertEquals("Updated", response.getBody().getName());
    }

    @Test
    void deleteMenuItem_ReturnsSuccessMessage() {
        ResponseEntity<String> response = itemController.deleteMenuItem(9L);
        assertEquals("MenuItem with ID 9 deleted successfully.", response.getBody());
        verify(itemService).deleteMenuItem(9L);
    }

    @Test
    void getMenuItemById_ReturnsItem() {
        MenuItem item = new MenuItem();
        item.setId(3L);

        when(itemService.getMenuItemById(3L)).thenReturn(item);

        ResponseEntity<MenuItem> response = itemController.getMenuItemById(3L);

        assertEquals(3L, response.getBody().getId());
    }

    @Test
    void getAllMenuItems_ReturnsList() {
        when(itemService.getAllMenuItems()).thenReturn(List.of(new MenuItem()));

        ResponseEntity<List<MenuItem>> response = itemController.getAllMenuItems();

        assertEquals(1, response.getBody().size());
    }
}
