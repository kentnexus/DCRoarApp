package com.paszlelab.dcroarapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.Activity.CoursePage;
import com.paszlelab.dcroarapp.Fragments.CoursesFragment;
import com.paszlelab.dcroarapp.databinding.CourseItemLayoutBinding;
import com.paszlelab.dcroarapp.models.CourseModel;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {


    // creating a variable for array list and context.
    private ArrayList<CourseModel> courseModelArrayList;

    //creating click listener
    private CourseClickListener listener;
    private Context context;

    // creating a constructor for our variables.
    public CourseAdapter(ArrayList<CourseModel> courseModelArrayList, CourseClickListener listener, CoursesFragment context) {
        this.courseModelArrayList = courseModelArrayList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CourseItemLayoutBinding courseLayoutBinding = CourseItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CourseAdapter.CourseViewHolder(courseLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.setCourseData(courseModelArrayList.get(position));
        }



    @Override
    public int getItemCount()  {
        return courseModelArrayList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View view;
        CourseItemLayoutBinding binding;

        public CourseViewHolder(CourseItemLayoutBinding courseLayoutBinding) {
            super(courseLayoutBinding.getRoot());
            binding = courseLayoutBinding;
        }



        void setCourseData(CourseModel courseModelArrayList){
            binding.idTVCourseCode.setText(courseModelArrayList.getCourseCode());
            binding.idTVCourseName.setText(courseModelArrayList.getCourseName());
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }


    public interface CourseClickListener{
        void onClick(View v, int position);
    }
}
