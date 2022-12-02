package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paszlelab.dcroarapp.Adapters.MessageAdapter;
import com.paszlelab.dcroarapp.databinding.ActivityMessagingBinding;
import com.paszlelab.dcroarapp.models.Message;
import com.paszlelab.dcroarapp.models.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Messaging extends AppCompatActivity {

    private ActivityMessagingBinding binding;
    private Student receiver;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String conversationId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMessagingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }

    private void init() {
        messages = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        messageAdapter = new MessageAdapter(
                messages, auth.getCurrentUser().getUid()
        );
        binding.rViewMessages.setAdapter(messageAdapter);
        db = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        HashMap<String, Object> message = new HashMap<>();
        message.put("senderId", auth.getCurrentUser().getUid());
        message.put("receiverId", receiver.getId());
        message.put("message", binding.editTextMessage.getText().toString());
        message.put("timestamp", new Date());
        db.collection("message").add(message);
        if (conversationId != null) {
            Log.d("-", conversationId);
            updateConversation(binding.editTextMessage.getText().toString());
        } else {
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put("senderId", auth.getCurrentUser().getUid());
            conversation.put("senderName", auth.getCurrentUser().getEmail());
            conversation.put("receiverId", receiver.getId());
            conversation.put("receiverName", receiver.getEmailAddress());
            conversation.put("lastMessage", binding.editTextMessage.getText().toString());
            conversation.put("timestamp", new Date());
            addConversation(conversation);
        }
        binding.editTextMessage.setText(null);
    }

    //TODO: add picture

    private void listenMessages() {
        db.collection("message")
                .whereEqualTo("senderId", auth.getCurrentUser().getUid().toString())
                .whereEqualTo("receiverId", receiver.getId())
                .addSnapshotListener(eventListener);

        db.collection("message")
                .whereEqualTo("senderId", receiver.getId())
                .whereEqualTo("receiverId", auth.getCurrentUser().getUid().toString())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = messages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Message message = new Message();
                    message.setSender(documentChange.getDocument().getString("senderId"));
                    message.setReceiver(documentChange.getDocument().getString("receiverId"));
                    message.setMessage(documentChange.getDocument().getString("message"));
                    message.setDateTime(getReadableDateTime(documentChange.getDocument().getDate("timestamp")));
                    message.setDate(documentChange.getDocument().getDate("timestamp"));
                    messages.add(message);
                }
            }
            Collections.sort(messages, (obj1, obj2) -> obj1.getDate().compareTo(obj2.getDate()));
            if (count == 0) {
                messageAdapter.notifyDataSetChanged();
            } else {
                messageAdapter.notifyItemRangeInserted(messages.size(), messages.size());
                binding.rViewMessages.smoothScrollToPosition(messages.size() - 1);
            }
            binding.rViewMessages.setVisibility(View.VISIBLE);
        }
//        binding.progressBar.setVisibility(View.GONE);
        if (conversationId != null) {
            checkForConversation();
        }
    };

    private void loadReceiverDetails() {
        receiver = (Student) getIntent().getSerializableExtra("student");
        binding.toolbar.txtMessageSender.setText(receiver.getFirstName() + " " + receiver.getLastName());
    }

    private void setListeners() {
        binding.toolbar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.sendBtn.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversation(HashMap<String, Object> conversation) {
        db.collection("RecentConvo")
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference =
                db.collection("RecentConvo").document(conversationId);
        documentReference.update(
                "lastMessage", message,
                "timestamp", new Date()
        );
    }

    private void checkForConversation() {
        if (messages.size() != 0) {
            checkForConversationRemotely(
                    auth.getCurrentUser().getUid(),
                    receiver.getId()
            );
            checkForConversationRemotely(
                    receiver.getId(),
                    auth.getCurrentUser().getUid()
            );
        }
    }

    private void checkForConversationRemotely(String senderId, String receiverId) {
        db.collection("RecentConvo")
                .whereEqualTo("senderId", senderId)
                .whereEqualTo("receiverId", receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
            Log.d("-", conversationId);
        }
    };
}