package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BMI extends AppCompatActivity {
    TextView currentHeight, currentWeight, Age;
    EditText Age_input;
    SeekBar seekbarforheight, seekbarforweight;
    Button calculatebmi, back;
    CardView male,female;

    int currentProgress, currentProgress2;
    String intProgress="0";
    String intProgress2="0";
    String typerofuser="0";
    String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        male = (CardView) findViewById(R.id.MaleCard);
        female = (CardView) findViewById(R.id.FemaleCard);
        currentWeight = findViewById(R.id.currentWeight);
        currentHeight = findViewById(R.id.currentHeight);
        seekbarforheight = findViewById(R.id.seekbarforheight);
        seekbarforweight = findViewById(R.id.seekbarforWeight);
        Age = findViewById(R.id.Age);
        Age_input = findViewById(R.id.Age_Input);
        calculatebmi = findViewById(R.id.calculatebmi);
        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BMI.this, Journal.class);
                startActivity(intent);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typerofuser="Male";

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typerofuser="Female";
            }
        });

        seekbarforheight.setMax(300);
        seekbarforheight.setProgress(0);
        seekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress = progress;
                intProgress = String.valueOf(currentProgress);
                currentHeight.setText(intProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbarforweight.setMax(300);
        seekbarforweight.setProgress(0);
        seekbarforweight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentProgress2 = progress;
                intProgress2 = String.valueOf(currentProgress2);
                currentWeight.setText(intProgress2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calculatebmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age=Age_input.getText().toString();

                if(typerofuser.equals("0"))
                {
                    Toast.makeText(BMI.this,"Select your gender first",Toast.LENGTH_SHORT).show();
                }
                else if(intProgress.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Adjust to your height first",Toast.LENGTH_SHORT).show();
                }
                else if(intProgress2.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Adjust to your weight first",Toast.LENGTH_SHORT).show();
                }
                else if (age.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter your age",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(BMI.this, BMI2.class);
                    intent.putExtra("gender", typerofuser);
                    intent.putExtra("height", intProgress);
                    intent.putExtra("weight", intProgress2);
                    intent.putExtra("age", age);
                    startActivity(intent);
                }
            }
        });

    }
}