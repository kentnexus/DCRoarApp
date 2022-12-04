package com.paszlelab.dcroarapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.databinding.CourseItemLayoutBinding;
import com.paszlelab.dcroarapp.listeners.CourseListener;
import com.paszlelab.dcroarapp.models.CourseModel;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {


    // creating a variable for array list and context.
    private List<CourseModel> courseModelArrayList;

    //creating click listener
    private CourseListener courseListener;

    // creating a constructor for our variables.
    public CourseAdapter(List<CourseModel> courseModelArrayList, CourseListener courseListener) {
        this.courseModelArrayList = courseModelArrayList;
        this.courseListener = courseListener;
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CourseItemLayoutBinding courseLayoutBinding = CourseItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CourseAdapter.CourseViewHolder(courseLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.setCourseData(courseModelArrayList.get(position));
    }


    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        private View view;
        CourseItemLayoutBinding binding;

        public CourseViewHolder(CourseItemLayoutBinding courseLayoutBinding) {
            super(courseLayoutBinding.getRoot());
            binding = courseLayoutBinding;
        }


        void setCourseData(CourseModel courseModelArrayList) {
            binding.idTVCourseCode.setText(courseModelArrayList.getCourseDept() + " " + courseModelArrayList.getCourseCode());
            Log.d("-", courseModelArrayList.getCourseCode());
            binding.idTVCourseName.setText(courseModelArrayList.getCourseName());

            binding.getRoot().setOnClickListener(v->{
                courseListener.onCourseClicked(courseModelArrayList);
            });
        }

//        @Override
//        public void onClick(View view) {
//            listener.onClick(view, getAdapterPosition());
//            listener.onClick();
//        }
    }


//    public interface CourseClickListener {
//        void onClick(View v, int position);
//    }
}
