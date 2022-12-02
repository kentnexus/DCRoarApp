package com.paszlelab.dcroarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.MessageItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.ConversationListener;
import com.paszlelab.dcroarapp.models.Message;
import com.paszlelab.dcroarapp.models.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private final List<Message> messages;
    private final ConversationListener conversationListener;

    public RecentConversationsAdapter(List<Message> messages, ConversationListener conversationListener) {
        this.messages = messages;
        this.conversationListener = conversationListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                MessageItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(messages.get(position));
    }

    @Override
    public int getItemCount() {
//        Log.d("-", messages.size()+" items");
        return messages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{
        MessageItemLayoutBinding binding;

        ConversionViewHolder(MessageItemLayoutBinding messageItemLayoutBinding){
            super(messageItemLayoutBinding.getRoot());
            binding = messageItemLayoutBinding;
        }

        void setData(Message message){
//TODO:add picture
            binding.txtName.setText(message.getConversionName());
            binding.txtLastMessage.setText(message.getMessage());
            binding.txtDate.setText(getReadableDateTime(message.getDate()));
            binding.getRoot().setOnClickListener(v->{
                Student student = new Student();
                student.setId(message.getConversionId());
                student.setEmailAddress(message.getConversionName());
                conversationListener.onConversationClicked(student);
            });
        }
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}
