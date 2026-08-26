package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigation;

    MaterialCardView cardProfile, cardMeal, cardComplaint, cardServices, cardNotice,
            cardApplications, cardPayment, cardMaintenance, cardNotice2;

    TextView tvComplaint;
    ComplaintDatabaseHelper complaintDb;

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

        setContentView(R.layout.activity_main);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (toolbar == null || bottomNavigation == null) {
            Toast.makeText(this, "UI Initialization failed", Toast.LENGTH_LONG).show();
            return;
        }

        cardProfile = findViewById(R.id.cardProfile);
        cardMeal = findViewById(R.id.cardMeal);
        cardComplaint = findViewById(R.id.cardComplaint);
        cardServices = findViewById(R.id.cardServices);
        cardNotice = findViewById(R.id.cardNotice);

        cardApplications = findViewById(R.id.cardApplications);
        cardPayment = findViewById(R.id.cardPayment);
        cardMaintenance = findViewById(R.id.cardMaintenance);
        cardNotice2 = findViewById(R.id.cardNotice2);

        tvComplaint = findViewById(R.id.tvComplaint);

        try {
            complaintDb = new ComplaintDatabaseHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Toolbar
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        toolbar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_notification) {

                Toast.makeText(
                        MainActivity.this,
                        "No new notifications",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }

            return false;
        });


        // Bottom Navigation

        bottomNavigation.setSelectedItemId(R.id.nav_home);

        NavigationHelper.setupBottomNavigation(this, bottomNavigation);


        // Insight Card Clicks

        if (cardProfile != null) {
            cardProfile.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            });
        }

        if (cardMeal != null) {
            cardMeal.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, MealActivity.class));
            });
        }

        if (cardComplaint != null) {
            cardComplaint.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
            });
        }

        if (cardServices != null) {
            cardServices.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ServicesActivity.class));
            });
        }

        if (cardNotice != null) {
            cardNotice.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, NoticesActivity.class));
            });
        }

        if (cardApplications != null) {
            cardApplications.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ServicesActivity.class));
            });
        }

        if (cardPayment != null) {
            cardPayment.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            });
        }

        if (cardMaintenance != null) {
            cardMaintenance.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
            });
        }

        if (cardNotice2 != null) {
            cardNotice2.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, NoticesActivity.class));
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (complaintDb != null && tvComplaint != null) {
            int pending = complaintDb.getComplaintsCount("Pending");
            tvComplaint.setText(getString(R.string.pending_complaints_format, pending));
        }
    }

    @Override
    protected void onDestroy() {
        if (complaintDb != null) {
            complaintDb.close();
        }
        super.onDestroy();
    }
}
