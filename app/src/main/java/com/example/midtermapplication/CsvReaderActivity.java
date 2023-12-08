package com.example.midtermapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CsvReaderActivity extends AppCompatActivity {

    private TextView mTextViewCsvResult, fileName;
    private static final int READ_REQUEST_CODE = 123;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    List<Student> studentList = new ArrayList<>();
    List<Certificates> certificatesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_reader);
        mTextViewCsvResult = findViewById(R.id.readAction);
        fileName = findViewById(R.id.fileName);

        findViewById(R.id.button_loadCsv).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent, READ_REQUEST_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri selectedFileUri = resultData.getData();
                try {
                    String method = getIntent().getStringExtra("method");
                    if (method.equalsIgnoreCase("addStudent")) {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Students");
                        int testValue = readCSV(selectedFileUri).get(0).split(",").length;
                        Toast.makeText(this, "" + testValue, Toast.LENGTH_SHORT).show();
                        for (String student : readCSV(selectedFileUri)) {
                            String id = student.split(",")[0];
                            String name = student.split(",")[1];
                            String mail = student.split(",")[2];

                            studentList.add(new Student(id, name, mail));
                        }
                        findViewById(R.id.button_saveCsv).setOnClickListener(v -> {
                            for (Student student : studentList) {
                                addStudent(student);
                            }
                            Toast.makeText(this, "Add student successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        });

                    } else if (method.equalsIgnoreCase("addCertificate")) {
                        String studentId = getIntent().getStringExtra("studentId");
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("Students").child(studentId).child("Certificates");
                        int testValue = readCSV(selectedFileUri).get(0).split(",").length;
                        Toast.makeText(this, "" + testValue, Toast.LENGTH_SHORT).show();
                        for (String certificateData : readCSV(selectedFileUri)) {
                            String certificateName = certificateData.split(",")[0];
                            String certificateBody = certificateData.split(",")[1];
                            Certificates certificate = new Certificates(certificateName, certificateBody, studentId);

                            certificatesList.add(certificate);
                        }
                        findViewById(R.id.button_saveCsv).setOnClickListener(v -> {
                            for (Certificates certificates : certificatesList) {
                                addCertificate(certificates);
                            }
                            Toast.makeText(this, "Add certificates successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    String selectedFileName = getFileName(selectedFileUri);
                    fileName.setText(selectedFileName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void addCertificate(Certificates certificate) {
        DatabaseReference newCertificateRef = databaseReference.push();
        newCertificateRef.setValue(certificate);
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private List<String> readCSV(Uri uri) throws IOException {
        InputStream csvFile = getContentResolver().openInputStream(uri);
        InputStreamReader isr = new InputStreamReader(csvFile);
        return readLines(new BufferedReader(isr));
    }

    private List<String> readLines(BufferedReader reader) throws IOException {
        try {
            return IOUtils.readLines(reader);
        } finally {
            reader.close();
        }
    }

    private void addStudent(Student student) {
        databaseReference.child(student.getStudentId()).setValue(student);
    }
}
