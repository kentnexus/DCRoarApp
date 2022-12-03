package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.paszlelab.dcroarapp.R;

public class CoursePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        TextView cCode = findViewById(R.id.pageCourseName);
        TextView cName = findViewById(R.id.pageCourseCode);

        String crscode = "Course code not set";
        String crsname = "Course name not set";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crscode = extras.getString("courseCode");
            crsname = extras.getString("courseName");
        }

        cCode.setText(crscode);
        cName.setText(crsname);

    }
}