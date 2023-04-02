package com.example.oca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {
    private List<FoodItem> foodItems;

    public FoodItemAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.titleView.setText(foodItem.getTitle());
        // Load image using Picasso if imageLink has been successfully loaded

        Picasso.get().load(foodItem.getImageLink()).placeholder(R.drawable.food).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                FoodItem foodItem = foodItems.get(position);

                // Create a bundle to pass the necessary data to the Recepie fragment
                Bundle bundle = new Bundle();
                bundle.putString("title", foodItem.getTitle());
                bundle.putString("image", foodItem.getImageLink());
                bundle.putString("ingredients", foodItem.getIngredients());
                bundle.putString("directions", foodItem.getDirections());

                // Create a new instance of the Recepie fragment
                Recepie fragment = new Recepie(foodItem);
                fragment.setArguments(bundle);

                // Open the Recepie fragment
                FragmentTransaction transaction = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.food_to_eat_frag, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        // Show placeholder image if imageLink has not been loaded yet
        //  holder.imageView.setImageResource(R.drawable.food);
    }


    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public class FoodItemViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private ImageView imageView;

        public FoodItemViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.food_title);
            imageView = itemView.findViewById(R.id.food_image);

        }

    }
}
