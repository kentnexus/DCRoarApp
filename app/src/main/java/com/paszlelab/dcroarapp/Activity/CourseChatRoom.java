package com.paszlelab.dcroarapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.Adapters.MessageAdapter;
import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.ActivityCourseChatRoomBinding;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CourseChatRoom extends AppCompatActivity {

    ActivityCourseChatRoomBinding binding;
    CourseModel course;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String conversationId = null;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bitmap headerIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.friends);
        binding.toolbar.profileImage.setImageBitmap(headerIcon);

        loadCourse();
        init();
    }

    public void init(){

    }

    private void sendMessage() {
        if (!binding.editTextMessage.getText().toString().isEmpty()) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("courseDept", course.getCourseDept());
            message.put("courseCode", course.getCourseCode());
            message.put("senderId", auth.getCurrentUser().getUid());
            message.put("message", binding.editTextMessage.getText().toString());
            message.put("timestamp", new Date());
            db.collection("chatroom").add(message);

            binding.editTextMessage.setText(null);
        }
    }

    public void loadCourse(){
        course = (CourseModel) getIntent().getSerializableExtra("course");
    }
}