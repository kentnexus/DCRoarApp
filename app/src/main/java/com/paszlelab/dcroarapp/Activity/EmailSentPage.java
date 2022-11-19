package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.paszlelab.dcroarapp.R;

public class EmailSentPage extends AppCompatActivity {

    private TextView tEmailSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_email_sent_page);

        tEmailSent = findViewById(R.id.textEmailSent);
        Intent intent = getIntent();
        String email = intent.getStringExtra("emailAdd");
        tEmailSent.setText(getString(R.string.checkemail, email));

    }

    public void goToInitPage(View v){

        startActivity(new Intent(this, InitialPage.class));
        this.finishAffinity();
    }
}