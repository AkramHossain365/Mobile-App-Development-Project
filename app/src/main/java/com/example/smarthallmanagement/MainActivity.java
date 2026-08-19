package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    MaterialCardView cardProfile, cardMeal, cardComplaint, cardServices, cardNotice;

    Button btnMeal, btnComplaint, btnNotice;

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

        cardProfile = findViewById(R.id.cardProfile);
        cardMeal = findViewById(R.id.cardMeal);
        cardComplaint = findViewById(R.id.cardComplaint);
        cardServices = findViewById(R.id.cardServices);
        cardNotice = findViewById(R.id.cardNotice);

        btnMeal = findViewById(R.id.btnMeal);
        btnComplaint = findViewById(R.id.btnComplaint);
        btnNotice = findViewById(R.id.btnNotice);


        // Toolbar
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

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                return true;

            } else if (id == R.id.nav_meal) {
                startActivity(new Intent(MainActivity.this, MealActivity.class));
                return true;

            } else if (id == R.id.nav_notices) {
                startActivity(new Intent(MainActivity.this, NoticesActivity.class));
                return true;

            } else if (id == R.id.nav_more) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }

            return false;
        });


        // Insight Card Clicks

        cardProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        cardMeal.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MealActivity.class));
        });

        cardComplaint.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
        });

        cardServices.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ServicesActivity.class));
        });

        cardNotice.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NoticesActivity.class));
        });


        // Quick action buttons

        btnMeal.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MealActivity.class));
        });


        btnComplaint.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ComplaintActivity.class));
        });


        btnNotice.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NoticesActivity.class));
        });

    }
}
