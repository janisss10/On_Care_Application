package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar and enable full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_screen_image);

        // Get the ImageView reference and load the image using Picasso
        fullScreenImageView = findViewById(R.id.full_screen_image_view);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        Picasso.get().load(imageUrl).into(fullScreenImageView);
    }
}
