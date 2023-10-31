package com.example.midtermapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText txtFullName, txtEmail, txtPassword, txtPhone, txtAge, txtRole;
    Button btnRegister;
    CheckBox checkBoxStatus;
    RadioGroup gRole;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtPhone = findViewById(R.id.txtPhone);
        txtAge = findViewById(R.id.txtAge);
        gRole = findViewById(R.id.gRole);

        checkBoxStatus = findViewById(R.id.checkboxStatus);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtFullName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String phone = txtPhone.getText().toString();
                String age = txtAge.getText().toString();
                String role = gRole.getCheckedRadioButtonId() ==R.id.roleManager ? "Manager" : "Student";

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || age.isEmpty() || role.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    SignUp(fullName, email, password, phone, age, role);
                }
            }
        });
    }

    private void SignUp(String fullName, String email, String password, String phone, String age, String role) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, phone, email, password, Integer.parseInt(age), role, checkBoxStatus.isChecked());

                            String uniqueKey = (email.split("@"))[0];


                            writeNewUser(uniqueKey, user);

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String uniqueKey, User user) {
        databaseReference.child(uniqueKey).setValue(user);
    }
}
