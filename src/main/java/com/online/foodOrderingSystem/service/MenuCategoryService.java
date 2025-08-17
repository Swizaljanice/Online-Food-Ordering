package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.repository.MenuCategoryRepository;
import com.online.foodOrderingSystem.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuCategoryService {

    @Autowired
    private MenuCategoryRepository categoryRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public MenuCategoryService(MenuCategoryRepository categoryRepository,
                               RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuCategory addCategory(Long restaurantId, MenuCategory category) {
        log.info("Adding new menu category to restaurantId={} -> {}", restaurantId, category);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.warn("Restaurant not found for id={}", restaurantId);
                    return new RuntimeException("Restaurant not found");
                });

        category.setRestaurant(restaurant);
        MenuCategory saved = categoryRepository.save(category);

        log.info("Menu category created with id={} under restaurantId={}", saved.getId(), restaurantId);
        return saved;
    }

    public List<MenuCategory> getCategoriesByRestaurant(Long restaurantId) {
        log.info("Fetching categories for restaurantId={}", restaurantId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.warn("Restaurant not found for id={}", restaurantId);
                    return new RuntimeException("Restaurant not found");
                });

        List<MenuCategory> categories = restaurant.getCategories();
        log.info("Found {} categories for restaurantId={}", categories.size(), restaurantId);
        return categories;
    }

    public MenuCategory updateCategory(Long categoryId, MenuCategory updatedCategory) {
        log.info("Updating menu category id={} with new values: {}", categoryId, updatedCategory);

        MenuCategory existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Menu category not found with id={}", categoryId);
                    return new RuntimeException("Menu category not found with id: " + categoryId);
                });

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());

        MenuCategory saved = categoryRepository.save(existingCategory);
        log.info("Menu category id={} updated successfully", categoryId);
        return saved;
    }

    public boolean deleteCategory(Long categoryId) {
        log.info("Deleting menu category with id={}", categoryId);

        MenuCategory existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Menu category not found with id={}", categoryId);
                    return new RuntimeException("Menu category not found with id: " + categoryId);
                });

        categoryRepository.delete(existingCategory);
        log.info("Menu category deleted successfully with id={}", categoryId);
        return true;
    }
}
