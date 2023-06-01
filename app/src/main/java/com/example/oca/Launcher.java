package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.onesignal.OneSignal;

public class Launcher extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 4000;
    //variables

    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView slogan, version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_launcher);
        OneSignal.initWithContext(this);
        // on below line we are setting app id for our one signal
        OneSignal.setAppId("ec5cb5bd-b310-40e1-b5e2-807b144e1582");
        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //Hooks
        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);
        version = findViewById(R.id.version);

        logo.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);
        version.setAnimation(bottomAnim);

        //Intent to Main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (Launcher.this, PageSlider.class);
                Pair[] pairs = new Pair [2];
                pairs[0] = new Pair<View,String>(logo,"logo_image");
                pairs[1] = new Pair<View,String>(slogan,"logo_name");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Launcher.this,pairs);
                    startActivity(intent,options.toBundle());

                }
            }
        },SPLASH_SCREEN);
    }
}