package com.example.midtermapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midtermapplication.databinding.ActivityMainBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();


        changeFragment(new ProfileFragment());


        mainBinding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profileTab) {
                    changeFragment(new ProfileFragment());
                }
                if (item.getItemId() == R.id.certificatesTab) {
                    changeFragment(new CertificatesFragment());
                }
                return true;
            }
        });
    }

    public void changeFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }

}