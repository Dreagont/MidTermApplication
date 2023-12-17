package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCertificatesActivity extends AppCompatActivity {
    Button btnAddCer;
    LinearLayout btnBack, btnDeleteCertificate;
    EditText txtAddCerName, txtAddCerBody;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificates);

        btnBack = findViewById(R.id.btnBackCer);
        btnAddCer = findViewById(R.id.btnAddCer);
        btnDeleteCertificate = findViewById(R.id.btnDeleteCertificate);

        txtAddCerName = findViewById(R.id.txtAddCerName);
        txtAddCerBody = findViewById(R.id.txtAddCerBody);

        String userRole = getIntent().getStringExtra("userRole");
        studentId = getIntent().getStringExtra("studentId");
        String method = getIntent().getStringExtra("method");
        Certificates certificates = getIntent().getParcelableExtra("certificate");
        String key = getIntent().getStringExtra("key");

        if (userRole.equalsIgnoreCase("Employee")) {

            txtAddCerName.setClickable(false);
            txtAddCerName.setFocusable(false);
            txtAddCerName.setCursorVisible(false);

            txtAddCerBody.setClickable(false);
            txtAddCerBody.setFocusable(false);
            txtAddCerBody.setCursorVisible(false);

            btnAddCer.setEnabled(false);
            btnAddCer.setAlpha(0.5f);

            btnDeleteCertificate.setAlpha(0.5f);
            btnDeleteCertificate.setEnabled(false);

        }

        txtAddCerName.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});
        txtAddCerBody.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});
        btnDeleteCertificate.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students").child(studentId).child(("Certificates"));

        if (method.equalsIgnoreCase("edit")) {
            txtAddCerBody.setText(certificates.getBody());
            txtAddCerName.setText(certificates.getName());
            btnDeleteCertificate.setVisibility(View.VISIBLE);
            btnAddCer.setText("Update");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (method.equalsIgnoreCase("edit")) {
                    Certificates certificates1 = new Certificates(txtAddCerName.getText().toString(),txtAddCerBody.getText().toString(),studentId);
                    updateCertificate(certificates1, key);
                } else {
                    addCertificates();
                }

            }
        });

        btnDeleteCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCertificatesActivity.this);
                builder.setTitle("Delete certificate");
                builder.setMessage("Do you want to delete this certificate?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference certificateRef = firebaseDatabase.getReference("Students").child(studentId).child("Certificates").child(key);
                        certificateRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddCertificatesActivity.this, "Delete certificate successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                }
                        });
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
    }

    private void updateCertificate(Certificates certificates1, String key) {
        DatabaseReference certificateRef = firebaseDatabase.getReference("Students").child(studentId).child("Certificates").child(key);

        certificateRef.setValue(certificates1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCertificatesActivity.this, "Certificate updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddCertificatesActivity.this, "Failed to update certificate", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addCertificates() {
        String name = txtAddCerName.getText().toString().trim();
        String body = txtAddCerBody.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(body)) {
            Toast.makeText(this, "Please fill all fields!!", Toast.LENGTH_SHORT).show();
        } else {
            writeNewCertificate(new Certificates(name, body, studentId));
        }
    }

    private void writeNewCertificate(Certificates certificates) {
        DatabaseReference newCertificateRef = databaseReference.push();
        newCertificateRef.setValue(certificates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCertificatesActivity.this, "Certificate added successfully", Toast.LENGTH_SHORT).show();
                        txtAddCerName.setText("");
                        txtAddCerBody.setText("");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddCertificatesActivity.this, "Failed to add certificate", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
