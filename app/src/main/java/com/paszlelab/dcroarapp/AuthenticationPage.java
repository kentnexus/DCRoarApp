package com.paszlelab.dcroarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class AuthenticationPage extends AppCompatActivity {

    private EditText etValidEmail;
    private Button btnGenerate;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean match = validEmail(AuthenticationPage.this);


                if(match == true){
                    firebaseAuth = FirebaseAuth.getInstance();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(firebaseAuth.getCurrentUser() != null){
                                Intent intent = new Intent(AuthenticationPage.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(AuthenticationPage.this, InitialPage.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, 4000);
                }
            }
        });
    }

    public boolean validEmail(Context context){

        etValidEmail = findViewById(R.id.editEmailAddress);

        String email = etValidEmail.getText().toString();
        String pattern = ".*@student.douglascollege.ca";
        boolean match = Pattern.matches(pattern, email);

        return match;
    }
}