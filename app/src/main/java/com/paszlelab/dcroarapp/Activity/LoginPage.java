package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paszlelab.dcroarapp.R;

public class LoginPage extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPW;
    private Button btnLogin;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editLoginEmail);
        etPW = findViewById(R.id.editLoginPassword);
        btnLogin = findViewById(R.id.btnSignIn);
        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPW.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    firebaseUser = firebaseAuth.getCurrentUser();

                                    if(task.isSuccessful() && firebaseUser.isEmailVerified()){
                                        Toast.makeText(LoginPage.this, "Login successfully.", Toast.LENGTH_LONG).show();
                                        etEmail.setText("");
                                        etPW.setText("");
                                        startActivity(new Intent(LoginPage.this, HomePage.class));
                                    } else {
                                        try {
                                            Toast.makeText(LoginPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        } catch (Exception e){
                                            Toast.makeText(LoginPage.this, "Email not verified.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
            }
        });
    }
}