package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.paszlelab.dcroarapp.Adapters.UsersAdapter;
import com.paszlelab.dcroarapp.databinding.ActivityMessageFriendsBinding;
import com.paszlelab.dcroarapp.listeners.UserListener;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Message_Friends extends BaseActivity implements UserListener {

    private ActivityMessageFriendsBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private List<Student> students;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMessageFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                Message_Friends.this.finish();
            }
        });
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
                            String img = queryDocumentSnapshot.getId()+".jpeg";

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }

                            Student student = new Student();

                            student.setFirstName(firstName);
                            student.setLastName(lastName);
                            student.setEmailAddress(email);
                            student.setId(queryDocumentSnapshot.getId());
                            student.setImg(img);
                            student.setFullname(student.getFirstName()+" "+student.getLastName());
                            students.add(student);
                            Collections.sort(students, Student.firstNameComparator);
                        }
                        if(students.size()>0){
                            UsersAdapter usersAdapter = new UsersAdapter(students, this);
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
                            String img = queryDocumentSnapshot.getId()+".jpeg";

                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            } else {
                                if(firstName.toLowerCase().contains(query.toLowerCase()) ||
                                        lastName.toLowerCase().contains(query.toLowerCase())){

                                    Student student = new Student();

                                    student.setFirstName(firstName);
                                    student.setLastName(lastName);
                                    student.setEmailAddress(email);
                                    student.setImg(img);
                                    student.setFullname(student.getFirstName()+" "+student.getLastName());
                                    students.add(student);
                                }
                            }
                        }
                        if(students.size()>0){
                            UsersAdapter usersAdapter = new UsersAdapter(students, this);
                            binding.recyclerViewUsers.setAdapter(usersAdapter);
                            binding.recyclerViewUsers.setVisibility(View.VISIBLE);
                            binding.txtErrorMessage.setVisibility(View.GONE);
                        } else {
                            showErrorMessage();
                        }
                    }
                });
    }

    @Override
    public void onUserClicked(Student student) {
        Intent intent = new Intent(getApplicationContext(), Messaging.class);
        intent.putExtra("student", student);
        startActivity(intent);
        Message_Friends.this.finish();
    }
}