package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CertificatesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Certificates> certificates;
    private CertificatesAdapter certificatesAdapter;
    ImageView btnAdd, btnImport, btnExport;
    private String username;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView btnSortByName;
    String studentId, key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates_list);

        LinearLayout btnBack = findViewById(R.id.btnBackStudentList);
        SearchView searchBar = findViewById(R.id.certificate_search_bar);

        btnSortByName = findViewById(R.id.btnSortByName);
        btnImport = findViewById(R.id.btnAddCerCsv);
        btnExport = findViewById(R.id.btnExpCerCsv);

        btnSortByName.setBackgroundColor(Color.TRANSPARENT);

        final boolean[] nameAsc = {true};

        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAsc[0] = !nameAsc[0];

                if (nameAsc[0]) {
                    btnSortByName.setText("Sort name: Asc");
                    UserSortUtil.sortCertificatesByNameAsc(certificates);
                } else {
                    btnSortByName.setText("Sort name: Desc");
                    UserSortUtil.sortCertificatesByNameDesc(certificates);
                }
                btnSortByName.setBackgroundResource(R.drawable.forward_box);

                certificatesAdapter.notifyDataSetChanged();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FirebaseApp.initializeApp(this);

        btnAdd = findViewById(R.id.btnAddCer);

        String userRole = getIntent().getStringExtra("userRole");
        username = getIntent().getStringExtra("username");
        String currentUser = username;
        studentId = getIntent().getStringExtra("studentId");

        if (userRole.equalsIgnoreCase("employee")) {
            btnAdd.setVisibility(View.GONE);
            btnExport.setVisibility(View.GONE);
            btnImport.setVisibility(View.GONE);
        }

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificatesListActivity.this, CsvReaderActivity.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("method", "addCertificate");
                startActivity(intent);
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CertificatesListActivity.this);
                builder.setTitle("Export Certificate Data");
                builder.setMessage("Do you want to export certificate data to CSV?");
                builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveCertificates(studentId);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CertificatesListActivity.this, AddCertificatesActivity.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("userRole",userRole);
                intent.putExtra("method","create");
                startActivity(intent);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students").child(studentId).child("Certificates");
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

            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    certificatesAdapter.getFilter().filter(newText);
                    return true;
                }
            });

            certificatesAdapter.setOnItemClickListener(new CertificatesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Certificates certificate) {
                    Intent intent = new Intent(CertificatesListActivity.this, AddCertificatesActivity.class);
                    intent.putExtra("certificate", certificate);
                    intent.putExtra("studentId", studentId);
                    intent.putExtra("userRole", userRole);
                    intent.putExtra("method", "edit");

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Students").child(studentId).child("Certificates");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                if (String.valueOf(childSnapshot.child("name").getValue()).equals(certificate.getName())) {
                                    key = childSnapshot.getKey();

                                    intent.putExtra("key", key);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            });
            recyclerView.setAdapter(certificatesAdapter);
        } else {
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
            }
        });
    }

    private void saveCertificates(String studentId) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students").child(studentId).child("Certificates");

        List<Certificates> certificatesList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Certificates tmp = childSnapshot.getValue(Certificates.class);
                        certificatesList.add(tmp);
                    }

                    writeStudentDataToCsv(certificatesList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void writeStudentDataToCsv(List<Certificates> studentList) {
        String fileName = "certificateOut.csv";
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;

        try {
            File file = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String dataToWrite = "";

            for (Certificates student : studentList) {
                dataToWrite = dataToWrite + student.getName() + "," + student.getBody() + "\n";
            }

            writer.write(dataToWrite);
            writer.close();

            Toast.makeText(CertificatesListActivity.this, "CSV file saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(CertificatesListActivity.this, "Error saving CSV file", Toast.LENGTH_SHORT).show();
        }
    }

}
