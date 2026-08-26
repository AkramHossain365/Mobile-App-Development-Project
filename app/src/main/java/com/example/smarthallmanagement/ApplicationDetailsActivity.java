package com.example.smarthallmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ApplicationDetailsActivity
        extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextView tvId;
    private TextView tvType;
    private TextView tvSubject;
    private TextView tvReason;
    private TextView tvDetails;
    private TextView tvStatus;
    private TextView tvCreated;
    private TextView tvUpdated;

    private ApplicationDatabaseHelper database;

    private long applicationId = -1;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_application_details
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
                new ApplicationDatabaseHelper(
                        this
                );


        applicationId =
                getIntent().getLongExtra(
                        "application_id",
                        -1
                );


        initializeViews();


        toolbar.setNavigationOnClickListener(
                v -> finish()
        );


        loadApplication();
    }


    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarApplicationDetails
                );

        tvId =
                findViewById(
                        R.id.tvApplicationId
                );

        tvType =
                findViewById(
                        R.id.tvApplicationType
                );

        tvSubject =
                findViewById(
                        R.id.tvApplicationSubject
                );

        tvReason =
                findViewById(
                        R.id.tvApplicationReason
                );

        tvDetails =
                findViewById(
                        R.id.tvApplicationDetails
                );

        tvStatus =
                findViewById(
                        R.id.tvApplicationStatus
                );

        tvCreated =
                findViewById(
                        R.id.tvApplicationCreatedAt
                );

        tvUpdated =
                findViewById(
                        R.id.tvApplicationUpdatedAt
                );
    }


    // ==========================================
    // LOAD
    // ==========================================

    private void loadApplication() {

        if (applicationId == -1) {

            Toast.makeText(
                    this,
                    "Invalid application",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }


        Cursor cursor =
                database.getApplicationById(
                        applicationId
                );


        try {

            if (cursor.moveToFirst()) {

                String type =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_TYPE
                                )
                        );

                String subject =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_SUBJECT
                                )
                        );

                String reason =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_REASON
                                )
                        );

                String details =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_DETAILS
                                )
                        );

                String status =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_STATUS
                                )
                        );

                String created =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_CREATED_AT
                                )
                        );

                String updated =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        ApplicationDatabaseHelper.COL_UPDATED_AT
                                )
                        );


                tvId.setText(
                        "Application #" +
                                applicationId
                );

                tvType.setText(
                        type
                );

                tvSubject.setText(
                        subject
                );

                tvReason.setText(
                        reason
                );

                if (details == null ||
                        details.trim().isEmpty()) {

                    tvDetails.setText(
                            "No additional details."
                    );

                } else {

                    tvDetails.setText(
                            details
                    );
                }


                tvStatus.setText(
                        "Status: " +
                                status
                );


                tvCreated.setText(
                        "Submitted: " +
                                formatDate(created)
                );


                tvUpdated.setText(
                        "Last Updated: " +
                                formatDate(updated)
                );


                setStatusColor(
                        status
                );
            }

        } finally {

            cursor.close();
        }
    }


    // ==========================================
    // STATUS COLOR
    // ==========================================

    private void setStatusColor(
            String status
    ) {

        if (status.equalsIgnoreCase(
                "Pending"
        )) {

            tvStatus.setTextColor(
                    android.graphics.Color.rgb(
                            239,
                            108,
                            0
                    )
            );

        } else if (
                status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            tvStatus.setTextColor(
                    android.graphics.Color.rgb(
                            11,
                            107,
                            58
                    )
            );

        } else if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            tvStatus.setTextColor(
                    android.graphics.Color.rgb(
                            211,
                            47,
                            47
                    )
            );

        } else {

            tvStatus.setTextColor(
                    android.graphics.Color.rgb(
                            21,
                            101,
                            192
                    )
            );
        }
    }


    // ==========================================
    // DATE FORMAT
    // ==========================================

    private String formatDate(
            String dateTime
    ) {

        try {

            java.text.SimpleDateFormat input =
                    new java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            java.util.Locale.getDefault()
                    );

            java.text.SimpleDateFormat output =
                    new java.text.SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            java.util.Locale.getDefault()
                    );

            java.util.Date date =
                    input.parse(dateTime);

            if (date != null) {

                return output.format(date);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return dateTime;
    }


    @Override
    protected void onDestroy() {

        if (database != null) {

            database.close();
        }

        super.onDestroy();
    }
}