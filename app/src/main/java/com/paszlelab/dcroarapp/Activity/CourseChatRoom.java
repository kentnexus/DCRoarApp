package com.paszlelab.dcroarapp.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.Adapters.CourseChatAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityCourseChatRoomBinding;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Message;
import com.paszlelab.dcroarapp.models.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CourseChatRoom extends BaseActivity {

    private ActivityCourseChatRoomBinding binding;
    private CourseModel course;
    private List<Message> messages;
    private CourseChatAdapter courseChatAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Student user;
    private String conversationId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityCourseChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadCourse();
        init();
        loadUser();
        listenMessages();

    }

    public void init() {
        Bitmap headerIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_courses);
        binding.toolbar.profileImage.setImageBitmap(headerIcon);
        binding.toolbar.txtMessageSender.setText(course.getCourseDept() + " " + course.getCourseCode());
        messages = new ArrayList<>();
        user = new Student();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        courseChatAdapter = new CourseChatAdapter(messages, auth.getUid(), course.getCourseId());
        binding.rViewMessages.setAdapter(courseChatAdapter);
    }

    private void sendMessage() {
        if (!binding.editTextMessage.getText().toString().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("courseDept", course.getCourseDept());
            message.put("courseCode", course.getCourseCode());
            message.put("senderId", auth.getUid());
            message.put("senderName", user.getFullname());
            message.put("message", binding.editTextMessage.getText().toString());
            message.put("timestamp", new Date());
            message.put("courseId", course.getCourseId());
            db.collection("chatroom").add(message);
            if (conversationId != null) {
                Log.d("-", conversationId + " id");
                updateConversation(binding.editTextMessage.getText().toString());
            } else {
                Log.d("-", "no id");
                HashMap<String, Object> conversation = new HashMap<>();
                conversation.put("senderId", auth.getUid());
                conversation.put("senderName", user.getFirstName() + " " + user.getLastName());
                conversation.put("receiverId", course.getCourseId());
                conversation.put("receiverName", course.getCourseDept()+" "+course.getCourseCode());
                conversation.put("lastMessage", binding.editTextMessage.getText().toString());
                conversation.put("timestamp", new Date());
                addConversation(conversation);
            }
            binding.editTextMessage.setText(null);
        }
    }

    private void listenMessages() {
        db.collection("chatroom")
                .whereEqualTo("courseId", course.getCourseId())
                .addSnapshotListener(eventListener);

//        Log.d("course id", course.getCourseId());
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
//        Log.d("value",value.toString());
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = messages.size();
//            Log.d("count", count+"");
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Message message = new Message();
                    message.setSender(documentChange.getDocument().getString("senderId"));
                    message.setConversationName(documentChange.getDocument().getString("senderName"));
                    message.setMessage(documentChange.getDocument().getString("message"));
                    message.setDateTime(getReadableDateTime(documentChange.getDocument().getDate("timestamp")));
                    message.setDate(documentChange.getDocument().getDate("timestamp"));
//                    Log.d("message",message.getMessage().toString());
                    messages.add(message);
                }
            }
            Collections.sort(messages, (obj1, obj2) -> obj1.getDate().compareTo(obj2.getDate()));
            if (count == 0) {
                courseChatAdapter.notifyDataSetChanged();
            } else {
                courseChatAdapter.notifyItemRangeInserted(messages.size(), messages.size());
//                binding.rViewMessages.smoothScrollToPosition(messages.size() - 1);
            }
            binding.rViewMessages.setVisibility(View.VISIBLE);
        }

        if (conversationId == null) {
            checkForConversation();
        }
//        binding.progressBar.setVisibility(View.GONE);
    };

    public void loadCourse() {
        course = (CourseModel) getIntent().getSerializableExtra("course");
    }

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void setListeners() {
        binding.toolbar.backBtn.setOnClickListener(v -> CourseChatRoom.this.finish());
        binding.sendBtn.setOnClickListener(v -> sendMessage());
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

    private void addConversation(HashMap<String, Object> conversation) {
        db.collection("RecentCourse").add(conversation)
                .addOnSuccessListener(documentReference ->
                        conversationId = documentReference.getId());
    }

    private void updateConversation(String message) {
        DocumentReference documentReference = db.collection("RecentCourse").document(conversationId);
        documentReference.update("lastMessage", message,
                "timestamp", new Date());
    }

    private void checkForConversation() {
        if (messages.size() != 0) {
            checkForConversationRemotely(course.getCourseId());
        }
    }

    private void checkForConversationRemotely(String courseId) {
        db.collection("RecentCourse")
                .whereEqualTo("receiverId", course.getCourseId())
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