package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ProfileActivity extends AppCompatActivity {

    private MaterialToolbar toolbarProfile;

    private MaterialCardView cardEditProfile;

    private MaterialButton btnPaymentHistory;
    private MaterialButton btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        // =====================================
        // Initialize Views
        // =====================================

        toolbarProfile =
                findViewById(R.id.toolbarProfile);

        cardEditProfile =
                findViewById(R.id.cardEditProfile);

        btnPaymentHistory =
                findViewById(R.id.btnPaymentHistory);

        btnLogout =
                findViewById(R.id.btnLogout);


        // =====================================
        // Back Button
        // =====================================

        toolbarProfile.setNavigationOnClickListener(v -> {
            finish();
        });


        // =====================================
        // Edit Profile
        // =====================================

        cardEditProfile.setOnClickListener(v -> {

            Toast.makeText(
                    ProfileActivity.this,
                    "Edit Profile selected",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================
        // Payment History
        // =====================================

        btnPaymentHistory.setOnClickListener(v -> {

            Toast.makeText(
                    ProfileActivity.this,
                    "Opening payment history...",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================
        // Logout
        // =====================================

        btnLogout.setOnClickListener(v -> {

            showLogoutDialog();

        });

    }


    // =====================================
    // Logout Dialog
    // =====================================

    private void showLogoutDialog() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Logout");

        builder.setMessage(
                "Are you sure you want to logout?"
        );


        builder.setNegativeButton(
                "Cancel",
                (dialog, which) -> dialog.dismiss()
        );


        builder.setPositiveButton(
                "Logout",
                (dialog, which) -> logoutUser()
        );


        builder.show();

    }


    // =====================================
    // Logout
    // =====================================

    private void logoutUser() {

        Toast.makeText(
                ProfileActivity.this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();


        /*
         * Later, when LoginActivity is created,
         * use the following code:
         *
         * Intent intent = new Intent(
         *      ProfileActivity.this,
         *      LoginActivity.class
         * );
         *
         * intent.setFlags(
         *      Intent.FLAG_ACTIVITY_NEW_TASK |
         *      Intent.FLAG_ACTIVITY_CLEAR_TASK
         * );
         *
         * startActivity(intent);
         */

        finish();

    }

}