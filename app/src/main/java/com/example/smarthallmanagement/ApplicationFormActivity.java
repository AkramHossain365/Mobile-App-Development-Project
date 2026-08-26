package com.example.smarthallmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ApplicationFormActivity
        extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private AutoCompleteTextView spinnerType;

    private TextInputEditText etSubject;
    private TextInputEditText etReason;
    private TextInputEditText etDetails;

    private MaterialButton btnSubmit;

    private ApplicationDatabaseHelper database;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_form);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.hide(
                WindowInsetsCompat.Type.statusBars()
        );

        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );


        database =
                new ApplicationDatabaseHelper(
                        this
                );


        initializeViews();


        setupApplicationTypes();


        toolbar.setNavigationOnClickListener(
                v -> finish()
        );


        btnSubmit.setOnClickListener(
                v -> submitApplication()
        );
    }


    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarApplicationForm
                );

        spinnerType =
                findViewById(
                        R.id.spinnerApplicationType
                );

        etSubject =
                findViewById(
                        R.id.etApplicationSubject
                );

        etReason =
                findViewById(
                        R.id.etApplicationReason
                );

        etDetails =
                findViewById(
                        R.id.etApplicationDetails
                );

        btnSubmit =
                findViewById(
                        R.id.btnSubmitApplication
                );
    }


    // ==========================================
    // APPLICATION TYPES
    // ==========================================

    private void setupApplicationTypes() {

        String[] types = {

                "Seat / Hall Application",

                "Hall / Room Change",

                "Room Change",

                "Hall Clearance",

                "Meal Related",

                "Other"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        types
                );


        spinnerType.setAdapter(
                adapter
        );
    }


    // ==========================================
    // SUBMIT
    // ==========================================

    private void submitApplication() {

        String type =
                spinnerType
                        .getText()
                        .toString()
                        .trim();


        String subject =
                etSubject
                        .getText()
                        .toString()
                        .trim();


        String reason =
                etReason
                        .getText()
                        .toString()
                        .trim();


        String details =
                etDetails
                        .getText()
                        .toString()
                        .trim();


        // --------------------------------------
        // Validation
        // --------------------------------------

        if (TextUtils.isEmpty(type)) {

            spinnerType.setError(
                    "Select application type"
            );

            spinnerType.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(subject)) {

            etSubject.setError(
                    "Enter application subject"
            );

            etSubject.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(reason)) {

            etReason.setError(
                    "Enter reason"
            );

            etReason.requestFocus();

            return;
        }


        // --------------------------------------
        // Save
        // --------------------------------------

        try {
            long result =
                    database.insertApplication(
                            type,
                            subject,
                            reason,
                            details
                    );


            if (result != -1) {

                Toast.makeText(
                        this,
                        "Application submitted successfully",
                        Toast.LENGTH_LONG
                ).show();


                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to submit application",
                        Toast.LENGTH_LONG
                ).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(
                    this,
                    "Submission Error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    @Override
    protected void onDestroy() {

        if (database != null) {

            database.close();
        }

        super.onDestroy();
    }
}