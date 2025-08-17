package com.online.foodOrderingSystem.dto;

import java.math.BigDecimal;

public class MenuItemDto {
    private Long itemId;
    private String name;
    private String description;
    private java.math.BigDecimal price;
    private String imageUrl;
    private boolean available;

    public MenuItemDto(Long itemId, String name, String description, java.math.BigDecimal price, String imageUrl, boolean available) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
