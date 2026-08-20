package com.example.duethallportal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnNotice, btnSeatInfo, btnDining, btnComplain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // XML UI Elements Initialize
        btnNotice = findViewById(R.id.btnNotice);
        btnSeatInfo = findViewById(R.id.btnSeatInfo);
        btnDining = findViewById(R.id.btnDining);
        btnComplain = findViewById(R.id.btnComplain);

        // Notice Board Click Event
        btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Opening Notice Board...", Toast.LENGTH_SHORT).show();
                // intent to NoticeActivity
            }
        });

        // Seat Info Click Event
        btnSeatInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Opening Seat Information...", Toast.LENGTH_SHORT).show();
                // intent to SeatActivity
            }
        });

        // Dining System Click Event
        btnDining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Opening Dining Manager...", Toast.LENGTH_SHORT).show();
                // intent to DiningActivity
            }
        });

        // Complain Box Click Event
        btnComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Opening Complain Box...", Toast.LENGTH_SHORT).show();
                // intent to ComplainActivity
            }
        });
    }
}