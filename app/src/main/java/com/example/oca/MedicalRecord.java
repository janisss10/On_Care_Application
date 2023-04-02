package com.example.oca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

public class MedicalRecord extends AppCompatActivity {
Button take_photo, write, view_records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        write=findViewById(R.id.write);
        take_photo=findViewById(R.id.take_photo);
        view_records=findViewById(R.id.view_records);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), WriteRecord.class);
                startActivity(i);

            }
        });

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), TakePhoto.class);
                startActivity(i);
            }
        });

        view_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), ViewMedicalRecords.class);
                startActivity(i);
            }
        });

    }


}