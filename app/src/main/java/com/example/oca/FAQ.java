package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FAQ extends AppCompatActivity {
    TextView chatbot;
    RecyclerView recyclerView;
    List<Answers> answersList;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        chatbot = findViewById(R.id.to_chatbot);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        chatbot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        initData();
        setRecyclerView();

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FAQ.this, Chatbot.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FAQ.this, Journal.class);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        AnswersAdapter answersAdapter = new AnswersAdapter(answersList);
        recyclerView.setAdapter(answersAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {
        answersList = new ArrayList<>();
        answersList.add(new Answers("How do I know if I have high blood pressure?",
                "High blood pressure often doesn't have any symptoms, so you usually don't feel it." +
                        " For that reason, hypertension is usually diagnosed by a health care professional during a routine checkup." +
                        " \n\nIf you have a close relative with hypertension, or other risk factors, it is especially important to pay attention to your blood pressure reading." +
                        " \n\nIf your blood pressure is extremely high, you may have unusually strong headaches, chest pain, difficulty breathing, or poor exercise tolerance." +
                        " If you have any of these symptoms, seek an evaluation promptly."));

        answersList.add(new Answers("What are systolic and diastolic blood pressure?",
                "Systolic blood pressure, the first number, measures the pressure in your arteries when your heart beats." +
                        "\n\nDiastolic blood pressure, the second number, measures the pressure in your arteries when your heart rests between beats."));

        answersList.add(new Answers("What are the symptoms of diabetes?","Some people with diabetes don’t have any of these signs or symptoms stated below. The only way to know if you have diabetes is to have your doctor do a blood test." +
                "1)Being very thirsty \n2)Urinating often \n3)Feeling very hungry or tired \n4)losing weight without trying \n5)Sores that heal slowly \n6)Dry, itchy skin \n7)Feelings of pins and needles in your feet \n8)Blurry eyesight"));

        answersList.add(new Answers("What risk factors increase the likelihood of diabetes?",
                "1)Being overweight or obese \n2)Having a parent, brother, or sister with diabetes \n3)Having high blood pressure measuring 140/90 or higher \n4)Being physically inactive; exercising fewer than three times a week"));

        answersList.add(new Answers("What is Body Mass Index (BMI) and why is it important?",
                "It is a way to measure your size. It allows you to see if you are a healthy weight for your height. BMI is important as it is able to predict, with considerable accuracy, mortality and morbidity rates in populations."));
    }
}