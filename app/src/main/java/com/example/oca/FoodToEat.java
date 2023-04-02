package com.example.oca;

import static com.example.oca.CreateHealthActivity.TAG;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodToEat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodToEat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CardView bmi_card, bp_card, diabetes_card;
    public  String bmicomment = "";
    public String diabetesComment = "";
    public String bpComment = "";
    ArrayList<FoodItem> foodItemsbmi = new ArrayList<>();
    ArrayList<FoodItem> foodItemsdiabetes = new ArrayList<>();
    ArrayList<FoodItem> foodItemsbp = new ArrayList<>();
    ProgressBar prog;
    TextView section_bmi, section_diabetes, section_bp;
    CollectionReference foodCollectionRef, diabetesCollectionRef, bpCollectionRef;
    FoodItemAdapter adapter_bmi, adapter_diabetes, bp_adapter;
    RecyclerView recyclerview_bmi, recyclerview_diabetes, recyclerview_bp;
    TextView food_to_eat_bmi, food_to_eat_diabetes, food_to_eat_bp;
    String readings_for, diabetes;
    float sugar_level;
    int diastolic_bp, systolic_bp;
    FirebaseFirestore db;
    DocumentReference fooddocumentRef, diabetesDocumentRef, bpDocumentRef;
    boolean having_diabetes;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodToEat() {
        // Required empty public constructor
    }
    public static FoodToEat newInstance() {
        return new FoodToEat();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodToEat.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodToEat newInstance(String param1, String param2) {
        FoodToEat fragment = new FoodToEat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Toast.makeText(getContext(), "food to eat", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_to_eat, container, false);
        prog = view.findViewById(R.id.progbar);
        // Set the fragment title
        getActivity().setTitle("Foods to Eat");
        food_to_eat_bmi = view.findViewById(R.id.food_to_have_bmi);
        recyclerview_bmi = view.findViewById(R.id.food_to_eat_recycler_view_bmi);
        recyclerview_bmi.setLayoutManager(new LinearLayoutManager(getActivity()));
        bmi_card=view.findViewById(R.id.card_bmi);

        food_to_eat_diabetes = view.findViewById(R.id.food_to_have_diabetes);
        recyclerview_diabetes = view.findViewById(R.id.food_to_eat_recycler_view_diabetes);
        recyclerview_diabetes.setLayoutManager(new LinearLayoutManager(getActivity()));
        diabetes_card=view.findViewById(R.id.card_diabetes);

        food_to_eat_bp = view.findViewById(R.id.food_to_have_bp);
        recyclerview_bp = view.findViewById(R.id.food_to_eat_recycler_view_bp);
        recyclerview_bp.setLayoutManager(new LinearLayoutManager(getActivity()));
        bp_card=view.findViewById(R.id.card_bp);

        section_bmi = view.findViewById(R.id.section_bmi);
        section_diabetes = view.findViewById(R.id.section_diabetes);
        section_bp = view.findViewById(R.id.section_bp);
        db = FirebaseFirestore.getInstance();

        CollectionReference bmiRecordsRef = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("BMI_Records");
        Query query = bmiRecordsRef.orderBy("Date", Query.Direction.DESCENDING).limit(1);
        query = query.limit(1);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    bmicomment = documentSnapshot.getString("BMI_comment");
                    if (bmicomment.equals("high") || bmicomment.equals("low")) {
                        section_bmi.setVisibility(View.VISIBLE);
                        bmi_card.setVisibility(View.VISIBLE);
                        recyclerview_bmi.setVisibility(View.VISIBLE);
                        loadBMIData();
                    }
                } else {
                    // No documents found
                }
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
                        section_diabetes.setVisibility(View.VISIBLE);
                        diabetes_card.setVisibility(View.VISIBLE);
                        recyclerview_diabetes.setVisibility(View.VISIBLE);
                        loadDiabetesData();
                    }

                } else {
                    // No documents found
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });

        bpCollectionRef = db.collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("BP_Records");
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
                        bpComment = "Low";
                        section_bp.setVisibility(View.VISIBLE);
                        bp_card.setVisibility(View.VISIBLE);
                        recyclerview_bp.setVisibility(View.VISIBLE);
                        loadBPData();
                    } else if (systolic_bp > 121 ||  diastolic_bp > 81) {

                        recyclerview_bp.setVisibility(View.VISIBLE);
                        bp_card.setVisibility(View.VISIBLE);
                        bpComment = "High";
                        section_bp.setVisibility(View.VISIBLE);
                        loadBPData();
                    }


                } else {
                    // No documents found
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });
        prog.setVisibility(View.GONE);
        return view;
    }

    private void loadBPData() {
        if (bpComment.equals("High")) {
            bpDocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("Hypertension").document("rfMc9JWJlGXEnNX0sec3").collection("Description_Eat").document("H0t4cLerndYRSL3hvoFG");

            bpCollectionRef = db.collection("Food/l1tXxkP3vjDE3Ax29TJd/Hypertension/rfMc9JWJlGXEnNX0sec3/Food_Recepies");
            section_bp.setText("For high Blood Pressure");

        } else if (bpComment.equals("Low")) {
           section_bp.setText("For low Blood Pressure");
            bpDocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("Hypotension")
                    .document("836DvQP8bKeNBjsTzwsU").collection("Description_Eat")
                    .document("oSpwTk7UZ169UybVdei6");
            bpCollectionRef = db.collection("Food/l1tXxkP3vjDE3Ax29TJd/Hypotension/836DvQP8bKeNBjsTzwsU/Food_Recepies");

        }

        bpDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String description = document.getString("description");
                        String[] bulletPoints = description.split("\\.");
                        String bulletPointText = "";

                        for (String point : bulletPoints) {
                            bulletPointText += "\u2022 " + point.trim() + "\n";
                        }
                        prog.setVisibility(View.GONE);
                        food_to_eat_bp.setText(bulletPointText);

                    }
                }
            }
        });

        if (bpCollectionRef != null) {
            bpCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        prog.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract data from the document
                            String title = document.getString("title");
                            String ingredients = document.getString("ingredients");
                            String directions = document.getString("directions");
                            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Food_Images/" + title + ".jpg");

                            // Get the downloadable URL for the image
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String link = uri.toString();

                                    // Create a FoodItem object with the extracted data and image link
                                    FoodItem foodItem = new FoodItem(title, link, ingredients, directions);

                                    // Add the FoodItem object to your list of items
                                    foodItemsbp.add(foodItem);

                                    // Notify the adapter that the data set has changed
                                    bp_adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occur while getting the URL
                                    Log.d("link failed", e.getMessage());
                                }
                            });
                        }

                        // Create and set the adapter for your RecyclerView
                        bp_adapter = new FoodItemAdapter(foodItemsbp);
                        recyclerview_bp.setAdapter(bp_adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }

            });
        }
    }

    private void loadDiabetesData() {

        if (having_diabetes) {
            if (readings_for.equals("before")) {
                if (sugar_level < 7 && sugar_level > 4) {
                    diabetesComment = "Good";
                } else if (sugar_level < 4) {
                    diabetesComment = "Low";
                } else {
                    diabetesComment = "High";
                }
            } else {
                if (sugar_level < 10 && sugar_level > 4) {
                    diabetesComment = "Good";
                } else if (sugar_level < 4) {
                    diabetesComment = "Low";
                } else {
                    diabetesComment = "High";
                }
            }

            if (diabetesComment.equals("High")) {
                section_diabetes.setText("For High Diabetes");
                diabetesDocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("Diabetes_High")
                        .document("BD7oRrXX4yDOksBrrGoq").collection("Description_Eat").document("MHyNhftdAlUQcEI7xBcp");

                diabetesCollectionRef = db.collection("Food/l1tXxkP3vjDE3Ax29TJd/Diabetes_High/BD7oRrXX4yDOksBrrGoq/Food_Recepies");

            } else {

                section_diabetes.setText("For Low Diabetes");
                diabetesDocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("Diabetes_Low")
                        .document("dDLGJIjT6rSSaTux5ZmB").collection("Description_Eat").document("iHQSjcZmZLC9mhaFLMdG");

            }
            diabetesDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String description = document.getString("description");
                            String[] bulletPoints = description.split("\\.");
                            String bulletPointText = "";

                            for (String point : bulletPoints) {
                                bulletPointText += "\u2022 " + point.trim() + "\n";
                            }

                            prog.setVisibility(View.GONE);
                            food_to_eat_diabetes.setText(bulletPointText);
                        }
                    }
                }
            });

            if (diabetesCollectionRef != null) {
                diabetesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data from the document
                                String title = document.getString("title");
                                String ingredients = document.getString("ingredients");
                                String directions = document.getString("directions");
                                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Food_Images/" + title + ".jpg");

                                // Get the downloadable URL for the image
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String link = uri.toString();

                                        // Create a FoodItem object with the extracted data and image link
                                        FoodItem foodItem = new FoodItem(title, link, ingredients, directions);

                                        // Add the FoodItem object to your list of items
                                        foodItemsbmi.add(foodItem);

                                        // Notify the adapter that the data set has changed
                                        adapter_diabetes.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle any errors that occur while getting the URL
                                        Log.d("link failed", e.getMessage());
                                    }
                                });
                            }

                            // Create and set the adapter for your RecyclerView
                            adapter_diabetes = new FoodItemAdapter(foodItemsbmi);
                            recyclerview_diabetes.setAdapter(adapter_diabetes);
                            prog.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        }
    }

    private void loadBMIData() {

        if (bmicomment.equals("high")) {
            fooddocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("BMI_High").document("TOzhskePro5juj69XyZH").collection("Description_Eat").document("8NjqgbSGNbS85iZR0TXn");

            foodCollectionRef = db.collection("Food/l1tXxkP3vjDE3Ax29TJd/BMI_High/TOzhskePro5juj69XyZH/Food_Recepies");
            section_bmi.setText("For high BMI");
        } else if (bmicomment.equals("low")) {
            section_bmi.setText("For low BMI");
            fooddocumentRef = db.collection("Food").document("l1tXxkP3vjDE3Ax29TJd").collection("BMI_Low").document(
                    "S5tv8UTtDmbYJbRzQgfE").collection("Description_Eat").document(
                    "IfmMDsdqCron3On5Vvmg");
        }

        fooddocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String description = document.getString("description");
                        String[] bulletPoints = description.split("\\.");
                        String bulletPointText = "";

                        for (String point : bulletPoints) {
                            bulletPointText += "\u2022 " + point.trim() + "\n";
                        }
                        prog.setVisibility(View.GONE);
                        food_to_eat_bmi.setText(bulletPointText);

                    }
                }
            }
        });

        if (foodCollectionRef != null) {
            foodCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        prog.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract data from the document
                            String title = document.getString("title");
                            String ingredients = document.getString("ingredients");
                            String directions = document.getString("directions");
                            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Food_Images/" + title + ".jpg");

                            // Get the downloadable URL for the image
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String link = uri.toString();

                                    // Create a FoodItem object with the extracted data and image link
                                    FoodItem foodItem = new FoodItem(title, link, ingredients, directions);

                                    // Add the FoodItem object to your list of items
                                    foodItemsdiabetes.add(foodItem);

                                    // Notify the adapter that the data set has changed
                                    adapter_bmi.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle any errors that occur while getting the URL
                                    Log.d("link failed", e.getMessage());
                                }
                            });
                        }

                        // Create and set the adapter for your RecyclerView
                        adapter_bmi = new FoodItemAdapter(foodItemsdiabetes);
                        recyclerview_bmi.setAdapter(adapter_bmi);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }
    }

}