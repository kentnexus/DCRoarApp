package com.paszlelab.dcroarapp.Activity;

import static com.paszlelab.dcroarapp.Validations.ValidateFields.getGender;
import static com.paszlelab.dcroarapp.Validations.ValidateFields.requiredField;
import static com.paszlelab.dcroarapp.Validations.ValidateFields.validEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paszlelab.dcroarapp.Fragments.DatePickerFragment;
import com.paszlelab.dcroarapp.models.Student;
import com.paszlelab.dcroarapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegistrationPage extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{

    private EditText etFName, etLName, etEmail, etPW, etGender, etPhone;
    private RadioButton rMale, rFemale, rNon;
    private Button btnRegister, btnPickDate, btnBack;
    private boolean addedInfo;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationPage.this.finish();
            }
        });

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

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"");
            }
        });
        rMale = findViewById(R.id.rbMale);
        rFemale = findViewById(R.id.rbFemale);
        rNon = findViewById(R.id.rbNon);
        etGender = findViewById(R.id.etGender);

        etPhone = findViewById(R.id.editPhoneNumber);
        btnRegister = findViewById(R.id.btnPlus);
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
                    if(validEmail(RegistrationPage.this, etEmail)==true){
                        progressBar.setVisibility(View.VISIBLE);
                        btnRegister.setEnabled(false);
                        btnBack.setEnabled(false);
                        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                                        etPW.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
//                                    Log.d("-", addedInfo+"");
                                        btnRegister.setEnabled(true);
                                        btnBack.setEnabled(true);

//                                    if email registration is successful
                                        if (task.isSuccessful()) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(etFName.getText().toString()+" "+etLName.getText().toString())
                                                    .build();

                                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
//                                                                for writing to firestore db
                                                                String sFname = etFName.getText().toString();
                                                                String sLname = etLName.getText().toString();
                                                                String sEmail = etEmail.getText().toString();
                                                                String sGender = getGender(rMale, rFemale, rNon);
                                                                String sBday = btnPickDate.getText().toString();
                                                                String sPhone = etPhone.getText().toString();

                                                                addDataToFirestore(sFname, sLname, sEmail, sBday, sGender, sPhone);

//                                                                Toast.makeText(RegistrationPage.this, "Registered successfully. Please check your email's spam/junk folder for the verification link.", Toast.LENGTH_LONG).show()
                                                                Intent intent = new Intent(RegistrationPage.this, EmailSentPage.class);
                                                                intent.putExtra("emailAdd",sEmail);
                                                                startActivity(intent);
                                                                RegistrationPage.this.finishAffinity();
                                                            } else {
                                                                Toast.makeText(RegistrationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                        } else {
                                            Toast.makeText(RegistrationPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else{
                        Toast.makeText(RegistrationPage.this, "Please register with Douglas College student email address", Toast.LENGTH_LONG).show();
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
        collectionReference.document(firebaseAuth.getCurrentUser().getUid())
                .set(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("-", "Data has been added to firestore.");
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

    public void goToLogin(View v){
        startActivity(new Intent(this, LoginPage.class));
    }


}