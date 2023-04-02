package com.example.oca;

import static com.example.oca.CreateHealthActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewMedicalRecords extends AppCompatActivity {

    private Spinner medicationRecordsSpinner;
    private Spinner testRecordsSpinner;
    private FirebaseFirestore db;
    private LinearLayout imageGalleryLayout;
    TextView username;
    private List<String> imageUrls;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_records);
        medicationRecordsSpinner = findViewById(R.id.medication_spinner);
        testRecordsSpinner = findViewById(R.id.test_spinner);
        imageGalleryLayout = findViewById(R.id.images_layout);
        username=findViewById(R.id.username);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("Fullname").toString());
            }
        });

        // Get references to the Spinners

        imageUrls = new ArrayList<>();

        // Get the image URLs from the Firebase Firestore collection
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference imagesRef = db.collection("Patients")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Medical_Record_Images");
        imagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Iterate over the image documents and add the URLs to the list
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    String imageUrl = document.getString("URL");
                    imageUrls.add(imageUrl);
                }
                // Create the image views and add them to the layout
                for (String imageUrl : imageUrls) {
                    ImageView imageView = new ImageView(ViewMedicalRecords.this);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
                    layoutParams.setMargins(10, 0, 0, 0);
                    imageView.setLayoutParams(layoutParams);
                    Picasso.get().load(imageUrl).into(imageView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Show the full screen image view
                            Intent intent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                            intent.putExtra("imageUrl", imageUrl);
                            startActivity(intent);
                        }
                    });
                    imageGalleryLayout.addView(imageView);
                }
            }
        });

        // Populate the medication records spinner
        populateMedicationRecordsSpinner();

        // Populate the test records spinner
        populateTestRecordsSpinner();
    }

    private void populateMedicationRecordsSpinner() {
        // Query the Firestore collection for the medication records
        db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).collection("Medication_Records").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create a list of medication names
                List<String> medicationNames = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    medicationNames.add(document.getString("medication"));
                }

                // Create an adapter for the medication records spinner
                ArrayAdapter<String> medicationRecordsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicationNames);
                medicationRecordsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter for the medication records spinner
                medicationRecordsSpinner.setAdapter(medicationRecordsAdapter);
            } else {
                Log.d(TAG, "Error getting medication records: ", task.getException());
            }
        });
    }

    private void populateTestRecordsSpinner() {
        // Query the Firestore collection for the test records
        db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Test_Records").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create a list of test names
                List<String> testNames = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    testNames.add(document.getString("test"));
                }

                // Create an adapter for the test records spinner
                ArrayAdapter<String> testRecordsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, testNames);
                testRecordsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter for the test records spinner
                testRecordsSpinner.setAdapter(testRecordsAdapter);
            } else {
                Log.d(TAG, "Error getting test records: ", task.getException());
            }
        });
    }
}
