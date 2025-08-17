package com.online.foodOrderingSystem.dto;
//import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
//@Schema(description = "Order item details")
public class OrderItemDto {

//    @Schema(description = "Order item ID", example = "1")
    private Long id;
//    @Schema(description = "Menu item ID", example = "1")
    private Long menuItemId;
//    @Schema(description = "Menu item name", example = "Chicken Tikka")
    private String menuItemName;
//    @Schema(description = "Price per item", example = "1000")
    private BigDecimal price;
//    @Schema(description = "Quantity", example = "2")
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
