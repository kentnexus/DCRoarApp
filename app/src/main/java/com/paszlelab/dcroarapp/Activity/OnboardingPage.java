package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.paszlelab.dcroarapp.R;

import android.os.Handler;
import android.view.View;

public class OnboardingPage extends AppCompatActivity {

    String prevStarted = "yes";

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
//        if (!sharedpreferences.getBoolean(prevStarted, false)) {
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putBoolean(prevStarted, Boolean.TRUE);
//            editor.apply();
//        } else {
//            goToLogin();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_onboarding);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Handler handler = new Handler();

        if(firebaseUser != null && firebaseUser.isEmailVerified()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(OnboardingPage.this, MainPage.class));
                    OnboardingPage.this.finish();
                }
            },2000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(OnboardingPage.this, InitialPage.class));
                    OnboardingPage.this.finish();
                }
            },2000);
        }
    }

    public void goToInitPage(View view){
    }
}