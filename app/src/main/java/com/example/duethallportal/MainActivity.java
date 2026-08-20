package com.example.duethallportal;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etStudentId;
    private EditText etPassword;
    private Button btnSignIn;

    // Student information
    private final String STUDENT_ID = "2204010";
    private final String PASSWORD = "duet1234";
    private final String STUDENT_NAME = "Din Mohammad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize views
        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Sign in button
        btnSignIn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String studentId = etStudentId.getText()
                .toString()
                .trim();

        String password = etPassword.getText()
                .toString()
                .trim();

        // Empty validation
        if (TextUtils.isEmpty(studentId)) {
            etStudentId.setError("Enter your Student ID");
            etStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        // Demo login validation
        if (studentId.equals(STUDENT_ID) &&
                password.equals(PASSWORD)) {

            Toast.makeText(
                    MainActivity.this,
                    "Welcome, " + STUDENT_NAME,
                    Toast.LENGTH_LONG
            ).show();

        } else {

            Toast.makeText(
                    MainActivity.this,
                    "Invalid Student ID or Password",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}