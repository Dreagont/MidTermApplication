package com.example.midtermapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CertificatesHolder> implements Filterable {
    private List<User> users;
    private Context context;
    private List<User> originalUsers;

    private AdapterView.OnItemClickListener adapterViewListener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();


                if (filterPattern.isEmpty()) {
                    users = originalUsers;
                } else {
                    List<User> filteredList = new ArrayList<>();

                    for (User user : originalUsers) {
                        if (user.getName().toLowerCase().contains(filterPattern)) {
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
                users = (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
        this.originalUsers = users;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
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
        View view = LayoutInflater.from(this.context).inflate(R.layout.user_layout, parent, false);
        return new CertificatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesHolder holder, int position) {
        User user = users.get(position);
        holder.txtUserName.setText(user.getName());
        holder.txtUserMail.setText(user.getMail());
        holder.txtUserRole.setText(user.getRole());

        String userRole = holder.txtUserRole.getText().toString();
        if (userRole.equalsIgnoreCase("admin")) {
            holder.userIcon.setImageResource(R.drawable.admin_icon);
        } else if (userRole.equalsIgnoreCase("manager")) {
            holder.userIcon.setImageResource(R.drawable.manager_icon);
        } else if (userRole.equalsIgnoreCase("employee")) {
            holder.userIcon.setImageResource(R.drawable.employee_icon);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class CertificatesHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserMail, txtUserRole;
        ImageView userIcon;

        public CertificatesHolder(@NonNull View itemView) {
            super(itemView);

            userIcon = itemView.findViewById(R.id.userIcon);
            txtUserName = itemView.findViewById(R.id.tvUserName);
            txtUserMail = itemView.findViewById(R.id.tvMail);
            txtUserRole = itemView.findViewById(R.id.tvRole);

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

