package com.example.onlinesupertmarket.DTO;

import java.util.List;

public class CartItemResponseDTO {
    private List<AisleDTO> aisles;

    public List<AisleDTO> getAisles() {
        return aisles;
    }

    public void setAisles(List<AisleDTO> aisles) {
        this.aisles = aisles;
    }
}
