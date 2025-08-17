package com.online.foodOrderingSystem.dto;

import java.util.List;

public class MenuCategoryDto {
    private Long categoryId;
    private String categoryName;
    private String description;
    private List<MenuItemDto> items;

    public MenuCategoryDto(Long categoryId, String categoryName, String description, List<MenuItemDto> items) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.items = items;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MenuItemDto> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDto> items) {
        this.items = items;
    }
}
