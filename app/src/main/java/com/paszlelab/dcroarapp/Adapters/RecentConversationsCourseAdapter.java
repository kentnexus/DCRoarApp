package com.paszlelab.dcroarapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.MessageItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;
import com.paszlelab.dcroarapp.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentConversationsCourseAdapter extends RecyclerView.Adapter<RecentConversationsCourseAdapter.ConversationViewHolder> {

    private final List<Message> messages;
    private final CourseListener courseListener;

    public RecentConversationsCourseAdapter(List<Message> messages, CourseListener courseListener) {
        this.messages = messages;
        this.courseListener = courseListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentConversationsCourseAdapter.ConversationViewHolder(
                MessageItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        MessageItemLayoutBinding binding;

        ConversationViewHolder(MessageItemLayoutBinding messageItemLayoutBinding) {
            super(messageItemLayoutBinding.getRoot());
            binding = messageItemLayoutBinding;
        }

        void setData(Message message) {
            binding.txtName.setText(message.getConversationName());
            binding.txtLastMessage.setText(message.getMessage());
            binding.txtDate.setText(getReadableDateTime(message.getDate()));

//            Bitmap bm = BitmapFactory.decodeFile("/drawable/ic_courses.png");
//            binding.imgContactUserInfo.setImageBitmap(bm);

            binding.getRoot().setOnClickListener(v->{
                CourseModel course = new CourseModel();
                course.setCourseId(message.getReceiver());
                course.setCourseDept(message.getConversationName());
                course.setCourseCode("");
                courseListener.onCourseClicked(course);
            });
        }

        private String getReadableDateTime(Date date) {
            return new SimpleDateFormat("MM/dd/yyyy - hh:mm a", Locale.getDefault()).format(date);
        }
    }
}