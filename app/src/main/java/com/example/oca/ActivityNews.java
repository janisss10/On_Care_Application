package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class ActivityNews extends AppCompatActivity {

    private RecyclerView recyclerViewHealth, recyclerViewDiabetes, recyclerViewBloodPressure;
    private NewsAdapter adapterHealth, adapterBloodPressure, adapterDiabetes;
    private ProgressBar progressBar;
    List<Article> health;
    List<Article> diabetes;
    List<Article> blood_pressure;
    NewsApiClient newsApiClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerViewHealth = findViewById(R.id.recyclerViewHealth);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);


        recyclerViewDiabetes = findViewById(R.id.recyclerViewDiabetes);
        recyclerViewBloodPressure = findViewById(R.id.recyclerViewBloodPressure);
        recyclerViewHealth.setLayoutManager(layoutManager1);
        recyclerViewDiabetes.setLayoutManager(layoutManager2);
        recyclerViewBloodPressure.setLayoutManager(layoutManager3);
        adapterHealth = new NewsAdapter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), this);
        recyclerViewHealth.setAdapter(adapterHealth);

        adapterDiabetes = new NewsAdapter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), this);
        recyclerViewDiabetes.setAdapter(adapterDiabetes);

        adapterBloodPressure = new NewsAdapter(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), this);
        recyclerViewBloodPressure.setAdapter(adapterBloodPressure);


        progressBar = findViewById(R.id.progbar);
        newsApiClient = new NewsApiClient("8fdad228865d4268aa99ec3a94e7707a");

        // Call the News API to get health-related news
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("health")
                        .language("en")
                        .pageSize(3)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // Add the health news articles to the adapter
                        progressBar.setVisibility(View.GONE);
                        health = response.getArticles();
                        adapterHealth.setHealthArticles(health);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("hypertension").category("health")
                        .language("en")
                        .pageSize(3)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // Add the hypertension news articles to the adapter
                        blood_pressure = response.getArticles();
                        adapterBloodPressure.setBloodPressureArticles(blood_pressure);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("diabetes")
                        .language("en")
                        .pageSize(3)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // Add the diabetes news articles to the adapter
                        diabetes = response.getArticles();
                        adapterDiabetes.setDiabetesArticles(diabetes);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



    }
}
