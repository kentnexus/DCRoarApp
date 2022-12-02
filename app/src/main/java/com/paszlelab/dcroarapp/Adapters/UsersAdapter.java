package com.paszlelab.dcroarapp.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.FriendItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.UserListener;
import com.paszlelab.dcroarapp.models.Student;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<Student> students;
    private final UserListener userListener;

    public UsersAdapter(List<Student> students,UserListener userListener){
        this.students = students;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendItemLayoutBinding friendItemLayoutBinding = FriendItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(friendItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(students.get(position));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        FriendItemLayoutBinding binding;

        UserViewHolder(FriendItemLayoutBinding friendItemLayoutBinding){
            super(friendItemLayoutBinding.getRoot());
            binding = friendItemLayoutBinding;
        }

        void setUserData(Student student){
            binding.txtFriendName.setText(student.getFirstName()+" "+student.getLastName());
            binding.txtEmailAddress.setText(student.getEmailAddress());
//            binding.imgUserProfile.setImageBitmap(getUserImage());
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(student));
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}
