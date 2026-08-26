package com.example.smarthallmanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class NoticesActivity extends AppCompatActivity {

    private MaterialToolbar toolbarNotice;
    private BottomNavigationView bottomNavigation;

    private MaterialButton btnAll;
    private MaterialButton btnHall;
    private MaterialButton btnAcademic;
    private MaterialButton btnEmergency;

    private MaterialCardView cardNotice1;
    private MaterialCardView cardNotice2;
    private MaterialCardView cardNotice3;
    private MaterialCardView cardEmergency;

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

        setContentView(R.layout.activity_notices);

        // Toolbar
        toolbarNotice = findViewById(R.id.toolbarNotice);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Category buttons
        btnAll = findViewById(R.id.btnAll);
        btnHall = findViewById(R.id.btnHall);
        btnAcademic = findViewById(R.id.btnAcademic);
        btnEmergency = findViewById(R.id.btnEmergency);

        // Notice cards
        cardNotice1 = findViewById(R.id.cardNotice1);
        cardNotice2 = findViewById(R.id.cardNotice2);
        cardNotice3 = findViewById(R.id.cardNotice3);
        cardEmergency = findViewById(R.id.cardEmergency);


        // Bottom Navigation
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_notices);
            NavigationHelper.setupBottomNavigation(this, bottomNavigation);
        }


        // Back button
        toolbarNotice.setNavigationOnClickListener(v -> {
            finish();
        });


        // All notices
        btnAll.setOnClickListener(v -> {

            showAllNotices();

            Toast.makeText(
                    NoticesActivity.this,
                    "Showing all notices",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // Hall notices
        btnHall.setOnClickListener(v -> {

            showHallNotices();

        });


        // Academic notices
        btnAcademic.setOnClickListener(v -> {

            showAcademicNotices();

        });


        // Emergency notices
        btnEmergency.setOnClickListener(v -> {

            showEmergencyNotices();

        });


        // Notice card click
        cardNotice1.setOnClickListener(v -> {

            Toast.makeText(
                    NoticesActivity.this,
                    "Opening Dining Committee Meeting",
                    Toast.LENGTH_SHORT
            ).show();

        });


        cardNotice2.setOnClickListener(v -> {

            Toast.makeText(
                    NoticesActivity.this,
                    "Opening Water Supply Maintenance",
                    Toast.LENGTH_SHORT
            ).show();

        });


        cardNotice3.setOnClickListener(v -> {

            Toast.makeText(
                    NoticesActivity.this,
                    "Opening Examination Schedule",
                    Toast.LENGTH_SHORT
            ).show();

        });


        cardEmergency.setOnClickListener(v -> {

            Toast.makeText(
                    NoticesActivity.this,
                    "Opening emergency announcement",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    // Show all notices
    private void showAllNotices() {

        cardNotice1.setVisibility(MaterialCardView.VISIBLE);
        cardNotice2.setVisibility(MaterialCardView.VISIBLE);
        cardNotice3.setVisibility(MaterialCardView.VISIBLE);
        cardEmergency.setVisibility(MaterialCardView.VISIBLE);
    }


    // Show Hall notices
    private void showHallNotices() {

        cardNotice1.setVisibility(MaterialCardView.VISIBLE);
        cardNotice2.setVisibility(MaterialCardView.VISIBLE);

        cardNotice3.setVisibility(MaterialCardView.GONE);
        cardEmergency.setVisibility(MaterialCardView.GONE);
    }


    // Show Academic notices
    private void showAcademicNotices() {

        cardNotice1.setVisibility(MaterialCardView.GONE);
        cardNotice2.setVisibility(MaterialCardView.GONE);

        cardNotice3.setVisibility(MaterialCardView.VISIBLE);
        cardEmergency.setVisibility(MaterialCardView.GONE);
    }


    // Show Emergency notices
    private void showEmergencyNotices() {

        cardNotice1.setVisibility(MaterialCardView.GONE);
        cardNotice2.setVisibility(MaterialCardView.GONE);
        cardNotice3.setVisibility(MaterialCardView.GONE);

        cardEmergency.setVisibility(MaterialCardView.VISIBLE);
    }

}