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

public class CertificatesAdapter extends RecyclerView.Adapter<CertificatesAdapter.CertificatesHolder> implements Filterable {

    private List<Certificates> certificates;
    private List<Certificates> originalCertificates;
    private Context context;
    private AdapterView.OnItemClickListener adapterViewListener;

    public CertificatesAdapter(Context context, List<Certificates> certificates) {
        this.certificates = certificates;
        this.originalCertificates = certificates;
        this.context = context;
    }

    @NonNull
    @Override
    public CertificatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.certificate_layout, parent, false);
        return new CertificatesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificatesHolder holder, int position) {
        Certificates certificate = certificates.get(position);
        holder.txtCerName.setText(certificate.getName());
        holder.txtCerBody.setText(certificate.getBody());
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();


                if (filterPattern.isEmpty()) {
                    certificates = originalCertificates;
                } else {
                    List<Certificates> filteredList = new ArrayList<>();

                    for (Certificates user : originalCertificates) {
                        if (user.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(user);
                        }
                    }
                    certificates = filteredList;
                }

                FilterResults results = new FilterResults();
                results.values = certificates;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                certificates = (List<Certificates>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface OnItemClickListener {
        void onItemClick(Certificates user);
    }

    public void setOnItemClickListener(CertificatesAdapter.OnItemClickListener listener) {
        adapterViewListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClick(certificates.get(position));
                }
            }
        };
    }

    class CertificatesHolder extends RecyclerView.ViewHolder {

        TextView txtCerName, txtCerBody;

        public CertificatesHolder(@NonNull View itemView) {
            super(itemView);
            txtCerName = itemView.findViewById(R.id.tvCerName);
            txtCerBody = itemView.findViewById(R.id.tvCerBody);

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
