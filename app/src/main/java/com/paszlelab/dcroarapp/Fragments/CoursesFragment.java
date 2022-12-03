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

import com.paszlelab.dcroarapp.Activity.CoursePage;
import com.paszlelab.dcroarapp.Adapters.CourseAdapter;
import com.paszlelab.dcroarapp.databinding.FragmentCoursesBinding;
import com.paszlelab.dcroarapp.models.CourseModel;

import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    private FragmentCoursesBinding binding;
    private ArrayList<CourseModel> courseModelArrayList;
    CourseAdapter adapter;
    private CourseAdapter.CourseClickListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCoursesBinding.inflate(inflater, container,false);
        View view = binding.getRoot();

        getCourses();

        binding.searchId.setActivated(true);
        binding.searchId.setQueryHint("Type here");
        binding.searchId.setIconified(false);
        binding.searchId.clearFocus();
        binding.searchId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredSearch(newText);
                return true;
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



    private void filteredSearch(String query) {
        // creating a new array list to filter our data.
        ArrayList<CourseModel> filteredlist = new ArrayList<CourseModel>();

        for (CourseModel item : courseModelArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getCourseCode().toLowerCase().contains(query.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                Log.d("matched Course code", item.toString());
                filteredlist.add(item);

            }
            else if (item.getCourseName().toLowerCase().contains(query.toLowerCase())) {
                Log.d("matched Course name", item.toString());
                filteredlist.add(item);
            }
        }

        if(query.isEmpty()){
            getCourses();
            Toast.makeText(getContext(),"No Course Found..", Toast.LENGTH_SHORT).show();
            Log.d("Empty filter: ", "no course found");
        } else {
            adapter = new CourseAdapter(filteredlist,listener, CoursesFragment.this);
            binding.courseRView.setAdapter(adapter);
        }

    }

    private void getCourses() {
        setCourseOnClickListener();
        courseModelArrayList = new ArrayList<CourseModel>();

        // below line is to add data to our array list.
        courseModelArrayList.add(new CourseModel("BUSN 1198", "BUSINESS EXPLORATIONS I"));
        courseModelArrayList.add(new CourseModel("BUSN 1200", "FUNDAMENTALS OF BUSINESS"));
        courseModelArrayList.add(new CourseModel("CSIS 3175", "INTRODUCTION TO MOBILE APPLICATION DEVELOPMENT"));
        courseModelArrayList.add(new CourseModel("CSIS 1275", "INTRODUCTION TO PROGRAMMING II"));
        courseModelArrayList.add(new CourseModel("CSIS 1280", "MULTIMEDIA WEB DEVELOPMENT"));

        // initializing our adapter class.
        adapter = new CourseAdapter(courseModelArrayList, listener, CoursesFragment.this );

        // adding layout manager to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.courseRView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        binding.courseRView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        binding.courseRView.setAdapter(adapter);
    }

    private void setCourseOnClickListener() {
        listener = new CourseAdapter.CourseClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getContext(), CoursePage.class);
                intent.putExtra("courseCode", courseModelArrayList.get(position).getCourseCode());
                intent.putExtra("courseName", courseModelArrayList.get(position).getCourseName());
                startActivity(intent);
            }
        };
    }
}