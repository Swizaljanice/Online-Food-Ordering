package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.service.MenuCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class MenuCategoryController {

    @Autowired
    private MenuCategoryService categoryService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public MenuCategoryController(MenuCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @PostMapping("/addMenuCategory/{restaurantId}")
    public ResponseEntity<MenuCategory> addCategory(@PathVariable Long restaurantId,
                                                    @RequestBody MenuCategory category) {
        log.info("Request to add menu category for restaurantId={} -> {}", restaurantId, category);
        MenuCategory saved = categoryService.addCategory(restaurantId, category);
        log.info("Menu category created: id={} for restaurantId={}", saved.getId(), restaurantId);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @PutMapping("/updateMenuCategory/{categoryId}")
    public ResponseEntity<MenuCategory> updateCategory(@PathVariable Long categoryId,
                                                       @RequestBody MenuCategory updatedCategory) {
        log.info("Request to update menu category categoryId={} -> {}", categoryId, updatedCategory);
        MenuCategory category = categoryService.updateCategory(categoryId, updatedCategory);
        log.info("Menu category with id={} updated successfully", categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuCategory>> getCategoriesByRestaurant(@PathVariable Long restaurantId) {
        log.info("Request to fetch all categories for restaurantId={}", restaurantId);
        List<MenuCategory> categories = categoryService.getCategoriesByRestaurant(restaurantId);
        log.info("Found {} categories for restaurantId={}", categories.size(), restaurantId);
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT_STAFF')")
    @DeleteMapping("/deleteMenuCategory/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        log.info("Request to delete menu category with id={}", categoryId);
        categoryService.deleteCategory(categoryId);
        String msg = "Category with ID " + categoryId + " deleted successfully.";
        log.info(msg);
        return ResponseEntity.ok(msg);
    }
}
