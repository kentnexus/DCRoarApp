package com.paszlelab.dcroarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.LeftItemLayoutBinding;
import com.paszlelab.dcroarapp.databinding.RightItemLayoutBinding;
import com.paszlelab.dcroarapp.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //TODO: add picture

    private final List<Message> messages;
    private final String senderId;

    public MessageAdapter(List<Message> messages, String senderId) {
        this.messages = messages;
        this.senderId = senderId;
    }

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT)
            return new SentMessageViewHolder(
                    RightItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false)
        );
        else
            return new ReceivedMessageViewHolder(
                    LeftItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent, false)
            );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)== VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(messages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("-", messages.size()+"");
        return messages.size();
    }

    @Override
    public int getItemViewType(int position){
        if(messages.get(position).getSender().equals(senderId))
            return VIEW_TYPE_SENT;
        else
            return VIEW_TYPE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final RightItemLayoutBinding binding;

        SentMessageViewHolder(RightItemLayoutBinding rightItemLayoutBinding){
            super(rightItemLayoutBinding.getRoot());
            binding = rightItemLayoutBinding;
        }

        void setData(Message message){
            binding.txtMessage.setText(message.getMessage());
            binding.txtDateTime.setText(message.getDateTime());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final LeftItemLayoutBinding binding;

        ReceivedMessageViewHolder(LeftItemLayoutBinding leftItemLayoutBinding){
            super(leftItemLayoutBinding.getRoot());
            binding = leftItemLayoutBinding;
        }

        void setData(Message message){
            binding.txtMessage.setText(message.getMessage());
            binding.txtDateTime.setText(message.getDateTime());
        }
    }
}
