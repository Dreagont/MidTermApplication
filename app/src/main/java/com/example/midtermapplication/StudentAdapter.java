package com.example.midtermapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CertificatesHolder> implements Filterable {
    private List<Student> users;
    private Context context;
    private AdapterView.OnItemClickListener adapterViewListener;
    private List<Student> originalUsers;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();


                if (filterPattern.isEmpty()) {
                    users = originalUsers;
                } else {
                    List<Student> filteredList = new ArrayList<>();

                    for (Student user : originalUsers) {
                        if (user.getStudentName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(user);
                        }
                    }
                    users = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = users;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users = (List<Student>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public StudentAdapter(Context context, List<Student> users) {
        this.users = users;
        this.context = context;
        this.originalUsers = users;
    }

    public interface OnItemClickListener {
        void onItemClick(Student user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        adapterViewListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClick(users.get(position));
                }
            }
        };
    }

    @NonNull
    @Override
    public CertificatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.student_layout, parent, false);
        return new CertificatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesHolder holder, int position) {
        Student user = users.get(position);
        holder.txtStudentName.setText(user.getStudentName());
        holder.txtStudentId.setText(user.getStudentId());
        holder.txtStudentMail.setText(user.getStudentEmail());


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class CertificatesHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName, txtStudentMail, txtStudentId;

        public CertificatesHolder(@NonNull View itemView) {
            super(itemView);

            txtStudentId = itemView.findViewById(R.id.tvStudentId);
            txtStudentName = itemView.findViewById(R.id.tvStudentName);
            txtStudentMail = itemView.findViewById(R.id.tvStudentMail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapterViewListener != null) {
                        adapterViewListener.onItemClick(null, itemView, getAdapterPosition(), getItemId());
                    }
                }
            });
        }
    }
}

