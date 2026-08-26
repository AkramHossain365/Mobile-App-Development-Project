package com.example.smarthallmanagement;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentDetailsActivity
        extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private TextView tvId;
    private TextView tvType;
    private TextView tvDescription;
    private TextView tvAmount;
    private TextView tvMethod;
    private TextView tvReference;
    private TextView tvStatus;
    private TextView tvDate;
    private TextView tvUpdated;

    private PaymentDatabaseHelper database;

    private long paymentId = -1;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

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
                new PaymentDatabaseHelper(
                        this
                );


        paymentId =
                getIntent().getLongExtra(
                        "payment_id",
                        -1
                );


        initializeViews();

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    v -> finish()
            );
        }


        loadPayment();
    }


    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarPaymentDetails
                );


        tvId =
                findViewById(
                        R.id.tvPaymentId
                );


        tvType =
                findViewById(
                        R.id.tvPaymentType
                );


        tvDescription =
                findViewById(
                        R.id.tvPaymentDescription
                );


        tvAmount =
                findViewById(
                        R.id.tvPaymentAmount
                );


        tvMethod =
                findViewById(
                        R.id.tvPaymentMethod
                );


        tvReference =
                findViewById(
                        R.id.tvPaymentReference
                );


        tvStatus =
                findViewById(
                        R.id.tvPaymentStatus
                );


        tvDate =
                findViewById(
                        R.id.tvPaymentDate
                );


        tvUpdated =
                findViewById(
                        R.id.tvPaymentUpdated
                );
    }


    // ==========================================
    // LOAD PAYMENT
    // ==========================================

    private void loadPayment() {

        if (paymentId == -1) {
            Toast.makeText(
                    this,
                    "Invalid payment",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        if (database == null) return;


        Cursor cursor =
                database.getPaymentById(
                        paymentId
                );


        if (cursor == null) {
            Toast.makeText(this, "Could not load payment", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {

            if (cursor.moveToFirst()) {
                int typeIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_TYPE);
                int descIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_AMOUNT);
                int methodIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_METHOD);
                int refIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_REFERENCE);
                int statusIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_STATUS);
                int dateIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_PAYMENT_DATE);
                int updatedIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_UPDATED_AT);

                String type = (typeIndex != -1) ? cursor.getString(typeIndex) : "Unknown";
                String description = (descIndex != -1) ? cursor.getString(descIndex) : "";
                double amount = (amountIndex != -1) ? cursor.getDouble(amountIndex) : 0.0;
                String method = (methodIndex != -1) ? cursor.getString(methodIndex) : "";
                String reference = (refIndex != -1) ? cursor.getString(refIndex) : "";
                String status = (statusIndex != -1) ? cursor.getString(statusIndex) : "Pending";
                String date = (dateIndex != -1) ? cursor.getString(dateIndex) : "";
                String updated = (updatedIndex != -1) ? cursor.getString(updatedIndex) : "";

                if (tvId != null) tvId.setText("Payment #" + paymentId);
                if (tvType != null) tvType.setText(type);

                if (tvDescription != null) {
                    if (description == null || description.trim().isEmpty()) {
                        tvDescription.setText("No description");
                    } else {
                        tvDescription.setText(description);
                    }
                }

                if (tvAmount != null) {
                    tvAmount.setText(
                            "৳ " +
                                    String.format(
                                            Locale.getDefault(),
                                            "%.2f",
                                            amount
                                    )
                    );
                }

                if (tvMethod != null) {
                    tvMethod.setText(
                            method == null ?
                                    "Not available" :
                                    method
                    );
                }

                if (tvReference != null) {
                    tvReference.setText(
                            reference == null ||
                                    reference.trim().isEmpty()
                                    ?
                                    "Not provided"
                                    :
                                    reference
                    );
                }

                if (tvStatus != null) {
                    tvStatus.setText("Status: " + status);
                    setStatusColor(status);
                }

                if (tvDate != null) {
                    tvDate.setText(
                            "Payment Date: " +
                                    formatDate(date)
                    );
                }

                if (tvUpdated != null) {
                    tvUpdated.setText(
                            "Last Updated: " +
                                    formatDateTime(updated)
                    );
                }
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
        if (tvStatus == null || status == null) return;

        if (status.equalsIgnoreCase(
                "Paid"
        )) {

            tvStatus.setTextColor(
                    Color.rgb(
                            11,
                            107,
                            58
                    )
            );

        } else if (
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            tvStatus.setTextColor(
                    Color.rgb(
                            239,
                            108,
                            0
                    )
            );

        } else if (
                status.equalsIgnoreCase(
                        "Overdue"
                )
        ) {

            tvStatus.setTextColor(
                    Color.rgb(
                            211,
                            47,
                            47
                    )
            );
        }
    }


    // ==========================================
    // DATE
    // ==========================================

    private String formatDate(
            String date
    ) {

        if (date == null ||
                date.trim().isEmpty()) {

            return "Not available";
        }


        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );


            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                    );


            Date parsed =
                    input.parse(date);


            if (parsed != null) {

                return output.format(
                        parsed
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return date;
    }


    // ==========================================
    // DATE + TIME
    // ==========================================

    private String formatDateTime(
            String dateTime
    ) {

        if (dateTime == null ||
                dateTime.trim().isEmpty()) {

            return "Not available";
        }


        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                    );


            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a",
                            Locale.getDefault()
                    );


            Date parsed =
                    input.parse(dateTime);


            if (parsed != null) {

                return output.format(
                        parsed
                );
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