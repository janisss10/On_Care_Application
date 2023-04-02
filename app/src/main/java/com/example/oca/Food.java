package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.oca.FoodPageAdapter;
import com.example.oca.FoodToAvoid;
import com.example.oca.FoodToEat;
import com.example.oca.R;
import com.google.android.material.tabs.TabLayout;

public class Food extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // Create a new instance of the adapter
        FoodPageAdapter adapter = new FoodPageAdapter(getSupportFragmentManager());

        // Add the fragments to the adapter
        adapter.addFragment(new FoodToEat(), "Foods to Eat");
        adapter.addFragment(new FoodToAvoid(), "Foods to Avoid");

        // Set the adapter to the view pager
        viewPager.setAdapter(adapter);

        // Set the tab layout to use the view pager
        tabLayout.setupWithViewPager(viewPager);

        // Set a listener for when the user changes tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Display a toast message with the name of the selected tab
                Toast.makeText(getApplicationContext(), "Selected Tab: " + tab.getText(), Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
