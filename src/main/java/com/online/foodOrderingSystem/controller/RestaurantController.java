package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.MenuDto;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.repository.RestaurantRepository;
import com.online.foodOrderingSystem.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;


    @Autowired
    private RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        log.info("Request to add a new restaurant: {}", restaurant);
        Restaurant saved = restaurantService.addRestaurant(restaurant);
        log.info("Restaurant added successfully with id={}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        log.info("Request to fetch all restaurants");
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        log.info("Found {} restaurants", restaurants.size());
        return ResponseEntity.ok(restaurants);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateRestaurant/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable long id,
                                                       @RequestBody Restaurant updatedRestaurant) {
        log.info("Request to update restaurant with id={} data: {}", id, updatedRestaurant);
        updatedRestaurant.setId(id);
        Restaurant updated = restaurantService.updateRestaurant(updatedRestaurant);
        log.info("Restaurant with id={} updated successfully", id);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteRestaurant/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable long id) {
        log.info("Request to delete restaurant with id={}", id);
        restaurantService.deleteRestaurant(id);
        String msg = "Restaurant deleted successfully";
        log.info(msg + " (id={})", id);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<MenuDto> getRestaurantMenu(@PathVariable Long id) {
        log.info("Request to fetch menu for restaurantId={}", id);
        MenuDto menu = restaurantService.getMenuByRestaurantId(id);
        log.info("Menu for restaurantId={} retrieved successfully with {} categories",
                id, menu.getCategories() != null ? menu.getCategories().size() : 0);
        return ResponseEntity.ok(menu);
    }

}
