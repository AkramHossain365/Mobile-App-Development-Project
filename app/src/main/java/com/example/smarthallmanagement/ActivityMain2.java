package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    BottomNavigationView bottomNavigation;

    Button btnMeal;
    Button btnComplaint;
    Button btnNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.bottomNavigation);

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

                Toast.makeText(
                        MainActivity.this,
                        "Home",
                        Toast.LENGTH_SHORT
                ).show();

                return true;

            } else if (id == R.id.nav_meal) {

                Toast.makeText(
                        MainActivity.this,
                        "Meal section",
                        Toast.LENGTH_SHORT
                ).show();

                // Later:
                // startActivity(new Intent(MainActivity.this, MealActivity.class));

                return true;

            } else if (id == R.id.nav_notices) {

                Toast.makeText(
                        MainActivity.this,
                        "Notice section",
                        Toast.LENGTH_SHORT
                ).show();

                // Later:
                // startActivity(new Intent(MainActivity.this, NoticeActivity.class));

                return true;

            } else if (id == R.id.nav_more) {

                Toast.makeText(
                        MainActivity.this,
                        "More",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }

            return false;
        });


        // Quick action buttons

        btnMeal.setOnClickListener(v -> {

            Toast.makeText(
                    MainActivity.this,
                    "Opening Meal",
                    Toast.LENGTH_SHORT
            ).show();

        });


        btnComplaint.setOnClickListener(v -> {

            Toast.makeText(
                    MainActivity.this,
                    "Opening Complaint",
                    Toast.LENGTH_SHORT
            ).show();

        });


        btnNotice.setOnClickListener(v -> {

            Toast.makeText(
                    MainActivity.this,
                    "Opening Notices",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }
}