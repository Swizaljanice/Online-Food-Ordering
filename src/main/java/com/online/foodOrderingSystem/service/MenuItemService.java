package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.entity.MenuItem;
import com.online.foodOrderingSystem.repository.MenuCategoryRepository;
import com.online.foodOrderingSystem.repository.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository itemRepository;
    @Autowired
    private MenuCategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository itemRepository, MenuCategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public MenuItem addMenuItem(Long categoryId, MenuItem item) {
        log.info("Adding new menu item to categoryId={} -> {}", categoryId, item);

        MenuCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found for id={}", categoryId);
                    return new RuntimeException("Category not found");
                });

        item.setCategory(category);
        MenuItem saved = itemRepository.save(item);

        log.info("Menu item created with id={} under categoryId={}", saved.getId(), categoryId);
        return saved;
    }

    public MenuItem updateMenuItem(MenuItem item) {
        log.info("Updating menu item id={} with new values: {}", item.getId(), item);

        MenuItem existingItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> {
                    log.warn("Menu item not found with id={}", item.getId());
                    return new RuntimeException("MenuItem not found with id: " + item.getId());
                });

        existingItem.setName(item.getName());
        existingItem.setDescription(item.getDescription());
        existingItem.setPrice(item.getPrice());
        existingItem.setImageUrl(item.getImageUrl());
        existingItem.setAvailable(item.isAvailable());

        MenuItem saved = itemRepository.save(existingItem);
        log.info("Menu item id={} updated successfully", item.getId());
        return saved;
    }

    public void deleteMenuItem(Long id) {
        log.info("Deleting menu item with id={}", id);

        MenuItem existingItem = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Menu item not found with id={}", id);
                    return new RuntimeException("MenuItem not found with id: " + id);
                });

        itemRepository.delete(existingItem);
        log.info("Menu item deleted successfully with id={}", id);
    }

    public MenuItem getMenuItemById(Long id) {
        log.info("Fetching menu item by id={}", id);

        MenuItem item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Menu item not found with id={}", id);
                    return new RuntimeException("MenuItem not found with id: " + id);
                });

        log.info("Menu item found: {}", item);
        return item;
    }

    public List<MenuItem> getAllMenuItems() {
        log.info("Fetching all menu items");

        List<MenuItem> items = itemRepository.findAll();
        log.info("Found {} menu items", items.size());

        return items;
    }
}
