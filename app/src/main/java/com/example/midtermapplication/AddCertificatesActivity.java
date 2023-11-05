package com.example.midtermapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCertificatesActivity extends AppCompatActivity {
    Button btnAddCer, btnBack;
    EditText txtAddCerId, txtAddCerName, txtAddCerBody;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificates);

        btnBack = findViewById(R.id.btnBackCer);
        btnAddCer = findViewById(R.id.btnAddCer);

        txtAddCerId = findViewById(R.id.txtAddCerId);
        txtAddCerName = findViewById(R.id.txtAddCerName);
        txtAddCerBody = findViewById(R.id.txtAddCerBody);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Certificates");

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
        String id = txtAddCerId.getText().toString();
        String name = txtAddCerName.getText().toString();
        String body = txtAddCerBody.getText().toString();

        if (id.isEmpty() || name.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Please fill all field!!", Toast.LENGTH_SHORT).show();
        } else {
            writeNewCertificate(id,new Certificates(name,body,id));
        }
    }
    private void writeNewCertificate(String uniqueKey, Certificates certificates) {

        databaseReference.child(uniqueKey).setValue(certificates);

        Toast.makeText(this, "Add new certificate successfully", Toast.LENGTH_SHORT).show();

        txtAddCerId.setText("");
        txtAddCerName.setText("");
        txtAddCerBody.setText("");
    }
}