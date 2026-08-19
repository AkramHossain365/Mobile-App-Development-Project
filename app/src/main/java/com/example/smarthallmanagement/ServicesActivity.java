package com.example.smarthallmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class ServicesActivity extends AppCompatActivity {

    MaterialToolbar toolbarServices;
    BottomNavigationView bottomNavigation;

    MaterialCardView cardHallOffice;
    MaterialCardView cardRoomSeat;
    MaterialCardView cardApplications;
    MaterialCardView cardPayment;

    MaterialCardView cardMeal;
    MaterialCardView cardCanteen;

    MaterialCardView cardMaintenance;
    MaterialCardView cardElectricity;
    MaterialCardView cardWater;
    MaterialCardView cardInternet;
    MaterialCardView cardCleaning;
    MaterialCardView cardWashroom;
    MaterialCardView cardFurniture;
    MaterialCardView cardWaste;

    MaterialCardView cardSecurity;


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

        setContentView(R.layout.activity_services);


        // =====================================
        // TOOLBAR
        // =====================================

        toolbarServices = findViewById(R.id.toolbarServices);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        toolbarServices.setNavigationOnClickListener(v -> {
            finish();
        });


        // =====================================
        // BOTTOM NAVIGATION
        // =====================================

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(ServicesActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_meal) {
                startActivity(new Intent(ServicesActivity.this, MealActivity.class));
                return true;
            } else if (id == R.id.nav_notices) {
                startActivity(new Intent(ServicesActivity.this, NoticesActivity.class));
                return true;
            }
            return false;
        });


        // =====================================
        // HALL MANAGEMENT
        // =====================================

        cardHallOffice = findViewById(R.id.cardHallOffice);
        cardRoomSeat = findViewById(R.id.cardRoomSeat);
        cardApplications = findViewById(R.id.cardApplications);
        cardPayment = findViewById(R.id.cardPayment);


        cardHallOffice.setOnClickListener(v -> {

            showMessage(
                    "Hall Office",
                    "Hall office service selected."
            );

        });


        cardRoomSeat.setOnClickListener(v -> {

            showMessage(
                    "Room & Seat",
                    "Room and seat service selected."
            );

        });


        cardApplications.setOnClickListener(v -> {

            showMessage(
                    "Applications",
                    "Hall application service selected."
            );

        });


        cardPayment.setOnClickListener(v -> {

            showMessage(
                    "Hall Payment",
                    "Hall payment service selected."
            );

        });


        // =====================================
        // FOOD & MEAL
        // =====================================

        cardMeal = findViewById(R.id.cardMeal);
        cardCanteen = findViewById(R.id.cardCanteen);


        cardMeal.setOnClickListener(v -> {

            showMessage(
                    "Meal",
                    "Meal management selected."
            );

        });


        cardCanteen.setOnClickListener(v -> {

            showMessage(
                    "Canteen",
                    "Hall canteen service selected."
            );

        });


        // =====================================
        // FACILITIES & MAINTENANCE
        // =====================================

        cardMaintenance = findViewById(R.id.cardMaintenance);
        cardElectricity = findViewById(R.id.cardElectricity);
        cardWater = findViewById(R.id.cardWater);
        cardInternet = findViewById(R.id.cardInternet);
        cardCleaning = findViewById(R.id.cardCleaning);
        cardWashroom = findViewById(R.id.cardWashroom);
        cardFurniture = findViewById(R.id.cardFurniture);
        cardWaste = findViewById(R.id.cardWaste);


        cardMaintenance.setOnClickListener(v -> {

            showMessage(
                    "Maintenance",
                    "Maintenance service selected."
            );

        });


        cardElectricity.setOnClickListener(v -> {

            showMessage(
                    "Electricity",
                    "Electricity service selected."
            );

        });


        cardWater.setOnClickListener(v -> {

            showMessage(
                    "Water",
                    "Water and plumbing service selected."
            );

        });


        cardInternet.setOnClickListener(v -> {

            showMessage(
                    "Internet / Wi-Fi",
                    "Internet service selected."
            );

        });


        cardCleaning.setOnClickListener(v -> {

            showMessage(
                    "Cleaning",
                    "Cleaning service selected."
            );

        });


        cardWashroom.setOnClickListener(v -> {

            showMessage(
                    "Washroom",
                    "Washroom service selected."
            );

        });


        cardFurniture.setOnClickListener(v -> {

            showMessage(
                    "Furniture",
                    "Furniture service selected."
            );

        });


        cardWaste.setOnClickListener(v -> {

            showMessage(
                    "Waste Management",
                    "Waste management service selected."
            );

        });


        // =====================================
        // SECURITY
        // =====================================

        cardSecurity = findViewById(R.id.cardSecurity);


        cardSecurity.setOnClickListener(v -> {

            showMessage(
                    "Security",
                    "Hall security service selected."
            );

        });

    }


    // =========================================
    // TOAST MESSAGE
    // =========================================

    private void showMessage(String title, String message) {

        Toast.makeText(
                ServicesActivity.this,
                message,
                Toast.LENGTH_SHORT
        ).show();

    }

}