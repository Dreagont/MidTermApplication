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

public class CertificatesAdapter extends RecyclerView.Adapter<CertificatesAdapter.CertificatesHolder> {
    private List<Certificates> certificates;
    private Context context;

    public CertificatesAdapter(Context context, List<Certificates> certificates) {
        this.certificates = certificates;
        this.context = context;
    }

    @NonNull
    @Override
    public CertificatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(this.context).inflate(R.layout.certificate_layout, parent,false);

        return new CertificatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesHolder holder, int position) {
        Certificates certificate =certificates.get(position);

        holder.txtCerName.setText(certificate.getName());
        holder.txtCerBody.setText(certificate.getBody());
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    class CertificatesHolder extends RecyclerView.ViewHolder {

        TextView txtCerName, txtCerBody;
        public CertificatesHolder(@NonNull View itemView) {
            super(itemView);
            txtCerName = itemView.findViewById(R.id.tvCerName);
            txtCerBody = itemView.findViewById(R.id.tvCerBody);
        }
    }
}
