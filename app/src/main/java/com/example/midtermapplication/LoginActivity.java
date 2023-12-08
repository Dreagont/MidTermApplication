package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView txtCreate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private AlertDialog progressDialog;

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
            String savedUsername = getUsernameFromSession();
            checkAccountStatus(savedUsername);
            yourAsyncTask().execute();
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
    private AsyncTask<Void, Void, Void> yourAsyncTask() {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Hiển thị dialog loading khi bắt đầu xử lý
                showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                dismissProgressDialog();
            }
        };
    }

    private void showProgressDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.progress_dialog, null);

            builder.setView(view);
            builder.setCancelable(false);

            progressDialog = builder.create();

            if (!isFinishing()) {
                progressDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }

    private String getUsernameFromSession() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    private void checkAccountStatus(String username) {
        DatabaseReference userReference = databaseReference.child(username.split("@")[0]);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = String.valueOf(snapshot.child("lock").getValue());

                    if ("false".equals(status)) {
                        redirectToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Account is locked by admin", Toast.LENGTH_SHORT).show();
                        clearUserSession();
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
                } else {
                    Toast.makeText(LoginActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void clearUserSession() {
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void LoginApp() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        isValidCredentials(email,password);

    }



    private void isValidCredentials(String username, String password) {
        DatabaseReference userReference = databaseReference.child(username.split("@")[0]);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tmp = String.valueOf(snapshot.child("password").getValue());
                    String mail = String.valueOf(snapshot.child("mail").getValue());
                    String lock = String.valueOf(snapshot.child("lock").getValue());

                    if (lock.equalsIgnoreCase("false")) {
                        if (tmp.equals(password)) {
                            saveUserSession(username);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", mail);
                            LocalDateTime currentTime = LocalDateTime.now();

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy ");
                            String currentTimeString = currentTime.format(formatter);

                            DatabaseReference historyReference = userReference.child("History");

                            History newHistory = new History(currentTimeString);

                            historyReference.push().setValue(newHistory);
                            startActivity(intent);
                            finish();
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }  else {
                        Toast.makeText(LoginActivity.this, "Account is locked", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Account is not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error, if any
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
        intent.putExtra("username", preferences.getString("username", null));

        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy ");
        String currentTimeString = currentTime.format(formatter);

        String username = preferences.getString("username", null).split("@")[0];
        DatabaseReference historyReference = databaseReference.child(username).child("History");

        History newHistory = new History(currentTimeString);

        historyReference.push().setValue(newHistory);

        startActivity(intent);
        finish();
    }


}