package com.example.oca;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Dashboard extends AppCompatActivity {
    FirebaseFirestore db;
    int BMI_Value;
    int sugar_level;
    String diabetes;
    boolean having_diabetes;
    String readings_for;
    int diastolic_bp, systolic_bp;
    ProgressBar prog;
    TextView bmi, bp, diabetes_tv;
    float bmi_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
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
                        bmi.setText("BMI:\nLow");
                    } else if (bmi_value < 16.9 && bmi_value > 16) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI:\nLow");
                    } else if (bmi_value < 18.4 && bmi_value > 17) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI:\nLow");
                    } else if (bmi_value < 24.9 && bmi_value > 18.5) {
                        bmi.setBackgroundColor(Color.rgb(67, 242, 51));
                        bmi.setText("BMI:\nNormal");
                    } else if (bmi_value < 29.9 && bmi_value > 25) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); // yellow
                        bmi.setText("BMI:\nHigh");
                    } else if (bmi_value < 34.9 && bmi_value > 30) {
                        bmi.setBackgroundColor(Color.rgb(245, 237, 95)); //yellow
                        bmi.setText("BMI:\nHigh");
                    } else {
                        bmi.setBackgroundColor(Color.rgb(242, 65, 65));
                        bmi.setText("BMI:\nHigh");
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
                    sugar_level = Integer.parseInt(documentSnapshot.getString("Reading"));
                    readings_for = documentSnapshot.getString("Readings_for");
                    diabetes = documentSnapshot.getString("Diabetes");
                    if (readings_for.contains("before")) {
                        readings_for = "before";
                    } else {
                        readings_for = "after";
                    }

                    if (diabetes.equals("yes")) {
                        if (readings_for.equals("before")) {
                            if (sugar_level < 7 && sugar_level > 4) {
                                having_diabetes = false;
                            } else {
                                having_diabetes = true;
                            }
                        } else {
                            if (sugar_level < 10 && sugar_level > 4) {
                                having_diabetes = false;
                            } else {
                                having_diabetes = true;
                            }
                        }
                    } else {
                        if (readings_for.equals("before")) {
                            if (sugar_level < 6 && sugar_level > 4) {
                                having_diabetes = false;
                            } else {
                                having_diabetes = true;
                            }
                        } else {
                            if (sugar_level < 7.8 && sugar_level > 4) {
                                having_diabetes = false;
                            } else {
                                having_diabetes = true;
                            }
                        }
                    }

                    if (having_diabetes == true) {
                        if (readings_for.equals("before")) {
                            if (sugar_level < 7 && sugar_level > 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(67, 242, 51));
                                diabetes_tv.setText("Diabetes\nNormal");
                            } else if (sugar_level < 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(245, 237, 95));
                                diabetes_tv.setText("Diabetes\nLow");
                            } else {
                                diabetes_tv.setBackgroundColor(Color.rgb(242, 65, 65));
                                diabetes_tv.setText("Diabetes\nHigh");
                            }
                        } else {
                            if (sugar_level < 10 && sugar_level > 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(67, 242, 51));
                                diabetes_tv.setText("Diabetes\nNormal");
                            } else if (sugar_level < 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(245, 237, 95));
                                diabetes_tv.setText("Diabetes\nLow");
                            } else {
                                diabetes_tv.setBackgroundColor(Color.rgb(242, 65, 65));
                                diabetes_tv.setText("Diabetes\nHigh");
                            }
                        }


                    } else {

                        if (readings_for.equals("before")) {
                            if (sugar_level < 6 && sugar_level > 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(67, 242, 51));
                                diabetes_tv.setText("Diabetes\nNormal");
                            } else if (sugar_level < 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(245, 237, 95));
                                diabetes_tv.setText("Diabetes\nLow");
                            } else {
                                diabetes_tv.setBackgroundColor(Color.rgb(242, 65, 65));
                                diabetes_tv.setText("Diabetes\nHigh");
                            }
                        } else {
                            if (sugar_level < 7.8 && sugar_level > 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(67, 242, 51));
                                diabetes_tv.setText("Diabetes\nNormal");
                            } else if (sugar_level < 4) {
                                diabetes_tv.setBackgroundColor(Color.rgb(245, 237, 95));
                                diabetes_tv.setText("Diabetes\nLow");
                            } else {
                                diabetes_tv.setBackgroundColor(Color.rgb(242, 65, 65));
                                diabetes_tv.setText("Diabetes\nHigh");
                            }
                        }


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


    }
}