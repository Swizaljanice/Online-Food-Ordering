package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.MenuDto;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.service.RestaurantService;
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
class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    @Test
    void addRestaurant_ReturnsSavedRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Rest");

        when(restaurantService.addRestaurant(restaurant)).thenReturn(restaurant);

        ResponseEntity<Restaurant> resp = restaurantController.addRestaurant(restaurant);
        assertEquals(restaurant, resp.getBody());
    }

    @Test
    void getAllRestaurants_ReturnsList() {
        List<Restaurant> restaurants = List.of(new Restaurant());
        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> resp = restaurantController.getAllRestaurants();
        assertEquals(restaurants, resp.getBody());
    }

    @Test
    void updateRestaurant_ReturnsUpdatedRestaurant() {
        Restaurant updated = new Restaurant();
        updated.setId(1L);
        updated.setName("Updated");

        when(restaurantService.updateRestaurant(updated)).thenReturn(updated);

        ResponseEntity<Restaurant> resp = restaurantController.updateRestaurant(1L, updated);
        assertEquals(updated, resp.getBody());
    }

    @Test
    void deleteRestaurant_ReturnsSuccessMessage() {
        ResponseEntity<String> resp = restaurantController.deleteRestaurant(1L);
        assertEquals("Restaurant deleted successfully", resp.getBody());
        verify(restaurantService).deleteRestaurant(1L);
    }

    @Test
    void getRestaurantMenu_ReturnsMenuDto() {
        MenuDto menu = new MenuDto(1L, "Test Rest", List.of());
        when(restaurantService.getMenuByRestaurantId(1L)).thenReturn(menu);

        ResponseEntity<MenuDto> resp = restaurantController.getRestaurantMenu(1L);
        assertEquals(menu, resp.getBody());
    }
}
