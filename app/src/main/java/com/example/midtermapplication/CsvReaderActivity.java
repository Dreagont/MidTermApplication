package com.example.midtermapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReaderActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;

    private TextView resultTextView;
    private List<Certificates> certificateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_reader);

        resultTextView = findViewById(R.id.resultTextview);
        certificateList = new ArrayList<>();

        Button chooseFileButton = findViewById(R.id.chooseFileButton);

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // All file types
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected file
            final DocumentFile documentFile = DocumentFile.fromSingleUri(this, data.getData());
            if (documentFile != null) {
                String filePath = documentFile.getUri().getPath();
                Log.e("TAG","" + filePath);
                Toast.makeText(this, "" + filePath, Toast.LENGTH_SHORT).show();
                // Read the CSV file and populate the certificateList
                readCsvFile(filePath);
                // Display the certificates in the TextView
                displayCertificatess();
            }
        }
    }

    private void readCsvFile(String filePath) {
        try {
            // Use ContentResolver to open an InputStream
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            if (inputStream != null) {
                Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show();
                // Create a CSVReader object with the InputStream
                CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

                // Read all the records into a List<String[]>
                List<String[]> records = reader.readAll();

                // Populate the certificateList
                for (String[] record : records) {
                    Certificates certificate = new Certificates();
                    if (record.length >= 4) { // Ensure there are at least 4 fields in a record
                        certificate.setName(record[1]);
                        certificate.setBody(record[2]);
                        certificate.setStudentId(record[3]);
                        certificateList.add(certificate);
                    } else {
                        // Log a warning if a record doesn't have enough fields
                        Toast.makeText(this, "Skipping invalid record: " + Arrays.toString(record), Toast.LENGTH_SHORT).show();
                    }
                }

                // Close the reader
                reader.close();
            } else {
                Toast.makeText(this, "InputStream is null", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }


    private void displayCertificatess() {
        // Display the certificates in the TextView
        Toast.makeText(this, "" +certificateList.size(), Toast.LENGTH_SHORT).show();
        StringBuilder result = new StringBuilder();
        for (Certificates certificate : certificateList) {
            result.append("Name: ").append(certificate.getName()).append("\n");
            result.append("Body: ").append(certificate.getBody()).append("\n");
            result.append("Student ID: ").append(certificate.getStudentId()).append("\n\n");
        }
        resultTextView.setText(result.toString());
    }
}

