package com.example.smarthallmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class MealActivity extends AppCompatActivity {

    private MaterialToolbar toolbarMeal;

    private Button btnBreakfast;
    private Button btnLunch;
    private Button btnDinner;
    private Button btnMealHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal);

        // Initialize views
        toolbarMeal = findViewById(R.id.toolbarMeal);

        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnDinner = findViewById(R.id.btnDinner);
        btnMealHistory = findViewById(R.id.btnMealHistory);


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