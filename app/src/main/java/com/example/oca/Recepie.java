package com.example.oca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class Recepie extends Fragment {

    private FoodItem foodItem;
    String[] bulletPoints;
    String ingredients, directions;
    public Recepie(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recepie, container, false);

        ImageView foodImage = view.findViewById(R.id.food_image);
        TextView foodTitle = view.findViewById(R.id.food_title);
        TextView ingredients = view.findViewById(R.id.ingredients);
        TextView directions = view.findViewById(R.id.directions);

        Picasso.get().load(foodItem.getImageLink()).into(foodImage);
        foodTitle.setText(foodItem.getTitle());
        bulletPoints = foodItem.getIngredients().split("\\.");
        String bulletPointText = "";

        for (String point : bulletPoints) {
            bulletPointText += "\u2022 " + point.trim() + "\n";
        }

        ingredients.setText(bulletPointText);

        bulletPoints = foodItem.getDirections().split("\\.");
        bulletPointText = "";

        for (String point : bulletPoints) {
            bulletPointText += "\u2022 " + point.trim() + "\n";
        }

        directions.setText(bulletPointText);


        return view;
    }
}
