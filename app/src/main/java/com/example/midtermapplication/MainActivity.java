package com.example.midtermapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.midtermapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    FirebaseAuth mAuth;
    private ProfileFragment profileFragment = new ProfileFragment();
    private CertificatesFragment certificatesFragment = new CertificatesFragment();

    private UsersFragment usersFragment = new UsersFragment();
    private Fragment activeFragment = profileFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        fragmentManager.beginTransaction().add(R.id.mainFrame, profileFragment).add(R.id.mainFrame, certificatesFragment).hide(certificatesFragment).add(R.id.mainFrame, usersFragment).hide(usersFragment).commit();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");
        
        User receivedUser = (User) getIntent().getSerializableExtra("user");


        loadRole();



        mainBinding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profileTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                    activeFragment = profileFragment;
                    return true;
                }
                if (item.getItemId() == R.id.certificatesTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(certificatesFragment).commit();
                    activeFragment = certificatesFragment;
                    return true;
                }
                if (item.getItemId() == R.id.UsersTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(usersFragment).commit();
                    activeFragment = usersFragment;
                    return true;
                }
                return true;
            }
        });
    }

    private void loadRole() {
        BottomNavigationView bottomNavigationView = mainBinding.bottomBar;

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.UsersTab); // Replace with the ID of your "Users" menu item
        menuItem.setVisible(false);
        databaseReference.child((mAuth.getCurrentUser().getEmail().split("@"))[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = String.valueOf(snapshot.child("role").getValue());

                if (!role.equals("Student")) {
                    menuItem.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi từ cơ sở dữ liệu
            }
        });
    }

}