package com.example.midtermapplication;

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
    LinearLayout btnBack;
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

        txtAddCerName = findViewById(R.id.txtAddCerName);
        txtAddCerBody = findViewById(R.id.txtAddCerBody);

        // Apply the NoLineBreaksInputFilter to the EditText fields
        txtAddCerName.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});
        txtAddCerBody.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Certificates");
        studentId = getIntent().getStringExtra("studentId");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddCer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCertificates();
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
                        // Write was successful
                        Toast.makeText(AddCertificatesActivity.this, "Certificate added successfully", Toast.LENGTH_SHORT).show();
                        txtAddCerName.setText("");
                        txtAddCerBody.setText("");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Write failed
                        Toast.makeText(AddCertificatesActivity.this, "Failed to add certificate", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
