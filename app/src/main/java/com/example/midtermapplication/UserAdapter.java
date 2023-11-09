package com.example.midtermapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CertificatesHolder> {
    private List<User> users;
    private Context context;

    public UserAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public CertificatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(this.context).inflate(R.layout.user_layout, parent,false);

        return new CertificatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesHolder holder, int position) {
        User user =users.get(position);
        holder.txtUserName.setText(user.getName());
        holder.txtUserMail.setText(user.getMail());
        holder.txtUserRole.setText(user.getRole());
        holder.status.setChecked(user.isLock());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class CertificatesHolder extends RecyclerView.ViewHolder {

        TextView txtUserName, txtUserMail, txtUserRole;
        Switch status;
        public CertificatesHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.userStatus);
            txtUserName = itemView.findViewById(R.id.tvUserName);
            txtUserMail = itemView.findViewById(R.id.tvMail);
            txtUserRole = itemView.findViewById(R.id.tvRole);
        }
    }
}
