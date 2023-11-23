package com.example.midtermapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SplashActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private StorageReference saveStorageReference, loadStorageReference;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);

    }

    private void nextActivity() {

        //if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
//        } else {
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.putExtra("loadedUser",loadData());
//            startActivity(intent);
//        }
    }

//    private User loadData() {
//        User user1 = new User();
//        databaseReference.child((user.getEmail().split("@"))[0]).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String imageUrl = String.valueOf(snapshot.child("imageUrl").getValue());
//                    String mail = String.valueOf(snapshot.child("mail").getValue());
//                    String phone = String.valueOf(snapshot.child("phone").getValue());
//                    String age = String.valueOf(snapshot.child("age").getValue());
//                    String name = String.valueOf(snapshot.child("name").getValue());
//                    String role = String.valueOf(snapshot.child("role").getValue());
//                    Boolean state = String.valueOf(snapshot.child("lock").getValue()).equals("false") ? false : true ;
//
//                    user1.setAge(Integer.parseInt(age));
//                    user1.setMail(mail);
//                    user1.setImageUrl(imageUrl);
//                    user1.setName(name);
//                    user1.setPhone(phone);
//                    user1.setLock(state);
//                    user1.setRole(role);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return user1;
//    }
}