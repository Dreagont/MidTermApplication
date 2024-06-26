package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText txtFullName, txtEmail, txtPassword, txtPhone, txtAge, txtRole;
    Button btnRegister;
    LinearLayout btnBackUser, editOption;
    TextView btnDeleteUser,changeProPicture, btnHistory;
    CheckBox checkBoxStatus;
    RadioGroup gRole;
    ImageView profilePicture;
    private FirebaseStorage firebaseStorage;
    private StorageReference saveStorageReference, loadStorageReference;
    Uri selectedImageUri;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User editUser = new User();

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
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        profilePicture = findViewById(R.id.profilePicture);
        checkBoxStatus = findViewById(R.id.checkboxStatus);
        changeProPicture = findViewById(R.id.changeProPicture);
        editOption = findViewById(R.id.editOption);
        btnHistory = findViewById(R.id.btnHistory);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackUser = findViewById(R.id.btnBackUser);

        String userRole = getIntent().getStringExtra("userRole");
        if (!userRole.equalsIgnoreCase("admin")) {
            txtFullName.setClickable(false);
            txtFullName.setFocusable(false);
            txtFullName.setCursorVisible(false);

            txtEmail.setClickable(false);
            txtEmail.setFocusable(false);
            txtEmail.setCursorVisible(false);

            txtPassword.setClickable(false);
            txtPassword.setFocusable(false);
            txtPassword.setCursorVisible(false);

            txtPhone.setClickable(false);
            txtPhone.setFocusable(false);
            txtPhone.setCursorVisible(false);

            txtAge.setClickable(false);
            txtAge.setFocusable(false);
            txtAge.setCursorVisible(false);

            gRole.setClickable(false);
            gRole.setEnabled(false);
            gRole.setFocusable(false);

            btnDeleteUser.setClickable(false);
            btnDeleteUser.setEnabled(false);
            btnDeleteUser.setAlpha(0.5f);

            btnRegister.setEnabled(false);
            btnRegister.setAlpha(0.5f);

            changeProPicture.setEnabled(false);
            changeProPicture.setClickable(false);

            findViewById(R.id.PasswordField).setVisibility(View.GONE);
        }

        int maxLengthFullName = 50;
        int maxLengthEmail = 100;
        int maxLengthPassword = 20;
        int maxLengthPhone = 15;
        int maxLengthAge = 3;

        txtFullName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthFullName)});
        txtEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthEmail)});
        txtPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthPassword)});
        txtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthPhone)});
        txtAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthAge)});

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        saveStorageReference = FirebaseStorage.getInstance().getReference("profile_pictures");




        changeProPicture.setVisibility(View.GONE);

        txtFullName.setFilters(new InputFilter[] {new NoLineBreaksInputFilter()});
        txtEmail.setFilters(new InputFilter[] {new NoLineBreaksInputFilter()});

        editOption.setVisibility(View.GONE);

        String method = getIntent().getStringExtra("method");

        if (method != null && method.equals("edit")) {
            btnRegister.setText("Update");
            editUser = getIntent().getParcelableExtra("user");
            changeProPicture.setVisibility(View.VISIBLE);
            editOption.setVisibility(View.VISIBLE);

            txtFullName.setText(editUser.getName());
            txtEmail.setText(editUser.getMail());
            txtPassword.setText(editUser.getPassword());
            txtPhone.setText(editUser.getPhone());
            txtAge.setText(String.valueOf(editUser.getAge()));
            checkBoxStatus.setChecked(editUser.isLock());


            if (editUser.getRole().equals("Manager")) {
                gRole.check(R.id.roleManager);
            } else {
                gRole.check(R.id.roleStudent);
            }

            loadProfileImage(editUser.getImageUrl());
            changeProPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.with(RegisterActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                }
            });

            btnHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginHistoryActivity.class);
                    intent.putExtra("username",txtEmail.getText().toString().split("@")[0]);
                    startActivity(intent);
                }
            });
        }
        String oldEmail = txtEmail.getText().toString();
        btnBackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtFullName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String phone = txtPhone.getText().toString();
                String age = txtAge.getText().toString();
                String role = gRole.getCheckedRadioButtonId() == R.id.roleManager ? "Manager" : "Employee";

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Delete user");
                builder.setMessage("Do you want to delete this user?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(fullName, email, password, phone, age, role);
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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = txtFullName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String age = txtAge.getText().toString().trim();
                String role = gRole.getCheckedRadioButtonId() == R.id.roleManager ? "Manager" : "Employee";

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || age.isEmpty() || role.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (NoLineBreaksInputFilter.containsSpecialCharacter(fullName)) {
                    Toast.makeText(RegisterActivity.this, "Username can not contains special characters", Toast.LENGTH_SHORT).show();
                } else if (!NoLineBreaksInputFilter.isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                } else {
                    if (method.equals("create")) {
                        SignUp(fullName, email, password, phone, age, role);
                        clearText();
                    } else {
                        edit(fullName, email, password, phone, age, role, oldEmail);
                        if (!oldEmail.equals(email)) {
                            deleteUserNoToast(fullName, email, password, phone, age, role, oldEmail);
                        }
                        clearText();
                    }
                }
            }
        });
    }

    private void loadProfileImage(String imageUrl) {
        firebaseStorage = FirebaseStorage.getInstance();

        loadStorageReference = firebaseStorage.getReference("profile_pictures/" + imageUrl);
        loadStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imagePath = uri.toString();

                Picasso.get().load(imagePath).placeholder(R.drawable.baseline_downloading_24).into(profilePicture);

            }
        });
    }

    private void deleteUserNoToast(String fullName, String email, String password, String phone, String age, String role, String oldEmail) {
        String uniqueKey = (oldEmail.split("@"))[0];
        User user = new User(fullName, phone, email, password, Integer.parseInt(age), role, checkBoxStatus.isChecked());

        databaseReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        });
        finish();
    }

    private void edit(String fullName, String email, String password, String phone, String age, String role, String oldEmail) {
        String uniqueKey = (email.split("@"))[0];

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("name", fullName);
        updateMap.put("phone", phone);
        updateMap.put("mail", email);
        updateMap.put("password", password);
        updateMap.put("age", Integer.parseInt(age));
        updateMap.put("role", role);
        updateMap.put("lock", checkBoxStatus.isChecked());

        databaseReference.child(oldEmail.split("@")[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String oldUrl = String.valueOf(snapshot.child("imageUrl").getValue());
                updateMap.put("imageUrl", oldUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(uniqueKey).updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to update user.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearText() {
        ((EditText) findViewById(R.id.txtFullName)).setText("");
        ((EditText) findViewById(R.id.txtPhone)).setText("");
        ((EditText) findViewById(R.id.txtEmail)).setText("");
        ((EditText) findViewById(R.id.txtAge)).setText("");
        ((EditText) findViewById(R.id.txtPassword)).setText("");
        ((RadioGroup) findViewById(R.id.gRole)).clearCheck();
    }

    private void SignUp(String fullName, String email, String password, String phone, String age, String role) {
        User user = new User(fullName, phone, email, password, Integer.parseInt(age), role, checkBoxStatus.isChecked());

        String uniqueKey = (email.split("@"))[0];

        databaseReference.child(uniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                } else {
                    writeNewUser(uniqueKey, user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void writeNewUser(String uniqueKey, User user) {
        databaseReference.child(uniqueKey).setValue(user);
        Toast.makeText(this, "Add account successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteUser(String fullName, String email, String password, String phone, String age, String role) {
        String uniqueKey = (email.split("@"))[0];
        User user = new User(fullName, phone, email, password, Integer.parseInt(age), role, checkBoxStatus.isChecked());

        databaseReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            profilePicture.setImageURI(uri);

            String imageName = txtEmail.getText().toString().split("@")[0] + ".jpg";
            StorageReference imageRef = saveStorageReference.child(imageName);

            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                updateImageUrl(txtEmail.getText().toString().split("@")[0], imageName);

                Toast.makeText(RegisterActivity.this, "Profile picture uploaded successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(RegisterActivity.this, "Failed to upload profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(RegisterActivity.this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImageUrl(String username, String imageName) {
        databaseReference.child(username).child("imageUrl").setValue(imageName);
    }

}
