package com.paszlelab.dcroarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.paszlelab.dcroarapp.Activity.CoursePage;
import com.paszlelab.dcroarapp.Adapters.CourseAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentCourseChatListBinding;
import com.paszlelab.dcroarapp.databinding.FragmentCoursesListBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;

import java.util.ArrayList;
import java.util.List;

public class CoursesListFragment extends Fragment implements CourseListener {

    private FragmentCoursesListBinding binding;
    private ArrayList<CourseModel> courseModelArrayList;
    private CourseAdapter adapter;
    //    private CourseAdapter.CourseClickListener listener;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoursesListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        db = FirebaseFirestore.getInstance();

        getCourses();

        binding.searchId.setActivated(true);
        binding.searchId.setQueryHint("Type here");
        binding.searchId.setIconified(false);
        binding.searchId.clearFocus();
        binding.searchId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCourse(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCourse(newText);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onCourseClicked(CourseModel course) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CoursePage.class);
        intent.putExtra("course", course);
        startActivity(intent);
    }

    private void searchCourse(String query) {
        // creating a new array list to filter our data.
        if (query.isEmpty()) {
            getCourses();
        } else {
            binding.courseRView.setAdapter(null);
            filteredCourses(query);
        }

    }

    private void getCourses() {
        db.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {

                List<CourseModel> courses = new ArrayList<>();

                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    CourseModel course = new CourseModel();
                    course.setCourseDept(queryDocumentSnapshot.getString("department"));
                    course.setCourseCode(queryDocumentSnapshot.getString("courseCode"));
                    course.setCourseName(queryDocumentSnapshot.getString("name"));
                    course.setCourseId(queryDocumentSnapshot.getId());
                    courses.add(course);
                }
                if (courses.size() > 0) {
                    CourseAdapter courseAdapter = new CourseAdapter(courses, this);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    binding.courseRView.setHasFixedSize(true);

                    // setting layout manager
                    // to our recycler view.
                    binding.courseRView.setLayoutManager(manager);

                    // setting adapter to
                    // our recycler view.
                    binding.courseRView.setAdapter(courseAdapter);
                } else {
                    Toast.makeText(CoursesListFragment.this.getContext(), "No courses found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void filteredCourses(String query) {
        db.collection("courses").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {

                List<CourseModel> courses = new ArrayList<>();

                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    String dept = queryDocumentSnapshot.getString("department");
                    String courseCode = queryDocumentSnapshot.getString("courseCode");
                    String name = queryDocumentSnapshot.getString("name");

                    if (dept.toLowerCase().contains(query) ||
                            courseCode.contains(query) ||
                            name.toLowerCase().contains(query)) {
                        CourseModel course = new CourseModel();
                        course.setCourseDept(dept);
                        course.setCourseCode(courseCode);
                        course.setCourseName(name);
                        course.setCourseId(queryDocumentSnapshot.getId());
                        courses.add(course);
                    }
                }
                if (courses.size() > 0) {
                    CourseAdapter courseAdapter = new CourseAdapter(courses, this);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    binding.courseRView.setHasFixedSize(true);

                    // setting layout manager
                    // to our recycler view.
                    binding.courseRView.setLayoutManager(manager);

                    // setting adapter to
                    // our recycler view.
                    binding.courseRView.setAdapter(courseAdapter);
                } else {
                    Toast.makeText(CoursesListFragment.this.getContext(), "No courses found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}