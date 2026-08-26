package com.example.smarthallmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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

    private TextView tvTotalComplaints;
    private TextView tvPendingComplaints;
    private TextView tvResolvedComplaints;

    private ComplaintDatabaseHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen / Hide status bar
        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        windowInsetsController.hide(
                WindowInsetsCompat.Type.statusBars()
        );

        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        setContentView(
                R.layout.activity_complaint
        );


        // --------------------------------
        // Database
        // --------------------------------

        database =
                new ComplaintDatabaseHelper(this);


        // --------------------------------
        // Initialize Views
        // --------------------------------

        toolbarComplaint =
                findViewById(
                        R.id.toolbarComplaint
                );

        bottomNavigation =
                findViewById(
                        R.id.bottomNavigation
                );

        spinnerComplaintCategory =
                findViewById(
                        R.id.spinnerComplaintCategory
                );

        spinnerComplaintPriority =
                findViewById(
                        R.id.spinnerComplaintPriority
                );

        etComplaintSubject =
                findViewById(
                        R.id.etComplaintSubject
                );

        etComplaintDescription =
                findViewById(
                        R.id.etComplaintDescription
                );

        btnSubmitComplaint =
                findViewById(
                        R.id.btnSubmitComplaint
                );

        cardComplaint1 =
                findViewById(
                        R.id.cardComplaint1
                );

        cardComplaint2 =
                findViewById(
                        R.id.cardComplaint2
                );

        cardComplaint3 =
                findViewById(
                        R.id.cardComplaint3
                );

        tvTotalComplaints =
                findViewById(
                        R.id.tvTotalComplaints
                );

        tvPendingComplaints =
                findViewById(
                        R.id.tvPendingComplaints
                );

        tvResolvedComplaints =
                findViewById(
                        R.id.tvResolvedComplaints
                );


        // --------------------------------
        // Bottom Navigation
        // --------------------------------

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_complaint);
            NavigationHelper.setupBottomNavigation(this, bottomNavigation);
        }


        // --------------------------------
        // Back Button
        // --------------------------------

        toolbarComplaint.setNavigationOnClickListener(
                v -> finish()
        );


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

        spinnerComplaintCategory.setAdapter(
                categoryAdapter
        );


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

        spinnerComplaintPriority.setAdapter(
                priorityAdapter
        );


        // --------------------------------
        // Submit Complaint
        // --------------------------------

        btnSubmitComplaint.setOnClickListener(
                v -> submitComplaint()
        );


        // --------------------------------
        // Load Previous Complaints
        // --------------------------------

        loadComplaints();
    }


    // ====================================
    // SUBMIT COMPLAINT
    // ====================================

    private void submitComplaint() {

        String category =
                spinnerComplaintCategory
                        .getText()
                        .toString()
                        .trim();

        String subject =
                etComplaintSubject
                        .getText()
                        .toString()
                        .trim();

        String description =
                etComplaintDescription
                        .getText()
                        .toString()
                        .trim();

        String priority =
                spinnerComplaintPriority
                        .getText()
                        .toString()
                        .trim();


        // --------------------------------
        // Validation
        // --------------------------------

        if (TextUtils.isEmpty(category)) {

            spinnerComplaintCategory.setError(
                    getString(R.string.error_select_category)
            );

            spinnerComplaintCategory.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(subject)) {

            etComplaintSubject.setError(
                    getString(R.string.error_enter_subject)
            );

            etComplaintSubject.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(description)) {

            etComplaintDescription.setError(
                    getString(R.string.error_enter_description)
            );

            etComplaintDescription.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(priority)) {

            spinnerComplaintPriority.setError(
                    getString(R.string.error_select_priority)
            );

            spinnerComplaintPriority.requestFocus();

            return;
        }


        // --------------------------------
        // Save to Database
        // --------------------------------

        long result =
                database.insertComplaint(
                        category,
                        subject,
                        description,
                        priority
                );


        if (result != -1) {

            Toast.makeText(
                    ComplaintActivity.this,
                    getString(R.string.complaint_submitted_success),
                    Toast.LENGTH_LONG
            ).show();


            // Clear form

            spinnerComplaintCategory.setText(
                    "", false
            );

            etComplaintSubject.setText(
                    ""
            );

            etComplaintDescription.setText(
                    ""
            );

            spinnerComplaintPriority.setText(
                    "", false
            );


            // Refresh complaints

            loadComplaints();

        } else {

            Toast.makeText(
                    ComplaintActivity.this,
                    getString(R.string.complaint_submitted_failed),
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    // ====================================
    // LOAD COMPLAINTS FROM DATABASE
    // ====================================

    private void loadComplaints() {

        updateSummary();

        try (Cursor cursor = database.getAllComplaints()) {

            hideAllComplaintCards();

            int position = 1;

            while (cursor.moveToNext() && position <= 3) {

                long id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                                ComplaintDatabaseHelper.COL_ID
                        )
                );

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ComplaintDatabaseHelper.COL_CATEGORY
                        )
                );

                String subject = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ComplaintDatabaseHelper.COL_SUBJECT
                        )
                );

                String status = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ComplaintDatabaseHelper.COL_STATUS
                        )
                );

                String createdAt = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                ComplaintDatabaseHelper.COL_CREATED_AT
                        )
                );

                if (position == 1) {

                    showComplaintCard(
                            cardComplaint1,
                            id,
                            category,
                            subject,
                            status,
                            createdAt
                    );

                } else if (position == 2) {

                    showComplaintCard(
                            cardComplaint2,
                            id,
                            category,
                            subject,
                            status,
                            createdAt
                    );

                } else {

                    showComplaintCard(
                            cardComplaint3,
                            id,
                            category,
                            subject,
                            status,
                            createdAt
                    );
                }

                position++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ====================================
    // UPDATE SUMMARY
    // ====================================

    private void updateSummary() {

        int total =
                database.getTotalComplaintsCount();

        int pending =
                database.getComplaintsCount("Pending");

        int resolved =
                database.getComplaintsCount("Resolved");

        tvTotalComplaints.setText(
                String.valueOf(total)
        );

        tvPendingComplaints.setText(
                String.valueOf(pending)
        );

        tvResolvedComplaints.setText(
                String.valueOf(resolved)
        );
    }


    // ====================================
    // HIDE ALL CARDS
    // ====================================

    private void hideAllComplaintCards() {

        cardComplaint1.setVisibility(
                View.GONE
        );

        cardComplaint2.setVisibility(
                View.GONE
        );

        cardComplaint3.setVisibility(
                View.GONE
        );
    }


    // ====================================
    // SHOW COMPLAINT CARD
    // ====================================

    private void showComplaintCard(
            MaterialCardView card,
            long id,
            String category,
            String subject,
            String status,
            String createdAt
    ) {

            card.setVisibility(View.VISIBLE);

            TextView tvCategory;
            TextView tvStatus;
            TextView tvTitle;
            TextView tvDate;

            if (card.getId() == R.id.cardComplaint1) {

                tvCategory =
                        card.findViewById(
                                R.id.tvComplaint1Category
                        );

                tvStatus =
                        card.findViewById(
                                R.id.tvComplaint1Status
                        );

                tvTitle =
                        card.findViewById(
                                R.id.tvComplaint1Title
                        );

                tvDate =
                        card.findViewById(
                                R.id.tvComplaint1Date
                        );

            } else if (card.getId() == R.id.cardComplaint2) {

                tvCategory =
                        card.findViewById(
                                R.id.tvComplaint2Category
                        );

                tvStatus =
                        card.findViewById(
                                R.id.tvComplaint2Status
                        );

                tvTitle =
                        card.findViewById(
                                R.id.tvComplaint2Title
                        );

                tvDate =
                        card.findViewById(
                                R.id.tvComplaint2Date
                        );

            } else {

                tvCategory =
                        card.findViewById(
                                R.id.tvComplaint3Category
                        );

                tvStatus =
                        card.findViewById(
                                R.id.tvComplaint3Status
                        );

                tvTitle =
                        card.findViewById(
                                R.id.tvComplaint3Title
                        );

                tvDate =
                        card.findViewById(
                                R.id.tvComplaint3Date
                        );
            }


            // Category
            tvCategory.setText(
                    category.toUpperCase()
            );


            // Status
            tvStatus.setText(
                    status
            );


            // Subject
            tvTitle.setText(
                    subject
            );


            // Date
            tvDate.setText(
                    getString(
                            R.string.label_submitted_date,
                            ComplaintDatabaseHelper.formatComplaintDate(createdAt)
                    )
            );


            // --------------------------------
            // Status color
            // --------------------------------

            if (status.equalsIgnoreCase("Pending")) {

                tvStatus.setTextColor(
                        android.graphics.Color.rgb(
                                239,
                                108,
                                0
                        )
                );

            } else if (
                    status.equalsIgnoreCase("In Progress")
            ) {

                tvStatus.setTextColor(
                        android.graphics.Color.rgb(
                                21,
                                101,
                                192
                        )
                );

            } else if (
                    status.equalsIgnoreCase("Resolved")
            ) {

                tvStatus.setTextColor(
                        android.graphics.Color.rgb(
                                11,
                                107,
                                58
                        )
                );
            }


            // --------------------------------
            // Click complaint
            // --------------------------------

            card.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                ComplaintActivity.this,
                                ComplaintDetailsActivity.class
                        );

                intent.putExtra(
                        "complaint_id",
                        id
                );

                startActivity(intent);
            });
        }


    // ====================================
    // REFRESH WHEN RETURNING
    // ====================================

    @Override
    protected void onResume() {

        super.onResume();

        if (database != null) {
            loadComplaints();
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

