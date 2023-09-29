package com.example.onlinesupertmarket.Model;

public class User {
    private String username;
    private String spoonacularPassword;
    private String hash;

    public User(String username, String spoonacularPassword, String hash) {
        this.username = username;
        this.spoonacularPassword = spoonacularPassword;
        this.hash = hash;
    }

    public String getUsername() {
        return username;
    }

    public String getSpoonacularPassword() {
        return spoonacularPassword;
    }

    public String getHash() {
        return hash;
    }
}