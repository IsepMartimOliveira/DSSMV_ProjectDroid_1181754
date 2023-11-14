package com.example.onlinesupertmarket.DTO;

public class UserDTO {
    private String username;
    private String spoonacularPassword;
    private String hash;

    public UserDTO(String username, String spoonacularPassword, String hash) {
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