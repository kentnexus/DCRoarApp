package com.paszlelab.dcroarapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paszlelab.dcroarapp.constants.Constants;

import javax.annotation.Nullable;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        documentReference = db.collection("Student")
                .document(auth.getUid());
    }

    @Override
    protected void onPause(){
        super.onPause();
        documentReference.update(Constants.KEY_AVAILABILITY,0);
    }

    @Override
    protected void onResume(){
        super.onResume();
        documentReference.update(Constants.KEY_AVAILABILITY,1);
    }
}
