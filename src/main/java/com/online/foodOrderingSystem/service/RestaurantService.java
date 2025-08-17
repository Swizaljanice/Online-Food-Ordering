package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.dto.MenuCategoryDto;
import com.online.foodOrderingSystem.dto.MenuDto;
import com.online.foodOrderingSystem.dto.MenuItemDto;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        log.info("Adding new restaurant: {}", restaurant);
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant saved successfully with id={}", saved.getId());
        return saved;
    }

    public List<Restaurant> getAllRestaurants() {
        log.info("Fetching all restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        log.info("Found {} restaurants", restaurants.size());
        return restaurants;
    }

    public Restaurant getRestaurantById(Long id) {
        log.info("Fetching restaurant by id={}", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Restaurant not found with id={}", id);
                    return new RuntimeException("Restaurant not found");
                });
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        log.info("Updating restaurant with id={}", restaurant.getId());
        if (restaurantRepository.existsById(restaurant.getId())) {
            Restaurant updated = restaurantRepository.save(restaurant);
            log.info("Restaurant with id={} updated successfully", restaurant.getId());
            return updated;
        } else {
            log.warn("Restaurant not found with id={}", restaurant.getId());
            throw new RuntimeException("Restaurant not found with id: " + restaurant.getId());
        }
    }

    public boolean deleteRestaurant(long id) {
        log.info("Deleting restaurant with id={}", id);
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            log.info("Restaurant with id={} deleted successfully", id);
            return true;
        }
        log.warn("Restaurant not found with id={}", id);
        return false;
    }

    public MenuDto getMenuByRestaurantId(Long restaurantId) {
        log.info("Fetching menu for restaurantId={}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.warn("Restaurant not found with id={}", restaurantId);
                    return new RuntimeException("Restaurant not found");
                });

        List<MenuCategoryDto> categoryDtos = restaurant.getCategories().stream()
                .map(category -> {
                    List<MenuItemDto> itemDtos = category.getItems().stream()
                            .map(item -> new MenuItemDto(
                                    item.getId(),
                                    item.getName(),
                                    item.getDescription(),
                                    item.getPrice(),
                                    item.getImageUrl(),
                                    item.isAvailable()
                            ))
                            .toList();
                    return new MenuCategoryDto(
                            category.getId(),
                            category.getName(),
                            category.getDescription(),
                            itemDtos
                    );
                }).toList();

        log.info("Menu for restaurantId={} retrieved successfully with {} categories",
                restaurantId, categoryDtos.size());

        return new MenuDto(restaurant.getId(), restaurant.getName(), categoryDtos);
    }
}
