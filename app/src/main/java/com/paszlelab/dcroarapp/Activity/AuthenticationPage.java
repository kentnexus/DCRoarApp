package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paszlelab.dcroarapp.Fragments.DatePickerFragment;
import com.paszlelab.dcroarapp.Models.Student;
import com.paszlelab.dcroarapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class AuthenticationPage extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private EditText etFName;
    private EditText etLName;
    private EditText etEmail;
    private EditText etPW;
    private Spinner spinGender;
    private EditText etPhone;
    private Button btnRegister, btnPickDate;
    private boolean addedInfo;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        etFName = findViewById(R.id.editFName);
        etLName = findViewById(R.id.editLName);
        etEmail = findViewById(R.id.editEmailAddress);
        etPW = findViewById(R.id.editPassword);
//        try {
//            bday = new SimpleDateFormat("dd/MM/yyyy").parse(btnPickDate.getText().toString());
//        } catch (ParseException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
        btnPickDate = findViewById(R.id.btnDatePicker);
        btnPickDate.setText(getTodaysDate());

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"");
            }
        });
        spinGender = findViewById(R.id.spinGender);
        etPhone = findViewById(R.id.editTextPhone);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        addedInfo = false;

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                boolean notEmptyFName = requiredField(etFName,"First Name");
                boolean notEmptyLName = requiredField(etLName, "Last Name");
                boolean notEmptyEmail = requiredField(etEmail, "Email Address");
                boolean notEmptyPW = requiredField(etPW, "Password");

                if( notEmptyFName == true && notEmptyLName == true &&
                        notEmptyEmail == true && notEmptyPW == true) {
                    if(validEmail(AuthenticationPage.this)==true){
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                                    etPW.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
//                                    for writing to firestore db
                                    String sFname = etFName.getText().toString();
                                    String sLname = etLName.getText().toString();
                                    String sEmail = etEmail.getText().toString();
                                    String sBday = btnPickDate.getText().toString();
                                    String sGender = spinGender.getSelectedItem().toString();
                                    String sPhone = etPhone.getText().toString();

                                    addDataToFirestore(sFname, sLname, sEmail, sBday, sGender, sPhone);
//                                    Log.d("-", addedInfo+"");

//                                    if authentication is successful
                                    if (task.isSuccessful()) {
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AuthenticationPage.this, "Registered successfully. Please check your email's spam/junk folder for the verification link.", Toast.LENGTH_LONG).show();
                                                            etEmail.setText("");
                                                            etPW.setText("");
                                                            AuthenticationPage.this.finish();
                                                        } else {
                                                            Toast.makeText(AuthenticationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(AuthenticationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else{
                        Toast.makeText(AuthenticationPage.this, "Please register with Douglas College student email address", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

//    writing to Firestore Database
    private void addDataToFirestore(String sFname, String sLname, String sEmail, String sBday, String sGender, String sPhone) {

        student = new Student(sEmail);

        student.setFirstName(sFname);
        student.setLastName(sLname);
        student.setBirthday(sBday);
        student.setGender(sGender);
        student.setPhoneNumber(sPhone);

        CollectionReference collectionReference = firebaseFirestore.collection("Student");

        collectionReference.add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
//                Log.d("-", "Data has been added to firestore.");
                addedInfo = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("-", e.getMessage());
            }
        });
    }

//getting the date from datepicker
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

//    getting today's today as default value
    public String getTodaysDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String currentDateString = simpleDateFormat.format(c.getTime());

        return currentDateString;
    }

//    only DC students can register
    public boolean validEmail(Context context){

        etEmail = findViewById(R.id.editEmailAddress);

        String email = etEmail.getText().toString();
        String pattern = ".*@student.douglascollege.ca";
        boolean match = Pattern.matches(pattern, email);

        return match;
    }

//    required fields must be non-null
    public boolean requiredField(EditText field, String sfield){
        boolean notEmpty = false;
        if(TextUtils.isEmpty(field.getText()))
        {
            field.setError( sfield+" is required!" );
        }
        else {
            notEmpty = true;
        }
        return notEmpty;
    }

}