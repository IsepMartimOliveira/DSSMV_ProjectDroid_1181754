package com.example.onlinesupertmarket.DTO;

public class ShoppingCartRequest {
    private String item;

    public ShoppingCartRequest(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
