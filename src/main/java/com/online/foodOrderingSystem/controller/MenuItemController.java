package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.MenuItem;
import com.online.foodOrderingSystem.service.MenuItemService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/items")
public class MenuItemController {

    @Autowired
    private MenuItemService itemService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public MenuItemController(MenuItemService itemService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @PostMapping("/addMenuItem/{categoryId}")
    public ResponseEntity<MenuItem> addMenuItem(@PathVariable Long categoryId,
                                                @RequestBody MenuItem item) {
        log.info("Request to add new menu item for categoryId={} -> {}", categoryId, item);
        MenuItem saved = itemService.addMenuItem(categoryId, item);
        log.info("Menu item created with id={} under categoryId={}", saved.getId(), categoryId);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @PutMapping("/updateMenuItem/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody MenuItem item) {
        log.info("Request to update menu item id={} -> {}", id, item);
        item.setId(id);
        MenuItem updatedItem = itemService.updateMenuItem(item);
        log.info("Menu item id={} updated successfully", id);
        return ResponseEntity.ok(updatedItem);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @DeleteMapping("/deleteMenuItem/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        log.info("Request to delete menu item id={}", id);
        itemService.deleteMenuItem(id);
        String msg = "MenuItem with ID " + id + " deleted successfully.";
        log.info(msg);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/getMenuItemById/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        log.info("Fetching menu item by id={}", id);
        MenuItem item = itemService.getMenuItemById(id);
        log.info("Found menu item: {}", item);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        log.info("Fetching all menu items");
        List<MenuItem> items = itemService.getAllMenuItems();
        log.info("Found {} menu items", items.size());
        return ResponseEntity.ok(items);
    }
}
