package com.paszlelab.dcroarapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.paszlelab.dcroarapp.Adapters.ContactAdapter_old;
import com.paszlelab.dcroarapp.Adapters.UsersAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentAddFriendsBinding;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddFriendsFragment extends Fragment {

    private FragmentAddFriendsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private List<Student> students;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddFriendsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SearchView sv = binding.userSearchView;
        getUsers();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUser(newText);
                return true;
            }
        });

        binding.backSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void getUsers(){
        loading(true);
        db.collection("Student")
                .get()
                .addOnCompleteListener( task -> {
                    loading(false);
                    String currentUserId = auth.getCurrentUser().getUid();
                    if(task.isSuccessful() && task.getResult() != null){
                        students = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()) {

                            String firstName = queryDocumentSnapshot.getString("firstName");
                            String lastName = queryDocumentSnapshot.getString("lastName");
                            String email = queryDocumentSnapshot.getString("emailAddress");

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }

                            Student student = new Student();

                            student.setFirstName(firstName);
                            student.setLastName(lastName);
                            student.setEmailAddress(email);
                            students.add(student);
                            Collections.sort(students, Student.firstNameComparator);
                        }
                        if(students.size()>0){
                            UsersAdapter usersAdapter = new UsersAdapter(students);
                            binding.recyclerViewUsers.setAdapter(usersAdapter);
                            binding.recyclerViewUsers.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    }
                });
    }

    private void showErrorMessage(){
        binding.txtErrorMessage.setText(String.format("%s","No user available"));
        binding.txtErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBarUsers.setVisibility(View.VISIBLE);
        } else {
            binding.progressBarUsers.setVisibility(View.GONE);
        }
    }

    private void searchUser(String query){
        if(query.isEmpty()){
            getUsers();
        } else {
            binding.recyclerViewUsers.setAdapter(null);
            filteredUsers(query);
        }

    }

    private void filteredUsers(String query){
        loading(true);
        db.collection("Student")
                .get()
                .addOnCompleteListener( task -> {
                    loading(false);
                    String currentUserId = auth.getCurrentUser().getUid();
                    if(task.isSuccessful() && task.getResult() != null){
                        List<Student> students = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()) {

                            String firstName = queryDocumentSnapshot.getString("firstName");
                            String lastName = queryDocumentSnapshot.getString("lastName");
                            String email = queryDocumentSnapshot.getString("emailAddress");

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            } else {
                                Log.d("-",query);
                                if(firstName.toLowerCase().contains(query.toLowerCase()) ||
                                        lastName.toLowerCase().contains(query.toLowerCase())){

                                    Student student = new Student();

                                    student.setFirstName(firstName);
                                    student.setLastName(lastName);
                                    student.setEmailAddress(email);
                                    students.add(student);
                                }
                            }
                        }
                        if(students.size()>0){
                            UsersAdapter usersAdapter = new UsersAdapter(students);
                            binding.recyclerViewUsers.setAdapter(usersAdapter);
                            binding.recyclerViewUsers.setVisibility(View.VISIBLE);
                            binding.txtErrorMessage.setVisibility(View.GONE);
                        } else {
                            showErrorMessage();
                        }
                    }
                });
    }
}