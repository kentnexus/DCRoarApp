package com.paszlelab.dcroarapp.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.paszlelab.dcroarapp.Adapters.ContactAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentAddFriendsBinding;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;

public class AddFriendsFragment extends Fragment {

    private FragmentAddFriendsBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<Student> userContacts, appContacts;
    private ContactAdapter contactAdapter;
    private String userEmail;
    private ImageView btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddFriendsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.recyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewContact.setHasFixedSize(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userEmail = firebaseAuth.getCurrentUser().getEmail();


        btnBack = view.findViewById(R.id.backSearchBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }


}