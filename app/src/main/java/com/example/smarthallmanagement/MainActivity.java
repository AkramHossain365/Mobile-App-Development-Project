package com.example.smarthallmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    TextView tvWelcome, tvStudentName, tvStudentId, tvDepartment, tvRoom;
    ComplaintDatabaseHelper complaintDb;
    DatabaseHelper studentDb;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("SmartHallPreferences", MODE_PRIVATE);
        String loggedInStudentId = preferences.getString("student_id", "");

        // Fullscreen / Hide status bar
        WindowInsetsControllerCompat windowInsetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        if (toolbar == null || bottomNavigation == null) {
            Toast.makeText(this, "UI Initialization failed", Toast.LENGTH_LONG).show();
            return;
        }

        tvWelcome = findViewById(R.id.tvWelcome);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentId = findViewById(R.id.tvStudentId);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvRoom = findViewById(R.id.tvRoom);

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
            studentDb = new DatabaseHelper(this);
            loadStudentData(loggedInStudentId);
        } catch (Exception e) {
            Log.e("MainActivity", "Database Init Error: " + e.getMessage());
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
                startActivity(new Intent(MainActivity.this, ApplicationActivity.class));
            });
        }

        if (cardPayment != null) {
            cardPayment.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
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

    private void loadStudentData(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            Log.d("MainActivity", "No student logged in.");
            return;
        }

        try {
            if (studentDb == null) {
                studentDb = new DatabaseHelper(this);
            }
            
            Cursor cursor = studentDb.getStudent(studentId);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_NAME);
                int deptIndex = cursor.getColumnIndex(DatabaseHelper.COL_DEPARTMENT);
                int hallIndex = cursor.getColumnIndex(DatabaseHelper.COL_HALL);
                int roomIndex = cursor.getColumnIndex(DatabaseHelper.COL_ROOM);

                String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "Student";
                String dept = (deptIndex != -1) ? cursor.getString(deptIndex) : "";
                String hall = (hallIndex != -1) ? cursor.getString(hallIndex) : "";
                String room = (roomIndex != -1) ? cursor.getString(roomIndex) : "Not Assigned";

                if (tvWelcome != null) tvWelcome.setText("Welcome back, " + name + "!");
                if (tvStudentName != null) tvStudentName.setText(name);
                if (tvStudentId != null) tvStudentId.setText("Student ID: " + studentId);
                if (tvDepartment != null) tvDepartment.setText(dept + (hall != null && !hall.isEmpty() ? " • " + hall : ""));
                if (tvRoom != null) tvRoom.setText("Room: " + room);

                cursor.close();
            } else {
                Log.w("MainActivity", "Student record not found for ID: " + studentId);
                Toast.makeText(this, "Session data load failed. Please log in again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error loading student data: " + e.getMessage(), e);
            Toast.makeText(this, "Profile load error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (complaintDb != null && tvComplaint != null) {
                int pending = complaintDb.getComplaintsCount("Pending");
                tvComplaint.setText(pending + " Pending");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "onResume Error: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        if (complaintDb != null) {
            complaintDb.close();
        }
        if (studentDb != null) {
            studentDb.close();
        }
        super.onDestroy();
    }
}
