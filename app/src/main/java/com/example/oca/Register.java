package com.example.oca;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    TextView back, loginButton;
    Button Register;
    ProgressBar progressBar;
    FirebaseAuth Auth;
    FirebaseFirestore fStore;
    ImageView profileImage;
    Button selectImageButton;
    private TextInputLayout fullNameLayout, emailLayout, contactLayout, dobLayout, genLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText editEmail, editContact, editDob, editGender, editPassword, editConfirmPassword;
    private Uri selectedImage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImageToFirebaseStorage(String userID) {
        if (selectedImage != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileImageRef = storageRef.child("profile_images/" + userID + ".jpg");

            profileImageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String profileImageUrl = uri.toString();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            documentReference.update("ProfileImageUrl", profileImageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Profile Image URL Saved Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to Save Profile Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("RegisterActivity", "Failed to Save Profile Image URL", e);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to Upload Profile Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("RegisterActivity", "Failed to Upload Profile Image", e);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to Upload Profile Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("RegisterActivity", "Failed to Upload Profile Image", e);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Profile Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fStore = FirebaseFirestore.getInstance();
        profileImage = findViewById(R.id.profile);
        selectImageButton = findViewById(R.id.select_image_button);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);
            }
        });
        profileImage = findViewById(R.id.profile);
        selectImageButton = findViewById(R.id.select_image_button);

        loginButton = findViewById(R.id.logIn);
        fullNameLayout = findViewById(R.id.fullname);
        emailLayout = findViewById(R.id.email);
        contactLayout = findViewById(R.id.contact);
        dobLayout = findViewById(R.id.dob);
        passwordLayout = findViewById(R.id.password);
        confirmPasswordLayout = findViewById(R.id.cpassword);
        genLayout = findViewById(R.id.gender);
        editEmail = findViewById(R.id.email_edit_text);
        editContact = findViewById(R.id.contact_edit_text);
        editDob = findViewById(R.id.dob_edit_text);
        editGender = findViewById(R.id.gender_edit_text);
        editPassword = findViewById(R.id.password_edit_text);
        editConfirmPassword = findViewById(R.id.confirm_password_edit_text);
        Register = findViewById(R.id.Signup);
        progressBar = findViewById(R.id.progressBar2);
        back = findViewById(R.id.back);
        Auth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = fullNameLayout.getEditText().getText().toString();
                String pass = editPassword.getText().toString();
                String confirmPassword = editConfirmPassword.getText().toString();
                String e = editEmail.getText().toString();
                String date = editDob.getText().toString();
                String con = editContact.getText().toString();
                String gend = editGender.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(Name)) {
                    fullNameLayout.setError("Please enter your name!");
                    fullNameLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(e)) {
                    emailLayout.setError("Please enter your email!");
                    emailLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    emailLayout.setError("Please provide valid email");
                    emailLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(con)) {
                    contactLayout.setError("Please enter your contact no!");
                    contactLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    dobLayout.setError("Please enter your date of birth!");
                    dobLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(gend)) {
                    genLayout.setError("Please enter your gender!");
                    genLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    passwordLayout.setError("Please enter your password!");
                    passwordLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (pass.length() < 6) {
                    passwordLayout.setError("Min password length should be 7 characters!");
                    passwordLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    confirmPasswordLayout.setError("Please confirm your password!");
                    confirmPasswordLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!passwordLayout.getEditText().getText().toString().equals(confirmPassword)) {
                    confirmPasswordLayout.setError("Different password entered");
                    confirmPasswordLayout.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                Auth.createUserWithEmailAndPassword(e, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            String userID = Auth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email", e);
                            user.put("Password", pass);
                            user.put("Fullname", Name);
                            user.put("Contact", con);
                            user.put("DoB", date);
                            user.put("Gender", gend);
                            user.put("Registered Date", new Date());

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadProfileImageToFirebaseStorage(userID);
                                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();

                                    Intent myintent = new Intent(getApplicationContext(), MainPage.class);
                                    startActivity(myintent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "failure" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });


    }


}