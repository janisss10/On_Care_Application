package com.example.oca;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainPage extends AppCompatActivity {
    CardView journalCard, exerciseCard, testResultCard, appointmentCard, chatbotCard, profileCard, newsCard, healthCard;
    TextView dashboard;
    int sugar_level;
    String diabetes;
    boolean having_diabetes;
    String diab_result;
    int diastolic_bp, systolic_bp;
    ProgressBar prog;
    TextView bmi, bp, diabetes_tv;
    float bmi_value;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        journalCard = (CardView) findViewById(R.id.JournalCard);
        exerciseCard = (CardView) findViewById(R.id.ExerciseCard);
        testResultCard = (CardView) findViewById(R.id.TestResultCard);
        appointmentCard = (CardView) findViewById(R.id.AppointmentCard);
        chatbotCard = (CardView) findViewById(R.id.ChatbotCard);
        profileCard = (CardView) findViewById(R.id.ProfileCard);
        newsCard = (CardView) findViewById(R.id.NewsCard);
        healthCard = (CardView) findViewById(R.id.HealthCard);
        prog = findViewById(R.id.dashboard_prog);
        bmi = findViewById(R.id.bmi);
        bp = findViewById(R.id.bp);
        diabetes_tv = findViewById(R.id.diabetes);
        db = FirebaseFirestore.getInstance();
        CollectionReference bmiRecordsRef = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("BMI_Records");
        Query query = bmiRecordsRef.orderBy("Date", Query.Direction.DESCENDING).limit(1);
        query = query.limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    bmi_value = Float.parseFloat(documentSnapshot.getString("BMI_value"));

                    if (bmi_value < 16) {
                        bmi.setBackgroundColor(Color.rgb(242, 65, 65));
                        bmi.setText("BMI\nLow");
                    } else if (bmi_value < 16.9 && bmi_value > 16) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI\nLow");
                    } else if (bmi_value < 18.4 && bmi_value > 17) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI\nLow");
                    } else if (bmi_value < 24.9 && bmi_value > 18.5) {
                        bmi.setBackgroundColor(Color.rgb(67, 242, 51));
                        bmi.setText("BMI\nNormal");
                    } else if (bmi_value < 29.9 && bmi_value > 25) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI\nHigh");
                    } else if (bmi_value < 34.9 && bmi_value > 30) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); //yellow
                        bmi.setText("BMI\nHigh");
                    } else {
                        bmi.setBackgroundColor(Color.rgb(242, 65, 65));
                        bmi.setText("BMI\nHigh");
                    }

                }
                prog.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });

        CollectionReference bpCollectionRef = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("BP_Records");
        query = bpCollectionRef.orderBy("Date", Query.Direction.DESCENDING).limit(1);
        query = query.limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    diastolic_bp = Math.toIntExact(documentSnapshot.getLong("Diastolic_BP"));
                    systolic_bp = Math.toIntExact(documentSnapshot.getLong("Systolic_BP"));

                    if (systolic_bp < 90 || diastolic_bp < 60) {
                        bp.setBackgroundColor(Color.rgb(245, 237, 95));
                        bp.setText("Blood Pressure\nLow");

                    } else if (systolic_bp > 121 || diastolic_bp > 80) {
                        bp.setBackgroundColor(Color.rgb(242, 65, 65));
                        bp.setText("Blood Pressure\nHigh");
                    } else {
                        bp.setBackgroundColor(Color.rgb(67, 242, 51));
                        bp.setText("Blood Pressure\nNormal");
                    }
                }
                prog.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });

        CollectionReference diabatesRecordsRef = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Diabetes_Records");
        query = diabatesRecordsRef.orderBy("Date", Query.Direction.DESCENDING).limit(1);
        query = query.limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                    diab_result = documentSnapshot.getString("Result");
                    if (diab_result.equals("high")) {
                        diabetes_tv.setText("Diabetes\nHigh");
                        diabetes_tv.setBackgroundColor(Color.rgb(242, 65, 65));
                    } else if (diab_result.equals("low")) {
                        diabetes_tv.setText("Diabetes\nLow");
                        diabetes_tv.setBackgroundColor(Color.rgb(245, 237, 95));
                    } else {
                        diabetes_tv.setText("Diabetes\nNormal");
                        diabetes_tv.setBackgroundColor(Color.rgb(67, 242, 51));
                    }
                }
                prog.setVisibility(View.GONE);
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });

        journalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Journal.class);
                startActivity(intent);
            }
        });

        exerciseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Food.class);
                startActivity(intent);
            }
        });

        testResultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, MedicalRecordPassword.class);
                startActivity(intent);
            }
        });

        appointmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, BookAppointmentActivity.class);
                startActivity(intent);
            }
        });
        newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, ActivityNews.class);
                startActivity(intent);

            }
        });

        chatbotCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Chatbot.class);
                startActivity(intent);
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, CreateHealthActivity.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
            builder.setMessage("Do you want to exit?");
            builder.setCancelable(true);

            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainPage.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return true;
    }
}
