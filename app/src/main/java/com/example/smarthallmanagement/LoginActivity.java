package com.example.smarthallmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    // ==========================================
    // VIEWS
    // ==========================================

    private TextInputEditText etStudentId;
    private TextInputEditText etPassword;

    private TextInputLayout studentIdLayout;
    private TextInputLayout passwordLayout;

    private MaterialButton btnSignIn;

    private CheckBox checkRemember;

    private ProgressBar loading;

    private TextView tvForgotPassword;
    private TextView tvCreateAccount;


    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;


    // ==========================================
    // SHARED PREFERENCES
    // ==========================================

    private SharedPreferences preferences;

    private static final String PREF_NAME =
            "SmartHallPreferences";

    private static final String KEY_LOGGED_IN =
            "logged_in";

    private static final String KEY_STUDENT_ID =
            "student_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_login
        );


        // ======================================
        // INITIALIZE
        // ======================================

        databaseHelper =
                new DatabaseHelper(this);

        preferences =
                getSharedPreferences(
                        PREF_NAME,
                        MODE_PRIVATE
                );


        // ======================================
        // CHECK EXISTING LOGIN
        // ======================================

        if (preferences.getBoolean(
                KEY_LOGGED_IN,
                false)) {

            openMainActivity();

            return;
        }


        // ======================================
        // FIND VIEWS
        // ======================================

        studentIdLayout =
                findViewById(
                        R.id.studentIdLayout
                );

        passwordLayout =
                findViewById(
                        R.id.passwordLayout
                );

        etStudentId =
                findViewById(
                        R.id.etStudentId
                );

        etPassword =
                findViewById(
                        R.id.etPassword
                );

        btnSignIn =
                findViewById(
                        R.id.btnSignIn
                );

        checkRemember =
                findViewById(
                        R.id.checkRemember
                );

        loading =
                findViewById(
                        R.id.loading
                );

        tvForgotPassword =
                findViewById(
                        R.id.tvForgotPassword
                );

        tvCreateAccount =
                findViewById(
                        R.id.tvCreateAccount
                );


        // ======================================
        // SIGN IN
        // ======================================

        btnSignIn.setOnClickListener(
                v -> login()
        );


        // ======================================
        // FORGOT PASSWORD
        // ======================================

        tvForgotPassword.setOnClickListener(
                v -> showForgotPasswordMessage()
        );


        // ======================================
        // CREATE ACCOUNT
        // ======================================

        tvCreateAccount.setOnClickListener(
                v -> showRegistrationMessage()
        );
    }


    // ==========================================
    // LOGIN FUNCTION
    // ==========================================

    private void login() {

        String studentId =
                etStudentId
                        .getText()
                        .toString()
                        .trim();

        String password =
                etPassword
                        .getText()
                        .toString()
                        .trim();


        // ======================================
        // VALIDATION
        // ======================================

        studentIdLayout.setError(null);
        passwordLayout.setError(null);


        if (TextUtils.isEmpty(studentId)) {

            studentIdLayout.setError(
                    "Enter your Student ID"
            );

            etStudentId.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(password)) {

            passwordLayout.setError(
                    "Enter your password"
            );

            etPassword.requestFocus();

            return;
        }


        if (studentId.length() < 5) {

            studentIdLayout.setError(
                    "Enter a valid Student ID"
            );

            etStudentId.requestFocus();

            return;
        }


        // ======================================
        // SHOW LOADING
        // ======================================

        loading.setVisibility(
                ProgressBar.VISIBLE
        );

        btnSignIn.setEnabled(false);


        // ======================================
        // DATABASE LOGIN
        // ======================================

        new Handler().postDelayed(() -> {

            boolean loginSuccessful =
                    databaseHelper.checkLogin(
                            studentId,
                            password
                    );


            loading.setVisibility(
                    ProgressBar.GONE
            );

            btnSignIn.setEnabled(true);


            if (loginSuccessful) {
                // Save session
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_LOGGED_IN, true);
                editor.putString(KEY_STUDENT_ID, studentId);

                if (checkRemember.isChecked()) {
                    editor.putBoolean("remember_me", true);
                } else {
                    editor.putBoolean("remember_me", false);
                }
                editor.apply();

                Toast.makeText(
                        LoginActivity.this,
                        "Login successful",
                        Toast.LENGTH_SHORT
                ).show();

                openMainActivity();

            } else {
                passwordLayout.setError("Incorrect Student ID or password");
                Toast.makeText(
                        LoginActivity.this,
                        "Incorrect Student ID or password",
                        Toast.LENGTH_SHORT
                ).show();
            }

        }, 700);
    }


    // ==========================================
    // OPEN MAIN ACTIVITY
    // ==========================================

    private void openMainActivity() {

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }


    // ==========================================
    // FORGOT PASSWORD
    // ==========================================

    private void showForgotPasswordMessage() {

        Toast.makeText(
                this,
                "Password reset feature will be added here.",
                Toast.LENGTH_LONG
        ).show();
    }


    // ==========================================
    // REGISTRATION
    // ==========================================

    private void showRegistrationMessage() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}