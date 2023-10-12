package com.example.onlinesupertmarket.DTO;

import java.util.List;

public class RecipeDTO {

    private List<RecipeItemDTO> results;
    private int offset;
    private int number;
    private int totalResults;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public RecipeDTO(List<RecipeItemDTO> results, int offset, int number, int totalResults) {
        this.results = results;
        this.offset = offset;
        this.number = number;
        this.totalResults = totalResults;
    }

    public List<RecipeItemDTO> getResults() {
        return results;
    }

    public void setResults(List<RecipeItemDTO> results) {
        this.results = results;
    }
}
