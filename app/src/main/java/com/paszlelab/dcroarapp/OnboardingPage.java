package com.paszlelab.dcroarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnboardingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }

    public void goToLogin(View v){
        Intent intent = new Intent(this, InitialPage.class);
        startActivity(intent);
    }
}