package com.example.smarthallmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etStudentId, etPassword, etName, etDept, etYear, etSemester, etMobile, etEmail;
    private TextInputLayout idLayout, passLayout, nameLayout, deptLayout, yearLayout, semLayout, mobileLayout, emailLayout;
    private MaterialButton btnRegister;
    private TextView tvBackToLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        // Find Views
        etStudentId = findViewById(R.id.etRegStudentId);
        etPassword = findViewById(R.id.etRegPassword);
        etName = findViewById(R.id.etRegName);
        etDept = findViewById(R.id.etRegDept);
        etYear = findViewById(R.id.etRegYear);
        etSemester = findViewById(R.id.etRegSemester);
        etMobile = findViewById(R.id.etRegMobile);
        etEmail = findViewById(R.id.etRegEmail);

        idLayout = findViewById(R.id.regStudentIdLayout);
        passLayout = findViewById(R.id.regPasswordLayout);
        nameLayout = findViewById(R.id.regNameLayout);
        deptLayout = findViewById(R.id.regDeptLayout);
        yearLayout = findViewById(R.id.regYearLayout);
        semLayout = findViewById(R.id.regSemesterLayout);
        mobileLayout = findViewById(R.id.regMobileLayout);
        emailLayout = findViewById(R.id.regEmailLayout);

        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        try {
            String studentId = etStudentId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String dept = etDept.getText().toString().trim();
            String year = etYear.getText().toString().trim();
            String sem = etSemester.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            // Reset errors
            idLayout.setError(null);
            passLayout.setError(null);
            nameLayout.setError(null);

            if (TextUtils.isEmpty(studentId)) {
                idLayout.setError("Required");
                etStudentId.requestFocus();
                return;
            }

            if (studentId.length() < 5) {
                idLayout.setError("Student ID must be at least 5 characters");
                etStudentId.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passLayout.setError("Required");
                etPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passLayout.setError("Password must be at least 6 characters");
                etPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(name)) {
                nameLayout.setError("Required");
                etName.requestFocus();
                return;
            }

            if (databaseHelper.studentExists(studentId)) {
                idLayout.setError("Student ID already exists");
                etStudentId.requestFocus();
                Toast.makeText(this, "This Student ID is already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = databaseHelper.registerStudent(studentId, password, name, dept, year, sem, mobile, email);

            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("RegisterActivity", "Registration Error: " + e.getMessage(), e);
            Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
