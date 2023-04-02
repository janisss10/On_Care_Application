package com.example.oca;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<Article> healthArticles;
    private List<Article> diabetesArticles;
    private List<Article> bloodPressureArticles;
    private Context context;

    public NewsAdapter(List<Article> healthArticles, List<Article> diabetesArticles, List<Article> bloodPressureArticles, Context context) {
        this.healthArticles = healthArticles;
        this.diabetesArticles = diabetesArticles;
        this.bloodPressureArticles = bloodPressureArticles;
        this.context = context;
    }

    public void setHealthArticles(List<Article> healthArticles) {
        this.healthArticles = healthArticles;
        notifyDataSetChanged();
    }

    public void setDiabetesArticles(List<Article> diabetesArticles) {
        this.diabetesArticles = diabetesArticles;
        notifyDataSetChanged();
    }

    public void setBloodPressureArticles(List<Article> bloodPressureArticles) {
        this.bloodPressureArticles = bloodPressureArticles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article;

        if (position < healthArticles.size()) {
            article = healthArticles.get(position);
        } else if (position < healthArticles.size() + diabetesArticles.size()) {
            article = diabetesArticles.get(position - healthArticles.size());
        } else {
            article = bloodPressureArticles.get(position - healthArticles.size() - diabetesArticles.size());
        }

        holder.newsDateTextView.setText(article.getPublishedAt());
        holder.newsTitleTextView.setText(article.getTitle());
        holder.newsDescriptionTextView.setText(article.getDescription());

        holder.newsLinkButton.setOnClickListener(v -> {
            // Open the news link in a webview within the app
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", article.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return healthArticles.size() + diabetesArticles.size() + bloodPressureArticles.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView newsDateTextView;
        public TextView newsTitleTextView;
        public TextView newsDescriptionTextView;
        public Button newsLinkButton;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsDateTextView = itemView.findViewById(R.id.newsDateTextView);
            newsTitleTextView = itemView.findViewById(R.id.newsTitleTextView);
            newsDescriptionTextView = itemView.findViewById(R.id.newsDescriptionTextView);
            newsLinkButton = itemView.findViewById(R.id.newsLinkButton);
        }
    }
}
