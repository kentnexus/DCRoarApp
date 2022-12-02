package com.paszlelab.dcroarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.MessageItemLayoutBinding;
import com.paszlelab.dcroarapp.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private final List<Message> messages;

    public RecentConversationsAdapter(List<Message> messages) {
        this.messages = messages;
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
        Log.d("-", messages.size()+" items");
        return messages.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{
        MessageItemLayoutBinding binding;

        ConversionViewHolder(MessageItemLayoutBinding messageItemLayoutBinding){
            super(messageItemLayoutBinding.getRoot());
            binding = messageItemLayoutBinding;
        }

        void setData(Message message){
            binding.txtName.setText(message.getConversionName());
            binding.txtLastMessage.setText(message.getMessage());
            binding.txtDate.setText(getReadableDateTime(message.getDate()));
        }
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}
