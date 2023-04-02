package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Journal extends AppCompatActivity {
    CardView BPCard, diabetesCard, bmiCard, faqCard;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        BPCard = (CardView) findViewById(R.id.BloodPressureCard);
        diabetesCard = (CardView) findViewById(R.id.DiabetesCard);
        bmiCard = (CardView) findViewById(R.id.BMICard);
        faqCard = (CardView) findViewById(R.id.FAQCard);
        back = (Button) findViewById(R.id.back);

        BPCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Journal.this, PressureReading.class);
                startActivity(intent);
            }
        });

        diabetesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Journal.this, DiabetesReading.class);
                startActivity(intent);
            }
        });

        bmiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Journal.this, BMI.class);
                startActivity(intent);
            }
        });

        faqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Journal.this, FAQ.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Journal.this, MainPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Journal.this, MainPage.class);
        startActivity(intent);
    }
}