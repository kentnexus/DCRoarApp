package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
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
    private StorageReference storageReference;
    private Student student;


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
        student = new Student();
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
            message.put("senderName", student.getFullname());
            message.put("message", binding.editTextMessage.getText().toString());
            message.put("timestamp", new Date());
            message.put("courseId", course.getCourseId());
            db.collection("chatroom").add(message);

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
                binding.rViewMessages.smoothScrollToPosition(messages.size() - 1);
            }
            binding.rViewMessages.setVisibility(View.VISIBLE);
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

                        student.setFirstName(documentSnapshot.getString("firstName"));
                        student.setLastName(documentSnapshot.getString("lastName"));
                        student.setFullname(student.getFirstName() + " " + student.getLastName());
                    }
                });
    }
}