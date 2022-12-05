package com.paszlelab.dcroarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.Utilities.RetrieveImage;
import com.paszlelab.dcroarapp.databinding.LeftItemLayoutBinding;
import com.paszlelab.dcroarapp.databinding.LeftItemLayoutCoursechatBinding;
import com.paszlelab.dcroarapp.databinding.RightItemLayoutBinding;
import com.paszlelab.dcroarapp.models.Message;

import java.util.List;

public class CourseChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Message> messages;
    private final String senderId, courseId;

    public CourseChatAdapter(List<Message> messages, String senderId, String courseId) {
        this.messages = messages;
        this.senderId = senderId;
        this.courseId = courseId;
    }

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT)
            return new SentMessageViewHolder(RightItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ReceivedMessageViewHolder(LeftItemLayoutCoursechatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(messages.get(position));
        } else {
            ((ReceivedMessageViewHolder) holder).setData(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("-", messages.size()+" m");
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSender().equals(senderId)) return VIEW_TYPE_SENT;
        else return VIEW_TYPE_RECEIVED;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final RightItemLayoutBinding binding;

        SentMessageViewHolder(RightItemLayoutBinding rightItemLayoutBinding) {
            super(rightItemLayoutBinding.getRoot());
            binding = rightItemLayoutBinding;
        }

        void setData(Message message) {
            binding.txtMessage.setText(message.getMessage());
            binding.txtDateTime.setText(message.getDateTime());

//            String imgSrc = message.getSender() + ".jpeg";
//            Log.d("-", imgSrc);
//            TODO: image

            RetrieveImage.getImg(binding.getRoot().getContext(), message.getSender(), binding.userMessageImg);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final LeftItemLayoutCoursechatBinding binding;

        ReceivedMessageViewHolder(LeftItemLayoutCoursechatBinding leftItemLayoutCoursechatBinding) {
            super(leftItemLayoutCoursechatBinding.getRoot());
            binding = leftItemLayoutCoursechatBinding;
        }

        void setData(Message message) {
            binding.txtMessage.setText(message.getMessage());
            binding.txtDateTime.setText(message.getDateTime());
            binding.txtChatEmail.setText(message.getConversationName());

            String imgSrc = message.getSender() + ".jpeg";
//            Log.d("-", imgSrc);
//            TODO: image

            RetrieveImage.getImg(binding.getRoot().getContext(), message.getSender(), binding.userMessageImg);
        }
    }
}
