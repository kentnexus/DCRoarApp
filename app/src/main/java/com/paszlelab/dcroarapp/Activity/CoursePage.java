package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paszlelab.dcroarapp.databinding.ActivityCoursePageBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Message;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class CoursePage extends BaseActivity implements CourseListener {

    private ActivityCoursePageBinding binding;
    private CourseModel course;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private boolean[] hasJoined = new boolean[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityCoursePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadCourse();
        binding.pageCourseName.setText(course.getCourseName());
        binding.pageCourseCode.setText(course.getCourseDept() + " " + course.getCourseCode());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadUser();

//        binding.joinBtn.setOnClickListener(v -> {
//            onCourseClicked(course);
//        });

    }

    private void loadCourse() {
        course = (CourseModel) getIntent().getSerializableExtra("course");
    }

    @Override
    public void onCourseClicked(CourseModel course) {
        Intent intent = new Intent(CoursePage.this, CourseChatRoom.class);
        intent.putExtra("course", course);
        startActivity(intent);
        CoursePage.this.finish();
    }

    private void loadUser() {
        db.collection("chatroom")
                .whereEqualTo("courseId", course.getCourseId())
                .whereEqualTo("senderId", auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            hasJoined[0] = task.getResult().isEmpty();
                            if (hasJoined[0]) {
                                binding.joinBtn.setOnClickListener(v -> {
                                    onCourseClicked(course);
                                });
                            } else {
                                binding.joinBtn.setEnabled(false);
                                binding.joinBtn.setText("Joined");
                            }
                        }
                    }
                });
    }
}