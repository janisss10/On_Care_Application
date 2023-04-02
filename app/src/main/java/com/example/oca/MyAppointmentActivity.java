package com.example.oca;

import static com.example.oca.CreateHealthActivity.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    Button back;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);

        db = FirebaseFirestore.getInstance();
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        back=findViewById(R.id.back);
        recyclerView = findViewById(R.id.listofAppointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Query all appointments for the current user
        Query query = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Appointments")
                .whereEqualTo("ID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Listen for changes in appointments collection
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Extract data from the document
                        String clinicName = document.getString("Clinic Name");
                        String contact = document.getString("Contact");
                        String date = document.getString("Date");
                        String detail = document.getString("Detail");
                        String email = document.getString("Email");
                        String id = document.getString("ID");
                        String time = document.getString("Time");
                        String datePosted = document.getDate("dateposted").toString();

                        // Create an Appointment object with the extracted data
                        Appointment appointment = new Appointment(clinicName, contact, date, detail, email, id, time, datePosted);

                        // Add the appointment to the list and notify the adapter
                        appointmentList.add(appointment);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainPage.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), MainPage.class);
        startActivity(i);
    }
}
