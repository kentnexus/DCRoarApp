package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityCoursePageBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseChat;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Student;

public class CoursePage extends AppCompatActivity implements CourseListener {

    ActivityCoursePageBinding binding;
    CourseModel course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoursePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        course = (CourseModel) getIntent().getSerializableExtra("course");

        binding.joinBtn.setOnClickListener(v -> {
            onCourseClicked(course);
        });
    }

    @Override
    public void onCourseClicked(CourseModel course) {
        Intent intent = new Intent(CoursePage.this, CourseChatRoom.class);
        intent.putExtra("course", course);
        startActivity(intent);
    }
}