package com.example.midtermapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView btnSetting, proPic;
    private FirebaseAuth mAuth;
    private Button btnLogout;
    private TextView txtUser;
    private FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private StorageReference saveStorageReference, loadStorageReference;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        txtUser =view.findViewById(R.id.txtProUserAva);
        proPic = view.findViewById(R.id.proPic);
        btnSetting = view.findViewById(R.id.proSetting);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        txtUser.setText(mAuth.getCurrentUser().getEmail());
        loadPicture();


        firebaseStorage = FirebaseStorage.getInstance();
        saveStorageReference = firebaseStorage.getReference("profile_pictures");


        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        return view;
    }

    private void loadPicture() {
        databaseReference.child((mAuth.getCurrentUser().getEmail().split("@"))[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = String.valueOf(snapshot.child("imageUrl").getValue());

                    Bitmap cachedImage = loadProfileImageFromCache(imageUrl);

                    if (cachedImage != null) {
                        proPic.setImageBitmap(cachedImage);
                    } else {
                        firebaseStorage = FirebaseStorage.getInstance();

                        loadStorageReference = firebaseStorage.getReference("profile_pictures/" + imageUrl);
                        loadStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imagePath = uri.toString();

                                Glide.with(getContext()).load(imagePath).into(proPic);


                                saveProfileImageToCache(imagePath);
                            }
                        });
                    }
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveProfileImageToCache(String imagePath) {

        Glide.with(getContext())
                .asBitmap()
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Lưu vào cache
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                    }
                });
    }

    private Bitmap loadProfileImageFromCache(String imageUrl) {
        try {
            return Glide.with(getContext())
                    .asBitmap()
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_setting_popup, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.changeProPic) {
                    ImagePicker.with(ProfileFragment.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                    return true;
                } else if (itemId == R.id.logout) {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            Uri uri = data.getData();
            proPic.setImageURI(uri);
            String imageName = (mAuth.getCurrentUser().getEmail().split("@"))[0] + ".jpg";

            StorageReference imageRef = saveStorageReference.child(imageName);

            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getActivity(), "Profile picture uploaded successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Failed to upload profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}