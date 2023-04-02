package com.example.oca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateHealthActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    Button create;
    TextView note;
    TextInputLayout cblood, cdiabeteshist, current_diabetes, cstagediabetes, chbphist, cstagehbp, callerg, csleep, Rbmi;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_health);
        note=findViewById(R.id.note);
        create = (Button) findViewById(R.id.createhealth);
        cblood = (TextInputLayout) findViewById(R.id.cblood);
        cdiabeteshist = (TextInputLayout) findViewById(R.id.cdiabeteshist);
        current_diabetes = (TextInputLayout) findViewById(R.id.current_diabetes);
        cstagediabetes = (TextInputLayout) findViewById(R.id.cstagediabetes);
        chbphist = (TextInputLayout) findViewById(R.id.chbphist);
        cstagehbp = (TextInputLayout) findViewById(R.id.cstagehbp);
        //cdia = (TextInputLayout) findViewById(R.id.cdiagnosis);
        //cmed = (TextInputLayout) findViewById(R.id.cmed);
        callerg = (TextInputLayout) findViewById(R.id.callergies);
        csleep = (TextInputLayout) findViewById(R.id.csleep);

        // for thr first time with no entry
        CollectionReference documentReference = fStore.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Health_Data");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_LONG).show();
                    } else {
                        create.setVisibility(View.GONE);
                        note.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the data from the document and assign it to the relevant fields
                            String bloodType = document.getString("Blood");
                            String diabetesHistory = document.getString("Diabetes History");
                            String currentDiabetes = document.getString("Current Diabetes");
                            String diabetesStage = document.getString("Diabetes Stage");
                            String hbpHistory = document.getString("High Blood Pressure History");
                            String hbpStage = document.getString("High Blood Pressure Stage");
                            //String diagnosis = document.getString("Diagnosis");
                            //String medication = document.getString("Medication");
                            String allergies = document.getString("Allergies");
                            String sleepHours = document.getString("Sleep Hours");

                            // Set the text of the relevant TextViews or EditTexts to the retrieved data
                            cblood.getEditText().setText(bloodType);
                            cdiabeteshist.getEditText().setText(diabetesHistory);
                            current_diabetes.getEditText().setText(currentDiabetes);
                            cstagediabetes.getEditText().setText(diabetesStage);
                            chbphist.getEditText().setText(hbpHistory);
                            cstagehbp.getEditText().setText(hbpStage);
                            //cdia.getEditText().setText(diagnosis);
                            //cmed.getEditText().setText(medication);
                            callerg.getEditText().setText(allergies);
                            csleep.getEditText().setText(sleepHours);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }

        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userBlood = cblood.getEditText().getText().toString().toUpperCase();
                final String userhistdiabetes = cdiabeteshist.getEditText().getText().toString();
                final String current_Diabetes = current_diabetes.getEditText().getText().toString();
                final String userstagediabetes = cstagediabetes.getEditText().getText().toString();
                final String userhisthbp = chbphist.getEditText().getText().toString();
                final String userstagehbp = cstagehbp.getEditText().getText().toString();
                //final String userDia = cdia.getEditText().getText().toString();
                //final String userMed = cmed.getEditText().getText().toString();
                final String userAllerg = callerg.getEditText().getText().toString();
                final String userSleep = csleep.getEditText().getText().toString();

                String doc_id = fStore.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Health_Data").document().getId();
                DocumentReference documentReference = fStore.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Health_Data").document(doc_id);
                //firebase
                Map<String, Object> health = new HashMap<>();
                health.put("Blood", userBlood + "ve");
                health.put("Diabetes History", userhistdiabetes);
                health.put("Current Diabetes", current_Diabetes);
                health.put("Diabetes Stage", userstagediabetes);
                health.put("High Blood Pressure History", userhisthbp);
                health.put("High Blood Pressure Stage", userstagehbp);
                //health.put("Diagnosis", userDia);
                //health.put("Medication", userMed);
                health.put("Allergies", userAllerg);
                health.put("Sleep Hours", userSleep + " hours");
                health.put("Created Date", new Date());
                documentReference.set(health).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Created My health!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateHealthActivity.this, MainPage.class));
                        finish();
                    }
                });
            }
        });
    }
}