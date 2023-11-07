package com.example.onlinesupertmarket.Network;

public class Utils {
    public static String apiUrl="https://api.spoonacular.com/";
    public static String api_key="?apiKey=8d7e8f2c62fb434182d0bc9f11914e08";
    //8d7e8f2c62fb434182d0bc9f11914e08
    public static String userCreate="users/connect";
    public static String recipe="recipes/complexSearch";
    public static String cuisine="&cuisine=";
    public static String recipeURL="recipes/";
    public static String information="/information";
    public static  String mealPlaner="mealplanner/";
    public static String shoopingList="/shopping-list/items";
    public static String shoopingList2="/shopping-list";
    public static String hashURL="&hash=";
    public static  String includeNutrition="&includeNutrition=true";
     public static String searchIngredient="recipes/queries/analyze";



    public static String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }


}
