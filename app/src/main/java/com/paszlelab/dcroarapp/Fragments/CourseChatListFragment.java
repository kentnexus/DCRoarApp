package com.paszlelab.dcroarapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paszlelab.dcroarapp.Activity.CourseChatRoom;
import com.paszlelab.dcroarapp.Activity.CoursePage;
import com.paszlelab.dcroarapp.Activity.Messaging;
import com.paszlelab.dcroarapp.Adapters.RecentConversationsAdapter;
import com.paszlelab.dcroarapp.Adapters.RecentConversationsCourseAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentCourseChatListBinding;
import com.paszlelab.dcroarapp.listeners.ConversationListener;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Message;
import com.paszlelab.dcroarapp.models.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseChatListFragment extends Fragment  implements CourseListener {

    private FragmentCourseChatListBinding binding;
    private FirebaseAuth auth;
    private List<Message> conversations;
    private RecentConversationsCourseAdapter recentConversationsCourseAdapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCourseChatListBinding.inflate(inflater, container,false);
        View view = binding.getRoot();
        init();
        listenConversations();
        return view;
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        conversations = new ArrayList<>();
        recentConversationsCourseAdapter = new RecentConversationsCourseAdapter(conversations, this);
        binding.rViewRecentMessages.setAdapter(recentConversationsCourseAdapter);
    }

    private void listenConversations(){
        db.collection("RecentCourse")
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString("senderId");
                    String courseId = documentChange.getDocument().getString("receiverId");
                    Message message = new Message();
                    message.setSender(senderId);
                    message.setReceiver(courseId);
                    message.setConversationName(documentChange.getDocument().getString("receiverName"));
                    message.setMessage(documentChange.getDocument().getString("lastMessage"));
                    message.setDate(documentChange.getDocument().getDate("timestamp"));
                    conversations.add(message);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String receiverId = documentChange.getDocument().getString("receiverId");
                        if(conversations.get(i).getReceiver().equals(receiverId)){
                            conversations.get(i).setMessage(documentChange.getDocument().getString("lastMessage"));
                            conversations.get(i).setDate(documentChange.getDocument().getDate("timestamp"));
                            break;
                        }
                    }
                }
            }

            Collections.sort(conversations,(obj1, obj2) -> obj2.getDate().compareTo(obj1.getDate()));
            recentConversationsCourseAdapter.notifyDataSetChanged();
            binding.rViewRecentMessages.smoothScrollToPosition(0);
            binding.rViewRecentMessages.setVisibility(View.VISIBLE);
        }
    };


    @Override
    public void onCourseClicked(CourseModel course) {
        Intent intent = new Intent(getActivity().getApplicationContext(), CourseChatRoom.class);
        intent.putExtra("course", course);
        startActivity(intent);
    }
}