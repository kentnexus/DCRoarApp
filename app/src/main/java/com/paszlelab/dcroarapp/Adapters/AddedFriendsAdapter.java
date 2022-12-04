package com.paszlelab.dcroarapp.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.FriendItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.UserListener;
import com.paszlelab.dcroarapp.models.Friend;
import com.paszlelab.dcroarapp.models.Student;

import java.util.List;

public class AddedFriendsAdapter extends RecyclerView.Adapter<AddedFriendsAdapter.UserViewHolder>{
    private final List<Friend> friends;

    public AddedFriendsAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendItemLayoutBinding friendItemLayoutBinding = FriendItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AddedFriendsAdapter.UserViewHolder(friendItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(friends.get(position));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        FriendItemLayoutBinding binding;

        UserViewHolder(FriendItemLayoutBinding friendItemLayoutBinding) {
            super(friendItemLayoutBinding.getRoot());
            binding = friendItemLayoutBinding;
        }

        void setUserData(Friend friend) {
            binding.txtFriendName.setText(friend.getFriendFirst() + " " + friend.getFriendLast());
            binding.txtEmailAddress.setText(friend.getFriendEmail());
//            binding.imgUserProfile.setImageBitmap(getUserImage());
        }
    }
}
