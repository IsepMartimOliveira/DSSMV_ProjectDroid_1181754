package com.example.onlinesupertmarket.DTO;

import java.util.List;

public class RecepieInformationDTO {
    private List<ExtendedIngridientsDTO> extendedIngredients;

    public List<ExtendedIngridientsDTO> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<ExtendedIngridientsDTO> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }
}
