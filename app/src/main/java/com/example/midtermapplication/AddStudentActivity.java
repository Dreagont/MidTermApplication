package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity {
    LinearLayout btnBackStudent, btnDeleteStudent, studentCerList, addStudentLayout;
    EditText txtStudentId, txtStudentName, txtStudentEmail;
    Button btnSaveStudent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String username;
    Student editStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        btnBackStudent = findViewById(R.id.btnBackStudent);
        btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
        studentCerList = findViewById(R.id.studentCerList);
        txtStudentId = findViewById(R.id.txtStudentId);
        txtStudentName = findViewById(R.id.txtStudentName);
        txtStudentEmail = findViewById(R.id.txtStudentEmail);
        btnSaveStudent = findViewById(R.id.btnSaveStudent);
        addStudentLayout = findViewById(R.id.addStudentLayout);

        // Apply the NoLineBreaksInputFilter to the EditText fields
        txtStudentId.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});
        txtStudentName.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});
        txtStudentEmail.setFilters(new InputFilter[]{new NoLineBreaksInputFilter()});

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students");

        String method = getIntent().getStringExtra("method");
        String userRole = getIntent().getStringExtra("userRole");

        if (userRole.equalsIgnoreCase("Employee")) {
            txtStudentId.setClickable(false);
            txtStudentEmail.setClickable(false);
            txtStudentId.setFocusable(false);
            txtStudentId.setCursorVisible(false);

            txtStudentName.setClickable(false);
            txtStudentName.setFocusable(false);
            txtStudentName.setCursorVisible(false);

            txtStudentEmail.setClickable(false);
            txtStudentEmail.setFocusable(false);
            txtStudentEmail.setCursorVisible(false);

            btnSaveStudent.setEnabled(false);
            btnSaveStudent.setAlpha(0.5f);

            btnDeleteStudent.setAlpha(0.5f);
            btnDeleteStudent.setEnabled(false);

        }
        username = getIntent().getStringExtra("username");
        editStudent = getIntent().getParcelableExtra("student");

        if (method != null && method.equals("edit")) {
            studentCerList.setVisibility(View.VISIBLE);
            btnSaveStudent.setText("Update");

            btnDeleteStudent.setVisibility(View.VISIBLE);

            txtStudentEmail.setText(editStudent.getStudentEmail());
            txtStudentName.setText(editStudent.getStudentName());
            txtStudentId.setText(editStudent.getStudentId());
        }

        String oldId = txtStudentId.getText().toString();
        studentCerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudentActivity.this, CertificatesListActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("studentId", editStudent.getStudentId());
                intent.putExtra("userRole",userRole);
                startActivity(intent);
            }
        });

        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtStudentName.getText().toString();
                String email = txtStudentEmail.getText().toString();
                String id = txtStudentId.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentActivity.this);
                builder.setTitle("Delete student");
                builder.setMessage("Do you want to delete this student?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStudent(fullName, email, id);
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
        btnSaveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentId = txtStudentId.getText().toString().trim();
                String studentName = txtStudentName.getText().toString().trim();
                String studentMail = txtStudentEmail.getText().toString().trim();

                if (TextUtils.isEmpty(studentId) || TextUtils.isEmpty(studentName) || TextUtils.isEmpty(studentMail)) {
                    Toast.makeText(AddStudentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!NoLineBreaksInputFilter.isValidEmail(studentMail)) {
                    Toast.makeText(AddStudentActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (method.equals("create")) {
                    addStudent(studentId, studentName, studentMail);
                    Toast.makeText(AddStudentActivity.this, "Add student success", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    EditStudent(studentId, studentName, studentMail, oldId);
                    if (!oldId.equals(studentId)) {
                        deleteStudentNoToast(oldId);
                    }
                    finish();
                }
            }
        });

        btnBackStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void deleteStudentNoToast(String oldId) {
        String uniqueKey = oldId;
        databaseReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
        finish();
    }

    private void EditStudent(String studentId, String studentName, String studentMail, String oldId) {
        String uniqueKey = studentId;

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("studentId", studentId);
        updateMap.put("studentName", studentName);
        updateMap.put("studentEmail", studentMail);
        databaseReference.child(uniqueKey).updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddStudentActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteStudent(String fullName, String email, String id) {
        String uniqueKey = id;
        databaseReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddStudentActivity.this, "Delete student successfully", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    private void addStudent(String studentId, String studentName, String studentMail) {
        databaseReference.child(studentId).setValue(new Student(studentId, studentName, studentMail));
    }

}
