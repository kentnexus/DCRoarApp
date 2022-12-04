package com.paszlelab.dcroarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.paszlelab.dcroarapp.Activity.Add_Friends;
import com.paszlelab.dcroarapp.Activity.Message_Friends;
import com.paszlelab.dcroarapp.Activity.UserProfile;
import com.paszlelab.dcroarapp.Adapters.FindUsersAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityAddFriendsBinding;
import com.paszlelab.dcroarapp.databinding.FragmentFriendsBinding;
import com.paszlelab.dcroarapp.listeners.UserListener;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendsFragment extends Fragment implements UserListener {

    FragmentFriendsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private List<Student> students;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        androidx.appcompat.widget.SearchView sv = binding.mainDisp.userSearchView;
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

        return view;
    }

//    public void launchAddFriends(){
//        binding.btnPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getActivity().getApplicationContext(), Add_Friends.class);
//                startActivity(intent);
//            }
//        });
//    }

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

                            String userId = queryDocumentSnapshot.getId();
                            String firstName = queryDocumentSnapshot.getString("firstName");
                            String lastName = queryDocumentSnapshot.getString("lastName");
                            String email = queryDocumentSnapshot.getString("emailAddress");
                            String gender = queryDocumentSnapshot.getString("gender");
                            String dob = queryDocumentSnapshot.getString("birthday");
                            String img = userId+".jpeg";

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }

                            Student student = new Student();

                            student.setFirstName(firstName);
                            student.setLastName(lastName);
                            student.setEmailAddress(email);
                            student.setGender(gender);
                            student.setBirthday(dob);
                            student.setId(userId);
                            student.setImg(img);
                            students.add(student);
                            Collections.sort(students, Student.firstNameComparator);
                        }
                        if(students.size()>0){
                            FindUsersAdapter findUsersAdapter = new FindUsersAdapter(students, this);
                            binding.mainDisp.recyclerViewUsers.setAdapter(findUsersAdapter);
                            binding.mainDisp.recyclerViewUsers.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    }
                });
    }

    private void showErrorMessage(){
        binding.mainDisp.txtErrorMessage.setText(String.format("%s","No user available"));
        binding.mainDisp.txtErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.mainDisp.progressBarUsers.setVisibility(View.VISIBLE);
        } else {
            binding.mainDisp.progressBarUsers.setVisibility(View.GONE);
        }
    }

    private void searchUser(String query){
        if(query.isEmpty()){
            getUsers();
        } else {
            binding.mainDisp.recyclerViewUsers.setAdapter(null);
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

                            String userId = queryDocumentSnapshot.getId();
                            String firstName = queryDocumentSnapshot.getString("firstName");
                            String lastName = queryDocumentSnapshot.getString("lastName");
                            String email = queryDocumentSnapshot.getString("emailAddress");
                            String gender = queryDocumentSnapshot.getString("gender");
                            String dob = queryDocumentSnapshot.getString("birthday");
                            String img = userId+".jpeg";

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            } else {
                                if(firstName.toLowerCase().contains(query.toLowerCase()) ||
                                        lastName.toLowerCase().contains(query.toLowerCase())){

                                    Student student = new Student();

                                    student.setFirstName(firstName);
                                    student.setLastName(lastName);
                                    student.setEmailAddress(email);
                                    student.setGender(gender);
                                    student.setBirthday(dob);
                                    student.setId(userId);
                                    student.setImg(img);
                                    students.add(student);
                                }
                            }
                        }
                        if(students.size()>0){
                            FindUsersAdapter findUsersAdapter = new FindUsersAdapter(students, this);
                            binding.mainDisp.recyclerViewUsers.setAdapter(findUsersAdapter);
                            binding.mainDisp.recyclerViewUsers.setVisibility(View.VISIBLE);
                            binding.mainDisp.txtErrorMessage.setVisibility(View.GONE);
                        } else {
                            showErrorMessage();
                        }
                    }
                });
    }

    @Override
    public void onUserClicked(Student student) {
        Intent intent = new Intent(getActivity().getApplicationContext(), UserProfile.class);
        intent.putExtra("student", student);
        startActivity(intent);
    }

}