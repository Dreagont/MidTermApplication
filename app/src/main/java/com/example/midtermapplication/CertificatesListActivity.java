package com.example.midtermapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CertificatesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Certificates> certificates;
    private CertificatesAdapter certificatesAdapter;
    ImageView btnAdd;
    private String username;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates_list);

        FirebaseApp.initializeApp(this);

        btnAdd = findViewById(R.id.btnAddCer);
        username = getIntent().getStringExtra("username");
        String currentUser = username;
        String studentId = getIntent().getStringExtra("studentId");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificatesListActivity.this, AddCertificatesActivity.class);
                intent.putExtra("studentId",studentId);
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Certificates");
        if (currentUser != null) {
            certificates = new ArrayList<>();

            String uniqueKey = studentId;

            retrieveDataAndDisplay(uniqueKey);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

            recyclerView = findViewById(R.id.recycleCert);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

            certificatesAdapter = new CertificatesAdapter(this, certificates);
            recyclerView.setAdapter(certificatesAdapter);
        } else {
            // Handle the case where the user is not logged in
        }
    }

    private void retrieveDataAndDisplay(String uniqueKey) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    certificates.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String studentCerId = String.valueOf(childSnapshot.child("studentId").getValue());
                        String mail = String.valueOf(childSnapshot.child("body").getValue());
                        String name = String.valueOf(childSnapshot.child("name").getValue());

                        if (uniqueKey.equals(studentCerId)) {
                            certificates.add(new Certificates(name, mail));
                        }
                    }
                    certificatesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }
}
