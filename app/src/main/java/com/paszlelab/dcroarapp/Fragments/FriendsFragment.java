package com.paszlelab.dcroarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.paszlelab.dcroarapp.Activity.Add_Friends;
import com.paszlelab.dcroarapp.Activity.Message_Friends;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentFriendsBinding;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        launchAddFriends();

        return view;
    }

    public void launchAddFriends(){
        binding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Add_Friends.class);
                startActivity(intent);
            }
        });
    }

}