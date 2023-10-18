package com.example.onlinesupertmarket.DTO;

import java.util.List;

public class IngredientsDTO {

    private List<IngredientDTO> ingredients;


    public IngredientsDTO(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientDTO> getIngredientsListDTOS() {
        return ingredients;
    }

    public void setIngredientsListDTOS(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
