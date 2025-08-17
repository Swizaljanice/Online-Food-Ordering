package com.online.foodOrderingSystem.dto;

import java.util.List;

public class MenuDto {
    private Long restaurantId;
    private String restaurantName;
    private List<MenuCategoryDto> categories;

    public MenuDto(Long restaurantId, String restaurantName, List<MenuCategoryDto> categories) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.categories = categories;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public List<MenuCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<MenuCategoryDto> categories) {
        this.categories = categories;
    }
}





