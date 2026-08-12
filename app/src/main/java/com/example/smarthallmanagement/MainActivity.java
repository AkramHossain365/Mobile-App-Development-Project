package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open CalculatorActivity
        Intent intent = new Intent(
                MainActivity.this,
                CalculatorActivity.class
        );

        startActivity(intent);

        // Close MainActivity so pressing Back
        // does not return to the empty MainActivity
        finish();
    }
}