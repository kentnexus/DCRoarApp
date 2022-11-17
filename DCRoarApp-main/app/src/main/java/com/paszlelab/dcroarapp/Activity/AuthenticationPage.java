package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.paszlelab.dcroarapp.Fragments.DatePickerFragment;
import com.paszlelab.dcroarapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class AuthenticationPage extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private EditText etFName;
    private EditText etLName;
    private EditText etEmail;
    private EditText etPW;
    private Date bday;
    private Spinner sGender;
    private EditText etPhone;
    private Button btnRegister, btnPickDate;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        btnPickDate = findViewById(R.id.btnDatePicker);
        btnPickDate.setText(getTodaysDate());

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"");
            }
        });

        etFName = findViewById(R.id.editFName);
        etLName = findViewById(R.id.editLName);
        etEmail = findViewById(R.id.editEmailAddress);
        etPW = findViewById(R.id.editPassword);
        try {
            bday = new SimpleDateFormat("dd/MM/yyyy").parse(btnPickDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sGender = findViewById(R.id.spinGender);
        etPhone = findViewById(R.id.editTextPhone);
        btnRegister = findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(validEmail(AuthenticationPage.this)==true && !TextUtils.isEmpty(etPW.getText())){
                    requiredFields(etFName, etLName, bday, sGender, etPhone);
                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                            etPW.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(AuthenticationPage.this, "Registered successfully. Please check your email's spam/junk folder for the verification link.", Toast.LENGTH_LONG).show();
                                                            etEmail.setText("");
                                                            etPW.setText("");

                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    AuthenticationPage.this.finish();
                                                                }
                                                            }, 5000);
                                                        }else {
                                                            Toast.makeText(AuthenticationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else{
                                        Toast.makeText(AuthenticationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
                else{
                    if(TextUtils.isEmpty(etPW.getText())){
                        etPW.setError( "Password is required!" );}
                    else {
                        Toast.makeText(AuthenticationPage.this, "Please register with Douglas College student email address", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateString = simpleDateFormat.format(c.getTime());

        btnPickDate.setText(currentDateString);
    }

    public String getTodaysDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateString = simpleDateFormat.format(c.getTime());

        return currentDateString;
    }

    public boolean validEmail(Context context){

        etEmail = findViewById(R.id.editEmailAddress);

        String email = etEmail.getText().toString();
        String pattern = ".*@student.douglascollege.ca";
        boolean match = Pattern.matches(pattern, email);

        return match;
    }

    public void requiredFields(EditText fname, EditText lname, Date bday, Spinner gender, EditText phone){
        if(TextUtils.isEmpty(fname.getText())){
//            Toast.makeText(AuthenticationPage.this, "First Name is required.", Toast.LENGTH_LONG).show();
            fname.setError( "First name is required!" );
        } else if(TextUtils.isEmpty(lname.getText())){
            lname.setError( "Last name is required!" );
//        } else if(TextUtils.isEmpty(Date.getText())){
//
//        } else if(TextUtils.isEmpty(Spinner.getText())){

        } else if(TextUtils.isEmpty(phone.getText())){
            phone.setError( "Phone Number is required!" );
        }
        else{
            Intent i = new Intent(getApplicationContext(), AuthenticationPage.class);
            startActivity(i);
        }
    }
}