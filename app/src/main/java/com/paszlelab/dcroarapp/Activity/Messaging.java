package com.paszlelab.dcroarapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.Adapters.MessageAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.Utilities.RetrieveImage;
import com.paszlelab.dcroarapp.constants.Constants;
import com.paszlelab.dcroarapp.databinding.ActivityMessagingBinding;
import com.paszlelab.dcroarapp.models.Message;
import com.paszlelab.dcroarapp.models.Student;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Messaging extends BaseActivity {

    private ActivityMessagingBinding binding;
    private Student receiver, user;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String conversationId = null;
    private StorageReference storageReference;
    private Boolean isReceiverAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMessagingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        loadUser();
        listenMessages();
    }

    private void init() {
        messages = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        messageAdapter = new MessageAdapter(messages, auth.getUid());
        user = new Student();
        binding.rViewMessages.setAdapter(messageAdapter);
        db = FirebaseFirestore.getInstance();
    }

    private void sendMessage() {
        if (!binding.editTextMessage.getText().toString().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("senderId", auth.getCurrentUser().getUid());
            message.put("receiverId", receiver.getId());
            message.put("message", binding.editTextMessage.getText().toString());
            message.put("timestamp", new Date());
            db.collection("message").add(message);
            if (conversationId != null) {
                Log.d("-", conversationId + " id");
                updateConversation(binding.editTextMessage.getText().toString());
            } else {
                Log.d("-", "no id");
                HashMap<String, Object> conversation = new HashMap<>();
                conversation.put("senderId", auth.getCurrentUser().getUid());
                conversation.put("senderName", user.getFirstName() + " " + user.getLastName());
                conversation.put("receiverId", receiver.getId());
                conversation.put("receiverName", receiver.getFirstName() + " " + receiver.getLastName());
                conversation.put("lastMessage", binding.editTextMessage.getText().toString());
                conversation.put("timestamp", new Date());
                addConversation(conversation);
            }
            binding.editTextMessage.setText(null);
        }
    }

    private void listenMessages() {
        db.collection("message")
                .whereEqualTo("senderId", auth.getUid().toString())
                .whereEqualTo("receiverId", receiver.getId())
                .addSnapshotListener(eventListener);

        db.collection("message")
                .whereEqualTo("senderId", receiver.getId())
                .whereEqualTo("receiverId", auth.getUid().toString())
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
        if (conversationId == null) {
            checkForConversation();
        }
    };

    private void loadReceiverDetails() {
        receiver = (Student) getIntent().getSerializableExtra("student");
        binding.toolbar.txtMessageSender.setText(receiver.getFullname());
//        binding.toolbar.txtMessageSender.setText(receiver.getEmailAddress());
//        String imgSrc = receiver.getId() + ".jpeg";
//        TODO: this

        RetrieveImage.getImg(this, receiver.getId(), binding.toolbar.profileImage);

//        try {
//            storageReference = FirebaseStorage.getInstance()
//                    .getReference().child("profileImages/" + imgSrc);
//
//            final File localFile = File.createTempFile(receiver.getId(),"jpeg");
//            storageReference.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                            binding.toolbar.profileImage.setImageBitmap(bitmap);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//        } catch(Exception e){}
    }

    private void loadUser() {
//        Log.d("user", auth.getUid());
        db.collection("Student")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        user.setFirstName(documentSnapshot.getString("firstName"));
                        user.setLastName(documentSnapshot.getString("lastName"));
                        user.setFullname(user.getFirstName() + " " + user.getLastName());
                    }
                });
    }

    private void setListeners() {
        binding.toolbar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.sendBtn.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversation(HashMap<String, Object> conversation) {
        db.collection("RecentConvo").add(conversation).addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference = db.collection("RecentConvo").document(conversationId);
        documentReference.update("lastMessage", message,
                "timestamp", new Date());
    }

    private void checkForConversation() {
        if (messages.size() != 0) {
            checkForConversationRemotely(auth.getCurrentUser().getUid(), receiver.getId());
            checkForConversationRemotely(receiver.getId(), auth.getCurrentUser().getUid());
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
//            Log.d("-", conversationId);
        }
    };

    private void listenAvailabilityOfReceiver() {

        db.collection("Student")
                .document(receiver.getId())
                .addSnapshotListener(Messaging.this, (value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null) {
                        try {
                            if (value.getLong(Constants.KEY_AVAILABILITY) != null) {
                                int availability = Objects.requireNonNull(
                                        value.getLong(Constants.KEY_AVAILABILITY)
                                ).intValue();
                                isReceiverAvailable = availability == 1;
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (isReceiverAvailable) {
                        binding.txtAvailable.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtAvailable.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}