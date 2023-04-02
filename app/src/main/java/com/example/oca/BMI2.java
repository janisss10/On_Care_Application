package com.example.oca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BMI2 extends AppCompatActivity {
    TextView BMIDisplay,BMICategory,gender, Age;
    Button gotoBMI;

    ImageView imageView;
    String BMI;
    String BMI_comment="normal";
    float intBMI, intHeight,intWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi2);
        imageView = (ImageView) findViewById(R.id.imageview);
        BMIDisplay = (TextView) findViewById(R.id.BMIDisplay);
        gender = (TextView) findViewById(R.id.genderDisplay);
        Age = (TextView) findViewById(R.id.Age);
        BMICategory = (TextView) findViewById(R.id.BMICategoryDisplay);
        gotoBMI = (Button) findViewById(R.id.gotoBMI);

        String height = getIntent().getStringExtra("height");
        String weight = getIntent().getStringExtra("weight");
        String genders = getIntent().getStringExtra("gender");
        String age = getIntent().getStringExtra("age");

        gender.setText(genders);
        Age.setText(age);

        intHeight = Float.parseFloat(height);
        intWeight = Float.parseFloat(weight);

        intHeight = intHeight/100;
        intBMI = intWeight/(intHeight*intHeight);

        BMI = Float.toString(intBMI);
        System.out.println(BMI);

        if(intBMI<16)
        {
            BMICategory.setText("Severe Thinness");
            imageView.setImageResource(R.drawable.cross);
            BMI_comment="low";
        }

        else if(intBMI<16.9 && intBMI>16)
        {
            BMICategory.setText("Moderate Thinness");
            imageView.setImageResource(R.drawable.warning);
            BMI_comment="low";
        }

        else if(intBMI<18.4 && intBMI>17)
        {
            BMICategory.setText("Mild Thinness");
            imageView.setImageResource(R.drawable.warning);
            BMI_comment="low";
        }

        else if(intBMI<24.9 && intBMI>18.5 )
        {
            BMICategory.setText("Normal");
            imageView.setImageResource(R.drawable.okay);
            BMI_comment="normal";
        }

        else if(intBMI <29.9 && intBMI>25)
        {
            BMICategory.setText("Overweight");
            imageView.setImageResource(R.drawable.warning);
            BMI_comment="high";
        }

        else if(intBMI<34.9 && intBMI>30)
        {
            BMICategory.setText("Obese Class I");
            imageView.setImageResource(R.drawable.warning);
            BMI_comment="high";
        }

        else
        {
            BMICategory.setText("Obese Class II");
            imageView.setImageResource(R.drawable.cross);
            BMI_comment="high";
        }

        BMIDisplay.setText(BMI);
        Date currentDate = new Date();

        // Format the date to "dd/MM/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(currentDate);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("Date", formattedDate);
        data.put("BMI_value", String.valueOf(intBMI));
        data.put("BMI_comment", BMI_comment);
        data.put("User_ID", FirebaseAuth.getInstance().getUid());

        String recordID = db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("BMI_Records").document().getId(); // generate a new unique record ID

        db.collection("Patients").document(FirebaseAuth.getInstance().getUid()).collection("BMI_Records").document(recordID)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                    }
                });


        gotoBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BMI.class);
                startActivity(intent);
            }
        });
    }
}