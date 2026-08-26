package com.example.smarthallmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity
        extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private MaterialButton btnNewPayment;

    private TextView tvTotalPaid;
    private TextView tvTotalDue;
    private TextView tvTotalPayments;
    private TextView tvPendingPayments;

    private LinearLayout paymentListContainer;
    private TextView tvNoPayments;

    private PaymentDatabaseHelper database;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.hide(WindowInsetsCompat.Type.statusBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );


        database =
                new PaymentDatabaseHelper(
                        this
                );


        initializeViews();

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    v -> finish()
            );
        }

        if (btnNewPayment != null) {
            btnNewPayment.setOnClickListener(
                    v -> {
                        startActivity(
                                new Intent(
                                        PaymentActivity.this,
                                        PaymentFormActivity.class
                                )
                        );
                    }
            );
        }
    }


    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarPayment
                );


        btnNewPayment =
                findViewById(
                        R.id.btnNewPayment
                );


        tvTotalPaid =
                findViewById(
                        R.id.tvTotalPaid
                );


        tvTotalDue =
                findViewById(
                        R.id.tvTotalDue
                );


        tvTotalPayments =
                findViewById(
                        R.id.tvTotalPayments
                );


        tvPendingPayments =
                findViewById(
                        R.id.tvPendingPayments
                );


        paymentListContainer =
                findViewById(
                        R.id.paymentListContainer
                );


        tvNoPayments =
                findViewById(
                        R.id.tvNoPayments
                );
    }


    // ==========================================
    // SUMMARY
    // ==========================================

    private void loadSummary() {
        if (tvTotalPaid == null || tvTotalDue == null || tvTotalPayments == null || tvPendingPayments == null || database == null) {
            return;
        }

        try {
            double totalPaid =
                    database.getTotalPaidAmount();


            double totalDue =
                    database.getTotalDueAmount();


            int total =
                    database.getTotalPayments();


            int pending =
                    database.getCountByStatus(
                            "Pending"
                    );


            tvTotalPaid.setText(
                    "৳ " +
                            String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    totalPaid
                            )
            );


            tvTotalDue.setText(
                    "৳ " +
                            String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    totalDue
                            )
            );


            tvTotalPayments.setText(
                    String.valueOf(total)
            );


            tvPendingPayments.setText(
                    String.valueOf(pending)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==========================================
    // LOAD PAYMENT HISTORY
    // ==========================================

    private void loadPayments() {
        if (paymentListContainer == null || database == null) {
            return;
        }

        paymentListContainer.removeAllViews();

        Cursor cursor = null;
        try {
            cursor = database.getAllPayments();

            if (cursor == null) {
                if (tvNoPayments != null) {
                    tvNoPayments.setVisibility(View.VISIBLE);
                }
                return;
            }

            if (cursor.getCount() == 0) {
                if (tvNoPayments != null) {
                    tvNoPayments.setVisibility(View.VISIBLE);
                }
                return;
            }

            if (tvNoPayments != null) {
                tvNoPayments.setVisibility(View.GONE);
            }

            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_ID);
                int typeIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_TYPE);
                int descriptionIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_AMOUNT);
                int methodIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_METHOD);
                int referenceIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_REFERENCE);
                int statusIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_STATUS);
                int dateIndex = cursor.getColumnIndex(PaymentDatabaseHelper.COL_PAYMENT_DATE);

                long id = (idIndex != -1) ? cursor.getLong(idIndex) : -1;
                String type = (typeIndex != -1) ? cursor.getString(typeIndex) : "Unknown";
                String description = (descriptionIndex != -1) ? cursor.getString(descriptionIndex) : "";
                double amount = (amountIndex != -1) ? cursor.getDouble(amountIndex) : 0.0;
                String method = (methodIndex != -1) ? cursor.getString(methodIndex) : "";
                String reference = (referenceIndex != -1) ? cursor.getString(referenceIndex) : "";
                String status = (statusIndex != -1) ? cursor.getString(statusIndex) : "Pending";
                String paymentDate = (dateIndex != -1) ? cursor.getString(dateIndex) : "";

                addPaymentCard(
                        id,
                        type,
                        description,
                        amount,
                        method,
                        reference,
                        status,
                        paymentDate
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    // ==========================================
    // PAYMENT CARD
    // ==========================================

    private void addPaymentCard(
            long id,
            String type,
            String description,
            double amount,
            String method,
            String reference,
            String status,
            String paymentDate
    ) {

        MaterialCardView card =
                new MaterialCardView(this);


        card.setRadius(
                dpToPx(16)
        );


        card.setCardElevation(
                dpToPx(2)
        );


        card.setClickable(true);


        LinearLayout content =
                new LinearLayout(this);


        content.setOrientation(
                LinearLayout.VERTICAL
        );


        int padding =
                dpToPx(16);


        content.setPadding(
                padding,
                padding,
                padding,
                padding
        );


        // --------------------------------------
        // Top row
        // --------------------------------------

        LinearLayout topRow =
                new LinearLayout(this);


        topRow.setOrientation(
                LinearLayout.HORIZONTAL
        );


        topRow.setGravity(
                Gravity.CENTER_VERTICAL
        );


        TextView tvType =
                new TextView(this);


        tvType.setText(
                type != null ? type.toUpperCase() : "UNKNOWN"
        );


        tvType.setTextSize(12);


        tvType.setTypeface(
                null,
                Typeface.BOLD
        );


        tvType.setTextColor(
                Color.rgb(
                        11,
                        107,
                        58
                )
        );


        LinearLayout.LayoutParams typeParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );


        topRow.addView(
                tvType,
                typeParams
        );


        TextView tvStatus =
                new TextView(this);


        tvStatus.setText(
                status
        );


        tvStatus.setTextSize(12);


        tvStatus.setTypeface(
                null,
                Typeface.BOLD
        );


        setStatusColor(
                tvStatus,
                status
        );


        topRow.addView(
                tvStatus
        );


        content.addView(
                topRow
        );


        // --------------------------------------
        // Description
        // --------------------------------------

        if (description != null &&
                !description.trim().isEmpty()) {

            TextView tvDescription =
                    new TextView(this);


            tvDescription.setText(
                    description
            );


            tvDescription.setTextSize(
                    16
            );


            tvDescription.setTextColor(
                    Color.rgb(
                            34,
                            34,
                            34
                    )
            );


            tvDescription.setTypeface(
                    null,
                    Typeface.BOLD
            );


            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            params.topMargin =
                    dpToPx(8);


            content.addView(
                    tvDescription,
                    params
            );
        }


        // --------------------------------------
        // Amount
        // --------------------------------------

        TextView tvAmount =
                new TextView(this);


        tvAmount.setText(
                "৳ " +
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                amount
                        )
        );


        tvAmount.setTextSize(
                18
        );


        tvAmount.setTypeface(
                null,
                Typeface.BOLD
        );


        tvAmount.setTextColor(
                Color.rgb(
                        11,
                        107,
                        58
                )
        );


        LinearLayout.LayoutParams amountParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        amountParams.topMargin =
                dpToPx(8);


        content.addView(
                tvAmount,
                amountParams
        );


        // --------------------------------------
        // Date
        // --------------------------------------

        TextView tvDate =
                new TextView(this);


        tvDate.setText(
                "Payment Date: " +
                        formatDate(paymentDate)
        );


        tvDate.setTextSize(
                12
        );


        tvDate.setTextColor(
                Color.rgb(
                        136,
                        136,
                        136
                )
        );


        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        dateParams.topMargin =
                dpToPx(5);


        content.addView(
                tvDate,
                dateParams
        );


        // --------------------------------------
        // Reference
        // --------------------------------------

        if (reference != null &&
                !reference.trim().isEmpty()) {

            TextView tvReference =
                    new TextView(this);


            tvReference.setText(
                    "Reference: " +
                            reference
            );


            tvReference.setTextSize(
                    12
            );


            tvReference.setTextColor(
                    Color.rgb(
                            120,
                            120,
                            120
                    )
            );


            LinearLayout.LayoutParams refParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );


            refParams.topMargin =
                    dpToPx(4);


            content.addView(
                    tvReference,
                    refParams
            );
        }


        card.addView(
                content
        );


        // --------------------------------------
        // Click
        // --------------------------------------

        card.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    PaymentActivity.this,
                                    PaymentDetailsActivity.class
                            );


                    intent.putExtra(
                            "payment_id",
                            id
                    );


                    startActivity(intent);
                }
        );


        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


        cardParams.topMargin =
                dpToPx(10);


        paymentListContainer.addView(
                card,
                cardParams
        );
    }


    // ==========================================
    // STATUS COLOR
    // ==========================================

    private void setStatusColor(
            TextView textView,
            String status
    ) {
        if (textView == null) return;
        if (status == null) {
            textView.setTextColor(Color.GRAY);
            return;
        }

        if (status.equalsIgnoreCase(
                "Paid"
        )) {

            textView.setTextColor(
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

            textView.setTextColor(
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

            textView.setTextColor(
                    Color.rgb(
                            211,
                            47,
                            47
                    )
            );

        } else {

            textView.setTextColor(
                    Color.rgb(
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

        if (dateTime == null ||
                dateTime.trim().isEmpty()) {

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


            Date date =
                    input.parse(
                            dateTime
                    );


            if (date != null) {

                return output.format(
                        date
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return dateTime;
    }


    // ==========================================
    // DP
    // ==========================================

    private int dpToPx(
            int dp
    ) {

        return Math.round(
                dp *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }


    // ==========================================
    // REFRESH
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();


        if (database != null) {

            loadSummary();

            loadPayments();
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