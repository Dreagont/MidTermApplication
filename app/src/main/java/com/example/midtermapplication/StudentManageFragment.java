package com.example.midtermapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    ImageView userIcon;
    TextView utUserName,utUserRole;
    LinearLayout studentList, add_student, AddStudentFromCsv, exportStudentToCSV;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String userRole = "";
    public StudentManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentManageFragment newInstance(String param1, String param2) {
        StudentManageFragment fragment = new StudentManageFragment();
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
        View view =  inflater.inflate(R.layout.fragment_studentmanage, container, false);
        add_student = view.findViewById(R.id.AddStudent);
        studentList = view.findViewById(R.id.studentList);
        exportStudentToCSV = view.findViewById(R.id.exportStudentToCSV);
        AddStudentFromCsv = view.findViewById(R.id.AddStudentFromCsv);
        userIcon = view.findViewById(R.id.userIcon);
        utUserName = view.findViewById(R.id.utUserName);
        utUserRole = view.findViewById(R.id.utUserRole);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Accounts");

        loadHeader(username, new UserRoleCallback() {
            @Override
            public void onUserRoleLoaded(String role) {
                if (role.equalsIgnoreCase("Employee")) {
                    AddStudentFromCsv.setVisibility(View.GONE);
                    exportStudentToCSV.setVisibility(View.GONE);
                    add_student.setVisibility(View.GONE);
                }
            }
        });
        AddStudentFromCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CsvReaderActivity.class);
                intent.putExtra("method","addStudent");
                startActivity(intent);
            }
        });

        exportStudentToCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Export Student Data");
                builder.setMessage("Do you want to export student data to CSV?");
                builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveStudent();
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


        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StudentListActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("userRole",userRole);
                startActivity(intent);
            }
        });
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddStudentActivity.class);
                intent.putExtra("method","create");
                intent.putExtra("username",username);
                intent.putExtra("userRole",userRole);
                startActivity(intent);
            }
        });
        return view;
    }

    private void saveStudent() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students");

        List<Student> studentList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        Student tmp = childSnapshot.getValue(Student.class);
                        studentList.add(tmp);
                    }

                    writeStudentDataToCsv(studentList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void writeStudentDataToCsv(List<Student> studentList) {
        String fileName = "stuOut.csv";

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + fileName;

        try {
            File file = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String dataToWrite = "";

            for (Student student : studentList) {
                dataToWrite = dataToWrite + student.getStudentId() + "," + student.getStudentName() + "," + student.getStudentEmail() + "\n";
            }

            writer.write(dataToWrite);
            writer.close();

            Toast.makeText(getContext(), "CSV file saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving CSV file", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadHeader(String username, UserRoleCallback callback) {
        databaseReference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String headName = String.valueOf(snapshot.child("name").getValue());
                String headRole = String.valueOf(snapshot.child("role").getValue());
                userRole = headRole;
                utUserName.setText(headName);
                utUserRole.setText(headRole);

                userIcon.setImageResource(headRole.equals("Manager")?
                        R.drawable.manager_icon : headRole.equals("Admin")?
                        R.drawable.admin_icon : R.drawable.employee_icon);

                callback.onUserRoleLoaded(userRole);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public interface UserRoleCallback {
        void onUserRoleLoaded(String userRole);
    }

}