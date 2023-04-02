package com.example.oca;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    String userId;
    private ImageView profileImage;
    private Button selectImageButton, editButton;
    private TextInputEditText editFullName, editEmail, editContact, editDob, editGender, editPassword, password;
    private TextInputLayout fullNameLayout, emailLayout, contactLayout, dobLayout, genderLayout, passwordLayout, confirmPasswordLayout;
    private Button signUpButton, changePassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri;

    private void loadUserData() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Fullname");
                        String email = document.getString("Email");
                        String gender = document.getString("Gender");
                        String contact = document.getString("Contact");
                        String dob = document.getString("DoB");
                        String imageUrl = document.getString("ProfileImageUrl");
                        // Load data into input fields
                        editFullName.setText(name);
                        editEmail.setText(email);
                        editGender.setText(gender);
                        editContact.setText(contact);
                        editDob.setText(dob);
                        // Load profile image
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get().load(imageUrl).placeholder(R.drawable.profile).into(profileImage);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    private boolean validateUserData() {
        boolean isValid = true;

        if (editFullName.getText().toString().trim().isEmpty()) {
            fullNameLayout.setError("Full name is required");
            isValid = false;
        } else {
            fullNameLayout.setError(null);
        }

        if (editEmail.getText().toString().trim().isEmpty()) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        if (editContact.getText().toString().trim().isEmpty()) {
            contactLayout.setError("Contact is required");
            isValid = false;
        } else {
            contactLayout.setError(null);
        }

        if (editDob.getText().toString().trim().isEmpty()) {
            dobLayout.setError("Date of Birth is required");
            isValid = false;
        } else {
            dobLayout.setError(null);
        }
        if (password.getText().toString().trim().isEmpty()) {
            dobLayout.setError("Password required");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }
        if (editGender.getText().toString().trim().isEmpty()) {
            genderLayout.setError("Gender is required");
            isValid = false;
        } else {
            genderLayout.setError(null);
        }
        return isValid;
    }

    private void updateUserData() {
        progressBar.setVisibility(View.VISIBLE);

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(userId + ".jpg");

            UploadTask uploadTask = fileReference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        updateUserDocument(downloadUri.toString());
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProfileActivity.this, "Failed to upload profile image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            updateUserDocument(null);
        }
    }

    private void updateUserDocument(String imageUrl) {
        DocumentReference documentReference = fStore.collection("Users").document(userId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("Fullname", editFullName.getText().toString());
        updates.put("Email", editEmail.getText().toString());
        updates.put("Contact", editContact.getText().toString());
        updates.put("DoB", editDob.getText().toString());
        updates.put("Gender", editGender.getText().toString());
        if (!editEmail.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), password.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updateEmail(editEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Email address updated successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, "Failed to update email address: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("email error", task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProfileActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("auth error", task.getException().getMessage());
                        }
                    }
                });
            }
        }
        if (imageUrl != null) {
            updates.put("ProfileImageUrl", imageUrl);
        }
        documentReference.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI elements
        profileImage = findViewById(R.id.profile);
        selectImageButton = findViewById(R.id.select_image_button);
        editFullName = findViewById(R.id.fullname_edit_text);
        editEmail = findViewById(R.id.email_edit_text);
        editContact = findViewById(R.id.contact_edit_text);
        editDob = findViewById(R.id.dob_edit_text);
        editGender = findViewById(R.id.gender_edit_text);
        editPassword = findViewById(R.id.password_edit_text);
        selectImageButton = findViewById(R.id.select_image_button);
        password = findViewById(R.id.password_edit_text);
        editButton = findViewById(R.id.update);
        changePassword = findViewById(R.id.change_password);
        progressBar = findViewById(R.id.progressBar2);
        fullNameLayout = findViewById(R.id.fullname);
        emailLayout = findViewById(R.id.email);
        contactLayout = findViewById(R.id.contact);
        dobLayout = findViewById(R.id.dob);
        genderLayout = findViewById(R.id.gender);
        signUpButton = (Button) findViewById(R.id.update);
        passwordLayout = findViewById(R.id.password);
        confirmPasswordLayout = findViewById(R.id.cpassword);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        userId = mAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        // Load user data
        loadUserData();

        // Update user data
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        // Add listeners for buttons
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle image selection
                openFileChooser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign up process
                if (validateUserData()) {
                    updateUserData();
                }
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle log in process
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to receive reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email and send email link

                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Reset link sent to your email! ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error! Reset link not send " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.show(); //add this line
            }
        });

    }

}

// Add other required methods
