package com.example.oca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputLayout Email_Input, Password;
    Button login, Signup;
    ImageView logo;
    TextView loginQuestion, RegisterNow, forgetPassword;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logo = findViewById(R.id.logo);
        Email_Input = findViewById(R.id.Email_Input);
        Password = findViewById(R.id.password);
        forgetPassword = findViewById(R.id.forgetPassword);
        login = findViewById(R.id.Signin);
        loginQuestion = findViewById(R.id.LoginPageQuestion);
        RegisterNow = findViewById(R.id.Register);
        //Signup = findViewById(R.id.Signin);

        Auth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = Password.getEditText().getText().toString();
                String email = Email_Input.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Email_Input.setError("Please enter your email!");
                    Email_Input.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Password.setError("Please enter your password!");
                    Password.requestFocus();
                    return;
                }

                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Login.this, MainPage.class);
                            startActivity(intent);
                        } else {
                            //If sign in fails, display a message to the user
                            Toast.makeText(Login.this, "Authentication failed. Please try again " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        RegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link sent to your email! ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Reset link not send " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialogue
                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in
        FirebaseUser currentUser = Auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
        }
    }
}
