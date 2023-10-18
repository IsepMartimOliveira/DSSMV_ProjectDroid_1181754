package com.example.onlinesupertmarket.Model;

public class Ingredient {
    private String name;
    private  boolean include;
    private  String image;

    public Ingredient(String name, boolean include, String image) {
        this.name = name;
        this.include = include;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
