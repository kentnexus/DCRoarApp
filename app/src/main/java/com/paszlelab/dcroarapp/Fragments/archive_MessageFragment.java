package com.paszlelab.dcroarapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityMessagingBinding;
import com.paszlelab.dcroarapp.databinding.ArchiveFragmentMessageBinding;
import com.paszlelab.dcroarapp.databinding.FragmentAddFriendsBinding;
import com.paszlelab.dcroarapp.models.Student;

public class archive_MessageFragment extends Fragment {

    private ArchiveFragmentMessageBinding binding;
    private Student receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ArchiveFragmentMessageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }

//    private void loadReceiverDetails(){
//        receiver = (Student) getIntent().getSerializableExtra("student");
//        binding.toolbar.txtMessageSender.setText(receiver.getFirstName() + " " + receiver.getLastName());
//    }
//
//    private void setListeners(){
//        binding.toolbar.backBtn.setOnClickListener(v->onBackPressed());
//    }
}