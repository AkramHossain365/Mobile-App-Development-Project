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

public class MealActivity extends AppCompatActivity {

    private MaterialToolbar toolbarMeal;
    private BottomNavigationView bottomNavigation;

    private Button btnBreakfast;
    private Button btnLunch;
    private Button btnDinner;
    private Button btnMealHistory;

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

        setContentView(R.layout.activity_meal);

        // Initialize views
        toolbarMeal = findViewById(R.id.toolbarMeal);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        btnMealHistory = findViewById(R.id.btnMealHistory);


        // Bottom Navigation
        bottomNavigation.setSelectedItemId(R.id.nav_meal);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(MealActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_meal) {
                return true;
            } else if (id == R.id.nav_notices) {
                startActivity(new Intent(MealActivity.this, NoticesActivity.class));
                return true;
            }
            return false;
        });


        // Back button
        toolbarMeal.setNavigationOnClickListener(v -> finish());


        // Breakfast button
        btnBreakfast.setOnClickListener(v -> {

            Toast.makeText(
                    MealActivity.this,
                    "Breakfast already taken",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Lunch button
        btnLunch.setOnClickListener(v -> {

            Toast.makeText(
                    MealActivity.this,
                    "Lunch selected",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Dinner button
        btnDinner.setOnClickListener(v -> {

            Toast.makeText(
                    MealActivity.this,
                    "Dinner selected",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Meal history
        btnMealHistory.setOnClickListener(v -> {

            Toast.makeText(
                    MealActivity.this,
                    "Opening meal history...",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }
}