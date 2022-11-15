package com.paszlelab.dcroarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InitialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
    }

    public void goToSignUp(View v){
        Intent intent = new Intent(this,AuthenticationPage.class);
        startActivity(intent);
    }
}