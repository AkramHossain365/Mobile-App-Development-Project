package com.example.smarthallmanagement;

import android.content.Intent;
import android.database.Cursor;
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

    private MealDatabaseHelper mealDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen / Hide status bar
        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        windowInsetsController.hide(
                WindowInsetsCompat.Type.statusBars()
        );

        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        setContentView(R.layout.activity_meal);

        // Initialize database
        mealDatabaseHelper =
                new MealDatabaseHelper(this);

        // Initialize views
        toolbarMeal = findViewById(R.id.toolbarMeal);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        btnMealHistory = findViewById(R.id.btnMealHistory);

        // Bottom Navigation
        // ---------------------------------------------------------

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_meal);
            NavigationHelper.setupBottomNavigation(this, bottomNavigation);
        }

        // ---------------------------------------------------------
        // Back button
        // ---------------------------------------------------------

        toolbarMeal.setNavigationOnClickListener(
                v -> finish()
        );

        // ---------------------------------------------------------
        // Breakfast
        // ---------------------------------------------------------

        btnBreakfast.setOnClickListener(v -> {

            openMealPlanner("breakfast");

        });

        // ---------------------------------------------------------
        // Lunch
        // ---------------------------------------------------------

        btnLunch.setOnClickListener(v -> {

            openMealPlanner("lunch");

        });

        // ---------------------------------------------------------
        // Dinner
        // ---------------------------------------------------------

        btnDinner.setOnClickListener(v -> {

            openMealPlanner("dinner");

        });

        // ---------------------------------------------------------
        // Meal History
        // ---------------------------------------------------------

        btnMealHistory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MealActivity.this,
                            MealHistoryActivity.class
                    );

            startActivity(intent);

        });
    }

    // ---------------------------------------------------------
    // OPEN MEAL PLANNER
    // ---------------------------------------------------------

    private void openMealPlanner(String mealType) {

        Intent intent =
                new Intent(
                        MealActivity.this,
                        MealPlannerActivity.class
                );

        intent.putExtra(
                "meal_type",
                mealType
        );

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        if (mealDatabaseHelper != null) {
            mealDatabaseHelper.close();
        }

        super.onDestroy();
    }
}