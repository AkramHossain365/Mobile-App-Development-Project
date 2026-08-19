package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    MaterialToolbar toolbarSettings;

    MaterialCardView cardEditProfile;
    MaterialCardView cardChangePassword;
    MaterialCardView cardAbout;
    MaterialCardView cardPrivacy;

    SwitchMaterial switchNotifications;
    SwitchMaterial switchDarkMode;

    MaterialButton btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen / Hide status bar
        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        setContentView(R.layout.activity_settings);


        // ==============================
        // TOOLBAR
        // ==============================

        toolbarSettings = findViewById(R.id.toolbarSettings);

        toolbarSettings.setNavigationOnClickListener(v -> {
            finish();
        });


        // ==============================
        // FIND VIEWS
        // ==============================

        cardEditProfile = findViewById(R.id.cardEditProfile);
        cardChangePassword = findViewById(R.id.cardChangePassword);

        cardAbout = findViewById(R.id.cardAbout);
        cardPrivacy = findViewById(R.id.cardPrivacy);

        switchNotifications = findViewById(R.id.switchNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        btnLogout = findViewById(R.id.btnLogout);


        // ==============================
        // EDIT PROFILE
        // ==============================

        cardEditProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    ProfileActivity.class
            );

            startActivity(intent);

        });


        // ==============================
        // CHANGE PASSWORD
        // ==============================

        cardChangePassword.setOnClickListener(v -> {

            Toast.makeText(
                    SettingsActivity.this,
                    "Change Password selected",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // ==============================
        // NOTIFICATIONS
        // ==============================

        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Notifications enabled",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Notifications disabled",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                }
        );


        // ==============================
        // DARK MODE
        // ==============================

        switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Dark mode enabled",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Dark mode disabled",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                }
        );


        // ==============================
        // ABOUT
        // ==============================

        cardAbout.setOnClickListener(v -> {

            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("DUET Hall Portal")
                    .setMessage(
                            "DUET Hall Portal\n\n" +
                                    "A digital platform for managing " +
                                    "hall-related services, meals, notices, " +
                                    "complaints and student information.\n\n" +
                                    "Version 1.0"
                    )
                    .setPositiveButton("OK", null)
                    .show();

        });


        // ==============================
        // PRIVACY
        // ==============================

        cardPrivacy.setOnClickListener(v -> {

            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("Privacy Policy")
                    .setMessage(
                            "Your personal and hall-related " +
                                    "information should only be used " +
                                    "for authorized hall portal services."
                    )
                    .setPositiveButton("OK", null)
                    .show();

        });


        // ==============================
        // LOGOUT
        // ==============================

        btnLogout.setOnClickListener(v -> {

            showLogoutDialog();

        });

    }


    // =====================================
    // LOGOUT CONFIRMATION
    // =====================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(SettingsActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", (dialog, which) -> {

                    logoutUser();

                })
                .show();

    }


    // =====================================
    // LOGOUT USER
    // =====================================

    private void logoutUser() {

        Toast.makeText(
                SettingsActivity.this,
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();


        /*
         * Later, when you add real login:
         *
         * SharedPreferences preferences =
         *         getSharedPreferences("UserSession", MODE_PRIVATE);
         *
         * preferences.edit().clear().apply();
         */


        Intent intent = new Intent(
                SettingsActivity.this,
                MainActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();

    }

}