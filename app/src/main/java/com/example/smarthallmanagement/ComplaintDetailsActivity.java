package com.example.smarthallmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ComplaintDetailsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextView tvComplaintId;
    private TextView tvCategory;
    private TextView tvSubject;
    private TextView tvDescription;
    private TextView tvPriority;
    private TextView tvCreatedAt;
    private TextView tvCurrentStatus;

    private AutoCompleteTextView spinnerStatus;

    private MaterialButton btnUpdateStatus;

    private ComplaintDatabaseHelper database;

    private long complaintId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_complaint_details
        );

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
                new ComplaintDatabaseHelper(this);

        complaintId =
                getIntent().getLongExtra(
                        "complaint_id",
                        -1
                );

        initializeViews();

        setupStatusSpinner();

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        loadComplaint();

        btnUpdateStatus.setOnClickListener(
                v -> updateStatus()
        );
    }

    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarComplaintDetails
                );

        tvComplaintId =
                findViewById(
                        R.id.tvComplaintId
                );

        tvCategory =
                findViewById(
                        R.id.tvComplaintCategory
                );

        tvSubject =
                findViewById(
                        R.id.tvComplaintSubject
                );

        tvDescription =
                findViewById(
                        R.id.tvComplaintDescription
                );

        tvPriority =
                findViewById(
                        R.id.tvComplaintPriority
                );

        tvCreatedAt =
                findViewById(
                        R.id.tvComplaintCreatedAt
                );

        tvCurrentStatus =
                findViewById(
                        R.id.tvCurrentStatus
                );

        spinnerStatus =
                findViewById(
                        R.id.spinnerComplaintStatus
                );

        btnUpdateStatus =
                findViewById(
                        R.id.btnUpdateComplaintStatus
                );
    }

    // ==========================================
    // STATUS SPINNER
    // ==========================================

    private void setupStatusSpinner() {

        String[] statuses = {
                "Pending",
                "In Progress",
                "Resolved"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        statuses
                );

        spinnerStatus.setAdapter(adapter);
    }

    // ==========================================
    // LOAD COMPLAINT
    // ==========================================

    private void loadComplaint() {

        if (complaintId == -1) {

            Toast.makeText(
                    this,
                    "Invalid complaint",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        try (Cursor cursor = database.getComplaintById(complaintId)) {

            if (cursor.moveToFirst()) {

                String category =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_CATEGORY
                                )
                        );

                String subject =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_SUBJECT
                                )
                        );

                String description =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_DESCRIPTION
                                )
                        );

                String priority =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_PRIORITY
                                )
                        );

                String status =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_STATUS
                                )
                        );

                String createdAt =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ComplaintDatabaseHelper.COL_CREATED_AT
                                )
                        );

                tvComplaintId.setText(
                        getString(R.string.complaint_id_format, complaintId)
                );

                tvCategory.setText(
                        category
                );

                tvSubject.setText(
                        subject
                );

                tvDescription.setText(
                        description
                );

                tvPriority.setText(
                        priority
                );

                tvCreatedAt.setText(
                        ComplaintDatabaseHelper.formatComplaintDate(createdAt)
                );

                tvCurrentStatus.setText(
                        getString(R.string.current_status_format, status)
                );

                spinnerStatus.setText(
                        status,
                        false
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // UPDATE STATUS
    // ==========================================

    private void updateStatus() {

        String status =
                spinnerStatus
                        .getText()
                        .toString()
                        .trim();

        if (status.isEmpty()) {

            Toast.makeText(
                    this,
                    getString(R.string.msg_select_status),
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean updated =
                database.updateComplaintStatus(
                        complaintId,
                        status
                );

        if (updated) {

            tvCurrentStatus.setText(
                    getString(R.string.current_status_format, status)
            );

            Toast.makeText(
                    this,
                    getString(R.string.msg_status_updated),
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    getString(R.string.msg_update_failed),
                    Toast.LENGTH_SHORT
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