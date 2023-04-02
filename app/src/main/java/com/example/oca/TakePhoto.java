package com.example.oca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TakePhoto extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgView;
    private Button uploadBtn;
    private Uri imageUri;

    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        // Initialize Firebase storage reference
        storageRef = FirebaseStorage.getInstance().getReference();

        // Get references to views
        imgView = findViewById(R.id.img_view);
        uploadBtn = findViewById(R.id.img_upload);

        // Set on click listener for image view
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch image picker intent
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        // Set on click listener for upload button
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if image is selected
                if (imageUri != null) {
                    // Generate a unique filename for the image
                    String filename = UUID.randomUUID().toString();
                    final StorageReference imageRef = storageRef.child("medical_records/" + filename);

                    // Upload image to Firebase storage
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image upload successful
                                    Toast.makeText(TakePhoto.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                    // Get download URL of uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Upload download URL to Firebase database
                                            String id = FirebaseFirestore.getInstance().collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Medical_Record_Images").document().getId();
                                            Map<String, Object> record = new HashMap<>();
                                            record.put("URL", uri.toString());
                                            FirebaseFirestore.getInstance().collection("Patients").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Medical_Record_Images").document(id).set(record);
                                            // Finish activity
                                            finish();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Image upload failed
                                    Toast.makeText(TakePhoto.this, "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Image not selected
                    Toast.makeText(TakePhoto.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Handle result of image picker intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Image selected successfully
            imageUri = data.getData();
            imgView.setImageURI(imageUri);
        }
    }
}
