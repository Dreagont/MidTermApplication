package com.example.midtermapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Student> students;
    private StudentAdapter studentAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView btnSortByName,btnSortByEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        SearchView searchBar = findViewById(R.id.student_search_bar);
        ImageView btnAddStudent = findViewById(R.id.btnAddStudent);
        LinearLayout btnBack = findViewById(R.id.btnBackStudentList);
        btnSortByName = findViewById(R.id.btnSortByName);
        btnSortByEmail = findViewById(R.id.btnSortByEmail);

        btnSortByEmail.setBackgroundColor(Color.TRANSPARENT);
        btnSortByName.setBackgroundColor(Color.TRANSPARENT);

        final boolean[] nameAsc = {true};

        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameAsc[0] = !nameAsc[0];

                if (nameAsc[0]) {
                    btnSortByName.setText("Sort name: Asc");
                    UserSortUtil.sortStudentByNameAsc(students);
                } else {
                    btnSortByName.setText("Sort name: Desc");
                    UserSortUtil.sortStudentByNameDesc(students);
                }
                btnSortByName.setBackgroundResource(R.drawable.forward_box);
                btnSortByEmail.setBackgroundColor(Color.TRANSPARENT);

                studentAdapter.notifyDataSetChanged();
            }
        });

        final boolean[] emailAsc = {true};

        btnSortByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAsc[0] = !emailAsc[0];

                if (emailAsc[0]) {
                    btnSortByEmail.setText("Sort email: Asc");
                    UserSortUtil.sortStudentByEmailAsc(students);
                } else {
                    btnSortByEmail.setText("Sort email: Desc");
                    UserSortUtil.sortStudentByEmailDesc(students);
                }

                btnSortByEmail.setBackgroundResource(R.drawable.forward_box);
                btnSortByName.setBackgroundColor(Color.TRANSPARENT);

                studentAdapter.notifyDataSetChanged();
            }
        });

        String currentUser = getIntent().getStringExtra("username");


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentAdapter.getFilter().filter(newText);
                return true;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBar.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students");

        if (currentUser != null) {
            students = new ArrayList<>();
            retrieveDataAndDisplay();

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.recycleUser);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

            studentAdapter = new StudentAdapter(this, students);
            recyclerView.setAdapter(studentAdapter);
        }

        studentAdapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                intent.putExtra("method", "edit");
                intent.putExtra("student", student);
                intent.putExtra("username",currentUser);
                startActivity(intent);
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                intent.putExtra("method", "create");
                startActivity(intent);
            }
        });
    }

    private void retrieveDataAndDisplay() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    students.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Student tmp = childSnapshot.getValue(Student.class);
                        students.add(tmp);
                    }
                    studentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
