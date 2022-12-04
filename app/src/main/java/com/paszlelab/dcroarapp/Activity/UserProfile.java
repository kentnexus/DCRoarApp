package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityUserProfileBinding;
import com.paszlelab.dcroarapp.models.Student;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    ActivityUserProfileBinding binding;
//    Button btnSend;
//    FirebaseAuth auth;
//    String CurrentState = "nothing_happened";
    FirebaseFirestore db;
    Student friend;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        friend = (Student) getIntent().getSerializableExtra("student");

        binding.txtFriendAbout.setText(friend.getFirstName() + " " + friend.getLastName());
        binding.profileFName.setText(friend.getFirstName());
        binding.profileLName.setText(friend.getLastName());
        binding.profileEmail.setText(friend.getEmailAddress());
        binding.profileGender.setText(friend.getGender());
        binding.profileDOB.setText(friend.getBirthday());

        String imgSrc = friend.getImg();

        try {
            storageReference = FirebaseStorage.getInstance()
                    .getReference().child("profileImages/" + imgSrc);

            final File localFile = File.createTempFile(friend.getId(),"jpeg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            binding.imgProfilePic.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch(Exception e){}
    }
}