package com.paszlelab.dcroarapp.Adapters;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.paszlelab.dcroarapp.databinding.FriendItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.UserListener;
import com.paszlelab.dcroarapp.models.Student;

import java.io.File;
import java.util.List;

public class FindUsersAdapter extends RecyclerView.Adapter<FindUsersAdapter.UserViewHolder> {

    private final List<Student> students;
    private final UserListener userListener;
    private StorageReference storageReference;

    public FindUsersAdapter(List<Student> students, UserListener userListener) {
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

        UserViewHolder(FriendItemLayoutBinding friendItemLayoutBinding) {
            super(friendItemLayoutBinding.getRoot());
            binding = friendItemLayoutBinding;
        }

        void setUserData(Student student) {
            binding.txtFriendName.setText(student.getFirstName() + " " + student.getLastName());
            binding.txtEmailAddress.setText(student.getEmailAddress());
//            binding.imgUserProfile.setImageBitmap(getUserImage(student.getImg()));

            String imgSrc = student.getImg();

            try {
                storageReference = FirebaseStorage.getInstance()
                        .getReference().child("profileImages/" + imgSrc);

                final File localFile = File.createTempFile(student.getId(),"jpeg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                binding.imgUserProfile.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } catch(Exception e){}

            binding.getRoot().setOnClickListener(v -> {
                userListener.onUserClicked(student);
            });
        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
