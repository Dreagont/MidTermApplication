package com.example.midtermapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView txtCreate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        if (hasSavedSession()) {
            // If a session exists, go to MainActivity
            redirectToMainActivity();
        } else
            {

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    if (username.isEmpty() || password.isEmpty()) {

                        Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginApp();
                    }
                }
            });
        }

    }

    private void LoginApp() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        isValidCredentials(email,password);

    }

    private void isValidCredentials(String username, String password) {
        databaseReference.child(username.split("@")[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                        String tmp = String.valueOf(snapshot.child("password").getValue());
                        String mail = String.valueOf(snapshot.child("mail").getValue());

                        if (tmp.equals(password)) {
                            saveUserSession(username);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username",mail);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }

                } else {
                    Toast.makeText(LoginActivity.this, "Account is not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserSession(String username) {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private boolean hasSavedSession() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return preferences.getString("username", null) != null;
    }

    private void redirectToMainActivity() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username",preferences.getString("username", null));
        startActivity(intent);
        finish();
    }


}