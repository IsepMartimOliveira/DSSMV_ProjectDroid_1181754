package com.example.onlinesupertmarket.Model;

import com.example.onlinesupertmarket.DTO.IngredientDTO;

import java.util.List;

public class Ingredients {
    private List<Ingredient> ingredients;

    public Ingredients(List<Ingredient> ingredientsList) {
        this.ingredients = ingredientsList;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredients;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredients = ingredientsList;
    }
}
