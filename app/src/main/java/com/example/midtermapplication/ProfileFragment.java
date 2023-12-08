package com.example.midtermapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    private String username;
    private ImageView btnSetting, proPic;
    private FirebaseAuth mAuth;
    private Button btnLogout;
    private TextView txtUser, txtProfileUsername, txtProfileAge, txtProfilePhone, txtProfileRole;
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
    public static ProfileFragment newInstance(String param1, String param2, String name) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("username", name);
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
            username = getArguments().getString("username").split("@")[0];
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
        txtProfileUsername = view.findViewById(R.id.txtProfileUsername);
        txtProfileAge = view.findViewById(R.id.txtProfileAge);
        txtProfilePhone = view.findViewById(R.id.txtProfilePhone);
        txtProfileRole = view.findViewById(R.id.txtProfileRole);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("username");
        }

        loadData();


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

    private void loadData() {
        databaseReference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = String.valueOf(snapshot.child("imageUrl").getValue());
                    String mail = String.valueOf(snapshot.child("mail").getValue());
                    String phone = String.valueOf(snapshot.child("phone").getValue());
                    String age = String.valueOf(snapshot.child("age").getValue());
                    String name = String.valueOf(snapshot.child("name").getValue());
                    String role = String.valueOf(snapshot.child("role").getValue());


                    txtProfileUsername.setText(mail);
                    txtProfileAge.setText(age);
                    txtProfilePhone.setText(phone);
                    txtUser.setText(name);
                    txtProfileRole.setText(role);


                    firebaseStorage = FirebaseStorage.getInstance();

                    loadStorageReference = firebaseStorage.getReference("profile_pictures/" + imageUrl);
                    loadStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imagePath = uri.toString();

                            Picasso.get().load(imagePath).placeholder(R.drawable.baseline_downloading_24).into(proPic);

                        }
                    });

                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showPopupMenu(View view) {

        PopupMenu popup = new PopupMenu(getActivity(), view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon",boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.profile_setting_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

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
                    clearUserSession();

                    // Navigate back to the login screen
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void clearUserSession() {
        SharedPreferences preferences = getContext().getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
//updateImageUrl(username);
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            Uri uri = data.getData();
            proPic.setImageURI(uri);
            String imageName = username + ".jpg";

            StorageReference imageRef = saveStorageReference.child(imageName);

            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                updateImageUrl(username);
                Toast.makeText(getActivity(), "Profile picture uploaded successfully!", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Failed to upload profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImageUrl(String username) {
        databaseReference.child(username).child("imageUrl").setValue(username + ".jpg");
    }

}