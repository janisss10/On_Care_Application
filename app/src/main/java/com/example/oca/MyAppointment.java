package com.example.oca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyAppointment extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ListView listOfAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listOfAppointment = findViewById(R.id.listofAppointment);


        fStore.collectionGroup("Appointment")
                .whereEqualTo("Email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<HashMap<String, Object>> app = new ArrayList<HashMap<String, Object>>();

                            for (final QueryDocumentSnapshot document : task.getResult()) {


                                HashMap<String, Object> myAppointment = new HashMap<String, Object>();

                                myAppointment.put("Clinic", document.getData().get("Clinic Name").toString());
                                myAppointment.put("Date", document.getData().get("Date").toString());
                                myAppointment.put("Time", document.getData().get("Time").toString());
                                myAppointment.put("Detail", document.getData().get("Detail").toString());
                                myAppointment.put("docRef", document.getReference());

                                app.add(myAppointment);

                            }


                            final SimpleAdapter adapter = new SimpleAdapter(MyAppointment.this, app, R.layout.appointmentview,
                                    new String[]{"Clinic", "Date", "Time", "Detail", "delete"},
                                    new int[]{R.id.myclinic, R.id.mydate, R.id.mytime, R.id.mydetail, R.id.mydelete}) {
                                @Override
                                public View getView(final int position, View convertView, final ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);

                                    Button b = (Button) v.findViewById(R.id.mydelete);
                                    b.setOnClickListener(new android.view.View.OnClickListener() {

                                        @Override
                                        public void onClick(View arg0) {
                                            // TODO Auto-generated method stub
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(MyAppointment.this);
                                            dialog.setTitle("Are you sure ?");
                                            dialog.setMessage("Deleting this appoint will not be reversed, you will need to book again");
                                            dialog.setPositiveButton("Delete Appointment", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    DocumentReference docref = (DocumentReference) app.get(position).get("docRef");
                                                    docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getBaseContext(), "Successfully deleted the appointment!", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(MyAppointment.this, MyAppointment.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getBaseContext(), "Error retrieving my comments", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
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
                                    return v;
                                }
                            };

                            listOfAppointment.setAdapter(adapter);



                        } else {
                            Log.i("MyComment", "Error: " + task.getException());
                            Toast.makeText(getBaseContext(), "Error retrieving my comments", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void clickCancel2(View v) {
// Go to the previous page
        onBackPressed();
    }
}
