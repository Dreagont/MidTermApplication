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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    private ProfileFragment profileFragment = new ProfileFragment();
    private StudentManageFragment studentManageFragment = new StudentManageFragment();
    private UsersDetailFragment usersDetailFragment = new UsersDetailFragment();
    private Fragment activeFragment = profileFragment;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public String username ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        username = getIntent().getStringExtra("username").split("@")[0];

        Bundle bundle = new Bundle();
        bundle.putString("username", username);


        profileFragment.setArguments(bundle);
        studentManageFragment.setArguments(bundle);
        usersDetailFragment.setArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.mainFrame, profileFragment)
                .add(R.id.mainFrame, studentManageFragment).hide(studentManageFragment)
                .add(R.id.mainFrame, usersDetailFragment).hide(usersDetailFragment)
                .commit();
        
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        loadRole(username);



        mainBinding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profileTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                    activeFragment = profileFragment;
                    return true;
                }
                if (item.getItemId() == R.id.studentManageTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(studentManageFragment).commit();
                    activeFragment = studentManageFragment;
                    return true;
                }
                if (item.getItemId() == R.id.UsersTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(usersDetailFragment).commit();
                    activeFragment = usersDetailFragment;
                    return true;
                }
                return true;
            }
        });
    }

    public void setBottomNavigationViewVisibility(int visibility) {
        BottomNavigationView bottomNavigationView = mainBinding.bottomBar;
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(visibility);
        }
    }

    private void loadRole(String userName) {
        BottomNavigationView bottomNavigationView = mainBinding.bottomBar;

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.UsersTab); // Replace with the ID of your "Users" menu item
        menuItem.setVisible(false);
        databaseReference.child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = String.valueOf(snapshot.child("role").getValue());

                if (!role.equals("Employee")) {
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