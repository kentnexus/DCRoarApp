package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paszlelab.dcroarapp.R;

public class InitialPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_initial);

    }

    public void goToTermsPage(View v){
        startActivity(new Intent(this, TermsPage.class));
    }

    public void goToLogin(View v){
        startActivity(new Intent(this, LoginPage.class));
    }
}