package com.example.onlinesupertmarket.DTO;

import com.example.onlinesupertmarket.Model.ShoppingCartItem;

import java.util.List;

public class AisleDTO {

    private List<ShoppingInfoDTO> items;



    public List<ShoppingInfoDTO> getItems() {
        return items;
    }

    public void setItems(List<ShoppingInfoDTO> items) {
        this.items = items;
    }
}
