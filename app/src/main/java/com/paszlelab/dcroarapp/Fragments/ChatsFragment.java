package com.paszlelab.dcroarapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paszlelab.dcroarapp.Adapters.RecentConversationsAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FragmentChatsBinding;
import com.paszlelab.dcroarapp.models.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private FirebaseAuth auth;
    private List<Message> conversations;
    private RecentConversationsAdapter recentConversationsAdapter;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        init();
        launchFindFriends();
        listenConversations();
//
//        Log.d("-", conversations.size()+"");
        return view;
    }

    private void init() {
        conversations = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        recentConversationsAdapter = new RecentConversationsAdapter(conversations);
        binding.rViewRecentMessages.setAdapter(recentConversationsAdapter);
        db = FirebaseFirestore.getInstance();
    }

    public void launchFindFriends() {
        binding.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MessageFriendsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rFrameFindReceiver, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void listenConversations(){
        db.collection("RecentConvo")
                .whereEqualTo("senderId", auth.getCurrentUser().getUid())
                .addSnapshotListener(eventListener);
        db.collection("RecentConvo")
                .whereEqualTo("receiverId", auth.getCurrentUser().getUid())
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
                    String receiverId = documentChange.getDocument().getString("receiverId");
                    Message message = new Message();
                    message.setSender(senderId);
                    message.setReceiver(receiverId);
                    if (auth.getCurrentUser().getUid().equals(senderId)) {
                        message.setConversionName(documentChange.getDocument().getString("receiverName"));
                        message.setConversionId(documentChange.getDocument().getString("receiverId"));
                    } else {
                        message.setConversionName(documentChange.getDocument().getString("senderName"));
                        message.setConversionId(documentChange.getDocument().getString("senderId"));
                    }
                    message.setMessage(documentChange.getDocument().getString("lastMessage"));
                    message.setDate(documentChange.getDocument().getDate("timestamp"));
                    conversations.add(message);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        Log.d("-","this works");
                        String senderId = documentChange.getDocument().getString("senderId");
                        String receiverId = documentChange.getDocument().getString("receiverId");
                        if(conversations.get(i).getSender().equals(senderId) &&
                                    conversations.get(i).getReceiver().equals(receiverId)){
                            conversations.get(i).setMessage(documentChange.getDocument().getString("lastMessage"));
                            conversations.get(i).setDate(documentChange.getDocument().getDate("timestamp"));
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations,(obj1, obj2) -> obj2.getDate().compareTo(obj1.getDate()));
            recentConversationsAdapter.notifyDataSetChanged();
            binding.rViewRecentMessages.smoothScrollToPosition(0);
            binding.rViewRecentMessages.setVisibility(View.VISIBLE);
        }
    };
}