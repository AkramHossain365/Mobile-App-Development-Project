package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class ComplaintActivity extends AppCompatActivity {

    private MaterialToolbar toolbarComplaint;
    private BottomNavigationView bottomNavigation;

    private AutoCompleteTextView spinnerComplaintCategory;
    private AutoCompleteTextView spinnerComplaintPriority;

    private TextInputEditText etComplaintSubject;
    private TextInputEditText etComplaintDescription;

    private MaterialButton btnSubmitComplaint;

    private MaterialCardView cardComplaint1;
    private MaterialCardView cardComplaint2;
    private MaterialCardView cardComplaint3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen / Hide status bar
        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        setContentView(R.layout.activity_complaint);


        // --------------------------------
        // Initialize Views
        // --------------------------------

        toolbarComplaint = findViewById(R.id.toolbarComplaint);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        spinnerComplaintCategory =
                findViewById(R.id.spinnerComplaintCategory);

        spinnerComplaintPriority =
                findViewById(R.id.spinnerComplaintPriority);

        etComplaintSubject =
                findViewById(R.id.etComplaintSubject);

        etComplaintDescription =
                findViewById(R.id.etComplaintDescription);

        btnSubmitComplaint =
                findViewById(R.id.btnSubmitComplaint);

        cardComplaint1 =
                findViewById(R.id.cardComplaint1);

        cardComplaint2 =
                findViewById(R.id.cardComplaint2);

        cardComplaint3 =
                findViewById(R.id.cardComplaint3);


        // --------------------------------
        // Bottom Navigation
        // --------------------------------

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ComplaintActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_meal) {
                startActivity(new Intent(ComplaintActivity.this, MealActivity.class));
                return true;
            } else if (id == R.id.nav_notices) {
                startActivity(new Intent(ComplaintActivity.this, NoticesActivity.class));
                return true;
            }
            return false;
        });


        // --------------------------------
        // Back Button
        // --------------------------------

        toolbarComplaint.setNavigationOnClickListener(v -> {
            finish();
        });


        // --------------------------------
        // Complaint Categories
        // --------------------------------

        String[] categories = {
                "Maintenance",
                "Electricity",
                "Water Supply",
                "Cleaning",
                "Dining",
                "Internet",
                "Security",
                "Other"
        };

        ArrayAdapter<String> categoryAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        categories
                );

        spinnerComplaintCategory.setAdapter(categoryAdapter);


        // --------------------------------
        // Complaint Priority
        // --------------------------------

        String[] priorities = {
                "Low",
                "Medium",
                "High"
        };

        ArrayAdapter<String> priorityAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        priorities
                );

        spinnerComplaintPriority.setAdapter(priorityAdapter);


        // --------------------------------
        // Submit Complaint
        // --------------------------------

        btnSubmitComplaint.setOnClickListener(v -> {

            submitComplaint();

        });


        // --------------------------------
        // Previous Complaint Clicks
        // --------------------------------

        cardComplaint1.setOnClickListener(v -> {

            Toast.makeText(
                    ComplaintActivity.this,
                    "Opening complaint details...",
                    Toast.LENGTH_SHORT
            ).show();

        });


        cardComplaint2.setOnClickListener(v -> {

            Toast.makeText(
                    ComplaintActivity.this,
                    "Complaint is currently in progress",
                    Toast.LENGTH_SHORT
            ).show();

        });


        cardComplaint3.setOnClickListener(v -> {

            Toast.makeText(
                    ComplaintActivity.this,
                    "Complaint has been resolved",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    // ====================================
    // Submit Complaint Function
    // ====================================

    private void submitComplaint() {

        String category =
                spinnerComplaintCategory.getText().toString().trim();

        String subject =
                etComplaintSubject.getText().toString().trim();

        String description =
                etComplaintDescription.getText().toString().trim();

        String priority =
                spinnerComplaintPriority.getText().toString().trim();


        // --------------------------------
        // Validation
        // --------------------------------

        if (TextUtils.isEmpty(category)) {

            spinnerComplaintCategory.setError(
                    "Select a category"
            );

            spinnerComplaintCategory.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(subject)) {

            etComplaintSubject.setError(
                    "Enter complaint subject"
            );

            etComplaintSubject.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(description)) {

            etComplaintDescription.setError(
                    "Enter complaint description"
            );

            etComplaintDescription.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(priority)) {

            spinnerComplaintPriority.setError(
                    "Select priority"
            );

            spinnerComplaintPriority.requestFocus();

            return;
        }


        // --------------------------------
        // Successful Submission
        // --------------------------------

        Toast.makeText(
                ComplaintActivity.this,
                "Complaint submitted successfully",
                Toast.LENGTH_LONG
        ).show();


        // Clear form

        spinnerComplaintCategory.setText("");
        etComplaintSubject.setText("");
        etComplaintDescription.setText("");
        spinnerComplaintPriority.setText("");

    }

}