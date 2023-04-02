package com.example.oca;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class WriteRecord extends AppCompatActivity {

    String dateMedication, dateTest;
    private EditText medicationNameEditText, clinicMedicationEditText, dateMedicationEditText, descriptionMedicationEditText,
            testNameEditText, clinicTestEditText, dateTestEditText, descriptionTestEditText;
    private Button saveRecordButton;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_record);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Get references to the EditText fields and the "Save Record" Button
        medicationNameEditText = findViewById(R.id.medication_med);
        clinicMedicationEditText = findViewById(R.id.clinic_med);
        dateMedicationEditText = findViewById(R.id.date_med);
        descriptionMedicationEditText = findViewById(R.id.description_med);
        testNameEditText = findViewById(R.id.test_test);
        clinicTestEditText = findViewById(R.id.clinic_test);
        dateTestEditText = findViewById(R.id.date_test);
        descriptionTestEditText = findViewById(R.id.description_test);
        saveRecordButton = findViewById(R.id.save_record);

        // Add an OnClickListener to the "Save Record" Button
        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditText fields
                String medicationName = medicationNameEditText.getText().toString().trim();
                String clinicMedication = clinicMedicationEditText.getText().toString().trim();
                String descriptionMedication = descriptionMedicationEditText.getText().toString().trim();
                String testName = testNameEditText.getText().toString().trim();
                String clinicTest = clinicTestEditText.getText().toString().trim();
                //String dateTest = dateTestEditText.getText().toString().trim();
                String descriptionTest = descriptionTestEditText.getText().toString().trim();

                dateMedicationEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(WriteRecord.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateMedication = dayOfMonth + "/" + (month + 1) + "/" + year;
                                dateMedicationEditText.setText(dateMedication);
                            }
                        }, year, month, day);

                        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

                dateTestEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(WriteRecord.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateTest = dayOfMonth + "/" + (month + 1) + "/" + year;
                                dateTestEditText.setText(dateTest);
                            }
                        }, year, month, day);

                        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

                // Create a new record object with the values from the EditText fields
                Map<String, Object> record = new HashMap<>();
                record.put("medication", medicationName);
                record.put("clinic", clinicMedication);
                record.put("date", dateMedicationEditText.getText().toString());
                record.put("description", descriptionMedication);

                // Add the record to Firebase Firestore
                String doc_id = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Medication_Records").document().getId();
                db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Medication_Records").document(doc_id).set(record).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

                record = new HashMap<>();
                record.put("test", testName);
                record.put("clinic", clinicTest);
                record.put("date", dateTestEditText.getText().toString());
                record.put("description", descriptionTest);

                doc_id = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Test_Records").document().getId();
                db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Test_Records").document(doc_id).set(record).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(WriteRecord.this, "Record saved successfully.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainPage.class);
                        startActivity(i);
                    }
                });

            }
        });
    }
}
