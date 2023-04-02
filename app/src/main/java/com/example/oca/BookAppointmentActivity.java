package com.example.oca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*import android.app.AlertDialog;*/
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class BookAppointmentActivity extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private Spinner spinner;
    TextInputLayout date, time, detail, contact;
    Button cancel, back, view, appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        date = findViewById(R.id.appdate);
        time = findViewById(R.id.appTime);
        detail = findViewById(R.id.appDetails);
        contact = findViewById(R.id.appContact);
        appointment = findViewById(R.id.book);
        view=findViewById(R.id.view);
        spinner = (Spinner) findViewById(R.id.clinicSpinner);
        cancel=findViewById(R.id.cancel);
        back=findViewById(R.id.back);
        final ArrayList<String> clinicName;
        final ArrayList<String> clinicId;
        clinicName = new ArrayList<String>();
        clinicId = new ArrayList<String>();


        fStore.collection("Clinic")
                .orderBy("Name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for ( QueryDocumentSnapshot document : task.getResult()) {
                                clinicName.add(document.getData().get("Name").toString());
                                clinicId.add(document.getId());
                                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, clinicName);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                            }
                        }
                        else { Toast.makeText(BookAppointmentActivity.this, "Error retrieving Clinics", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BookAppointmentActivity.this);
                dialog.setTitle("Book clinic appointment");
                dialog.setMessage("Upon confirmation, the respective clinic staff will contact you with your registered email/phone in a short while to confirm your appointment slot!");
                dialog.setPositiveButton("Confirm Appointment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = spinner.getSelectedItemPosition();
                        String selectedDocId = clinicId.get(position);
                        if (!selectedDocId.equals("")) {
                            Map<String, Object> appointment = new HashMap<>();
                            appointment.put("dateposted", new Date());
                            appointment.put("Email", currentUser.getEmail());
                            appointment.put("ID", currentUser.getUid());
                            appointment.put("Clinic Name", clinicName.get(position));
                            appointment.put("Contact", contact.getEditText().getText().toString());
                            appointment.put("Date", date.getEditText().getText().toString());
                            appointment.put("Time", time.getEditText().getText().toString());
                            appointment.put("Detail", detail.getEditText().getText().toString());
                            fStore.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Appointments").add(appointment)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Successfully made an appointment" , Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(BookAppointmentActivity.this, MyAppointmentActivity.class));

                                        } }) .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); }
                                    });
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), MainPage.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookAppointmentActivity.this, MyAppointmentActivity.class));
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


}