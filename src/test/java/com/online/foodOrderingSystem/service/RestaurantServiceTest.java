package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.dto.MenuDto;
import com.online.foodOrderingSystem.entity.MenuCategory;
import com.online.foodOrderingSystem.entity.MenuItem;
import com.online.foodOrderingSystem.entity.Restaurant;
import com.online.foodOrderingSystem.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void addRestaurant_Success() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Rest");

        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant saved = restaurantService.addRestaurant(restaurant);
        assertEquals("Test Rest", saved.getName());
    }

    @Test
    void getAllRestaurants_ReturnsList() {
        Restaurant r = new Restaurant();
        when(restaurantRepository.findAll()).thenReturn(List.of(r));

        List<Restaurant> result = restaurantService.getAllRestaurants();
        assertEquals(1, result.size());
    }

    @Test
    void getRestaurantById_Found() {
        Restaurant r = new Restaurant();
        r.setId(1L);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        Restaurant found = restaurantService.getRestaurantById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void getRestaurantById_NotFound_ThrowsException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> restaurantService.getRestaurantById(1L));
    }

    @Test
    void updateRestaurant_Found_SavesUpdated() {
        Restaurant r = new Restaurant();
        r.setId(1L);
        r.setName("Updated");

        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(restaurantRepository.save(r)).thenReturn(r);

        Restaurant updated = restaurantService.updateRestaurant(r);
        assertEquals("Updated", updated.getName());
    }

    @Test
    void updateRestaurant_NotFound_ThrowsException() {
        Restaurant r = new Restaurant();
        r.setId(1L);

        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> restaurantService.updateRestaurant(r));
    }

    @Test
    void deleteRestaurant_Found_DeletesAndReturnsTrue() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        boolean result = restaurantService.deleteRestaurant(1L);
        assertTrue(result);
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void deleteRestaurant_NotFound_ReturnsFalse() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);
        boolean result = restaurantService.deleteRestaurant(1L);
        assertFalse(result);
        verify(restaurantRepository, never()).deleteById(any());
    }

    @Test
    void getMenuByRestaurantId_Found_ReturnsMenuDto() {
        // Prepare restaurant with category and item
        MenuItem item = new MenuItem();
        item.setId(100L);
        item.setName("Pizza");
        item.setDescription("Cheese");
        item.setPrice(BigDecimal.valueOf(10));
        item.setImageUrl("img");
        item.setAvailable(true);

        MenuCategory category = new MenuCategory();
        category.setId(200L);
        category.setName("Main");
        category.setDescription("desc");
        category.setItems(List.of(item));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Rest");
        restaurant.setCategories(List.of(category));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        MenuDto menu = restaurantService.getMenuByRestaurantId(1L);

        assertEquals(1L, menu.getRestaurantId());
        assertEquals("Test Rest", menu.getRestaurantName());
        assertEquals(1, menu.getCategories().size());
        assertEquals("Pizza", menu.getCategories().get(0).getItems().get(0).getName());
    }

    @Test
    void getMenuByRestaurantId_NotFound_ThrowsException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> restaurantService.getMenuByRestaurantId(1L));
    }
}
