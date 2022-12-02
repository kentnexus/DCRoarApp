package com.paszlelab.dcroarapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.paszlelab.dcroarapp.R;
import com.paszlelab.dcroarapp.databinding.FriendItemLayoutBinding;
import com.paszlelab.dcroarapp.models.Student;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactAdapter_old extends RecyclerView.Adapter<ContactAdapter_old.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Student> arrayList, filterArrayList;


    public ContactAdapter_old(Context context, ArrayList<Student> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        filterArrayList = new ArrayList<>();
        filterArrayList.addAll(arrayList);
    }
    @Override
    public Filter getFilter() {
        return null;
    }

    private Filter friendsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Student> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0)
                filteredList.addAll(filterArrayList);
            else {
                String filter = charSequence.toString().toLowerCase().trim();
                for (Student student : filterArrayList) {
                    if (student.getFirstName().toLowerCase().contains(filter))
                        filteredList.add(student);
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((Collection<? extends Student>) results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        FriendItemLayoutBinding layoutBinding;

        public ViewHolder(@NotNull FriendItemLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());
            this.layoutBinding = layoutBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FriendItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.friend_item_layout, parent, false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
