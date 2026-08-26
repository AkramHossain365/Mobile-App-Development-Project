package com.example.smarthallmanagement;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PaymentFormActivity
        extends AppCompatActivity {

    private MaterialToolbar toolbar;

    private AutoCompleteTextView spinnerType;

    private TextInputEditText etDescription;
    private TextInputEditText etAmount;

    private AutoCompleteTextView spinnerMethod;

    private TextInputEditText etReference;
    private TextInputEditText etDate;

    private MaterialButton btnSubmit;

    private PaymentDatabaseHelper database;

    private Calendar calendar;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

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


        calendar =
                Calendar.getInstance();


        initializeViews();


        setupSpinners();


        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    v -> finish()
            );
        }


        if (etDate != null) {
            etDate.setOnClickListener(
                    v -> showDatePicker()
            );
        }


        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(
                    v -> savePayment()
            );
        }
    }


    // ==========================================
    // INITIALIZE
    // ==========================================

    private void initializeViews() {

        toolbar =
                findViewById(
                        R.id.toolbarPaymentForm
                );

        spinnerType =
                findViewById(
                        R.id.spinnerPaymentType
                );

        etDescription =
                findViewById(
                        R.id.etPaymentDescription
                );

        etAmount =
                findViewById(
                        R.id.etPaymentAmount
                );

        spinnerMethod =
                findViewById(
                        R.id.spinnerPaymentMethod
                );

        etReference =
                findViewById(
                        R.id.etPaymentReference
                );

        etDate =
                findViewById(
                        R.id.etPaymentDate
                );

        btnSubmit =
                findViewById(
                        R.id.btnSubmitPayment
                );


        // Set current date by default
        updateDateLabel();
    }


    // ==========================================
    // SPINNERS
    // ==========================================

    private void setupSpinners() {
        if (spinnerType == null || spinnerMethod == null) {
            return;
        }

        String[] types = {
                "Monthly Rent",
                "Meal Bill",
                "Utility Bill",
                "Admission Fee",
                "Security Deposit",
                "Others"
        };

        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        types
                );

        spinnerType.setAdapter(
                typeAdapter
        );


        String[] methods = {
                "Cash",
                "bKash",
                "Nagad",
                "Rocket",
                "Bank Transfer",
                "Credit/Debit Card"
        };

        ArrayAdapter<String> methodAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        methods
                );

        spinnerMethod.setAdapter(
                methodAdapter
        );
    }


    // ==========================================
    // DATE PICKER
    // ==========================================

    private void showDatePicker() {

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    updateDateLabel();

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }


    private void updateDateLabel() {
        if (etDate == null || calendar == null) {
            return;
        }

        String format =
                "yyyy-MM-dd";


        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        format,
                        Locale.getDefault()
                );


        etDate.setText(
                sdf.format(
                        calendar.getTime()
                )
        );
    }


    // ==========================================
    // SAVE
    // ==========================================

    private void savePayment() {
        if (spinnerType == null || etDescription == null || etAmount == null ||
                spinnerMethod == null || etReference == null || etDate == null || database == null) {
            return;
        }

        String type =
                spinnerType
                        .getText()
                        .toString()
                        .trim();


        String description =
                etDescription
                        .getText()
                        .toString()
                        .trim();


        String amountStr =
                etAmount
                        .getText()
                        .toString()
                        .trim();


        String method =
                spinnerMethod
                        .getText()
                        .toString()
                        .trim();


        String reference =
                etReference
                        .getText()
                        .toString()
                        .trim();


        String date =
                etDate
                        .getText()
                        .toString()
                        .trim();


        // --------------------------------------
        // Validation
        // --------------------------------------

        if (TextUtils.isEmpty(type)) {

            spinnerType.setError(
                    "Select payment type"
            );

            return;
        }


        if (TextUtils.isEmpty(amountStr)) {

            etAmount.setError(
                    "Enter amount"
            );

            return;
        }


        double amount;

        try {

            amount =
                    Double.parseDouble(
                            amountStr
                    );

        } catch (NumberFormatException e) {

            etAmount.setError(
                    "Invalid amount"
            );

            return;
        }


        if (TextUtils.isEmpty(method)) {

            spinnerMethod.setError(
                    "Select payment method"
            );

            return;
        }


        // --------------------------------------
        // Save to Database
        // --------------------------------------

        try {

            long result =
                    database.insertPayment(
                            type,
                            description,
                            amount,
                            method,
                            reference,
                            "Paid", // Status
                            date
                    );


            if (result != -1) {

                Toast.makeText(
                        this,
                        "Payment recorded successfully",
                        Toast.LENGTH_SHORT
                ).show();


                finish();

            } else {

                Toast.makeText(
                        this,
                        "Failed to record payment: Database Error",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (android.database.SQLException e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Database Error: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Error: " +
                            e.getMessage(),
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
