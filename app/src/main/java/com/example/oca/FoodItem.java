package com.example.oca;

public class FoodItem {
    private String title;
    private String imageLink;
    private String ingredients;
    private String directions;

    public FoodItem(String title, String imageLink, String ingredients, String directions) {
        this.title = title;
        this.imageLink = imageLink;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    public String getTitle() {
        return title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getDirections() {
        return directions;
    }


}

