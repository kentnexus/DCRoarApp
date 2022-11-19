package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.paszlelab.dcroarapp.R;

public class TermsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_terms_page);
    }

    public void goBack(View v){
        TermsPage.this.finish();
    }

    public void goToRegistration(View v){

        startActivityForResult(new Intent(this, RegistrationPage.class),1);
    }
}