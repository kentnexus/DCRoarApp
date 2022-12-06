package com.paszlelab.dcroarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paszlelab.dcroarapp.Activity.CoursePage;
import com.paszlelab.dcroarapp.Adapters.CourseAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentCoursesBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    private FragmentCoursesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCoursesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        replaceFragment(new CourseChatListFragment());

        binding.navBartop.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.gcourses:
                    replaceFragment(new CoursesListFragment());
                    break;
                case R.id.gchats:
                    replaceFragment(new CourseChatListFragment());
                    break;
            }

            return true;
        });
        // Inflate the layout for this fragment
        return view;
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rFrameLayoutCourse, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}