package com.example.oca;

public class NewsArticle extends com.kwabenaberko.newsapilib.models.Article {
    private String category;

    public NewsArticle(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
