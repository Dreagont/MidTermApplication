package com.example.midtermapplication;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private String username;
    private List<User> users;
    private UserAdapter userAdapter;
    ImageView btnAddUser,btnReload;
    Spinner sortByName,sortByRole;

    TextView btnSortByName, btnSortByRole;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public UsersDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersDetailFragment newInstance(String param1, String param2) {
        UsersDetailFragment fragment = new UsersDetailFragment();
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
            username = getArguments().getString("username").split("@")[0];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_detail, container, false);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String currentUser = username;

        btnAddUser = view.findViewById(R.id.btnAddUser);
        btnAddUser.setVisibility(View.GONE);

        btnSortByName = view.findViewById(R.id.btnSortByName);
        btnSortByRole = view.findViewById(R.id.btnSortByRole);

        btnSortByName.setBackgroundColor(Color.TRANSPARENT);
        btnSortByRole.setBackgroundColor(Color.TRANSPARENT);

        final boolean[] nameAsc = {true};

        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAsc[0] = !nameAsc[0];

                if (nameAsc[0]) {
                    btnSortByName.setText("Sort name: Asc");
                    UserSortUtil.sortUserByNameAsc(users);
                } else {
                    btnSortByName.setText("Sort name: Desc");
                    UserSortUtil.sortUserByNameDesc(users);
                }

                btnSortByName.setBackgroundResource(R.drawable.forward_box);
                btnSortByRole.setBackgroundColor(Color.TRANSPARENT);

                userAdapter.notifyDataSetChanged();
            }
        });

        final boolean[] roleAsc = {true};

        btnSortByRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roleAsc[0] = !roleAsc[0];

                if (roleAsc[0]) {
                    btnSortByRole.setText("Sort role: Asc");
                    UserSortUtil.sortByRoleAsc(users);
                } else {
                    btnSortByRole.setText("Sort role: Desc");
                    UserSortUtil.sortByRoleDesc(users);
                }

                // Change background color
                btnSortByRole.setBackgroundResource(R.drawable.forward_box);
                btnSortByName.setBackgroundColor(Color.TRANSPARENT);

                userAdapter.notifyDataSetChanged();
            }
        });

        SearchView searchBar = view.findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return true;
            }
        });

        if(searchBar != null) {
            searchBar.clearFocus();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");


        if (currentUser != null) {
            String userEmail = currentUser;
            verifyRole(currentUser);
            users = new ArrayList<>();

//            String uniqueKey =(userEmail.split("@"))[0];

            retrieveDataAndDisplay();


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);

            recyclerView = view.findViewById(R.id.recycleUser);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));

            userAdapter = new UserAdapter(requireContext(), users);
            recyclerView.setAdapter(userAdapter);


        } else {

        }

        return view;
    }

        private void verifyRole(String currentUser) {
        databaseReference.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (String.valueOf(snapshot.child("role").getValue()).equalsIgnoreCase("admin")) {
                        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(User user) {
                                Intent intent = new Intent(getContext(),RegisterActivity.class);
                                intent.putExtra("method","edit");
                                intent.putExtra("user",user);
                                startActivity(intent);
                            }
                        });
                        btnAddUser.setVisibility(View.VISIBLE);
                        btnAddUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), RegisterActivity.class);
                                intent.putExtra("method","create");
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveDataAndDisplay() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    users.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        User tmp = childSnapshot.getValue(User.class);
                        if (!tmp.getMail().split("@")[0].equals(username) && !tmp.getMail().split("@")[0].equals("admin")) {
                            users.add(tmp);
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Handle the result if needed
        }
    }
}