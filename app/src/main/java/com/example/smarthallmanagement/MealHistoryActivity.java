package com.example.smarthallmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MealHistoryActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Spinner spinnerMonth;

    private TextView tvBreakfastCount;
    private TextView tvLunchCount;
    private TextView tvDinnerCount;
    private TextView tvOffCount;

    private TextView tvHistory;

    private MealDatabaseHelper database;

    private final ArrayList<String> monthDates =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.hide(
                WindowInsetsCompat.Type.statusBars()
        );

        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        setContentView(
                R.layout.activity_meal_history
        );

        database =
                new MealDatabaseHelper(this);

        toolbar =
                findViewById(
                        R.id.toolbarMealHistory
                );

        spinnerMonth =
                findViewById(
                        R.id.spinnerMonth
                );

        tvBreakfastCount =
                findViewById(
                        R.id.tvBreakfastCount
                );

        tvLunchCount =
                findViewById(
                        R.id.tvLunchCount
                );

        tvDinnerCount =
                findViewById(
                        R.id.tvDinnerCount
                );

        tvOffCount =
                findViewById(
                        R.id.tvOffCount
                );

        tvHistory =
                findViewById(
                        R.id.tvHistory
                );

        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        setupMonths();

        loadSelectedMonth();

        spinnerMonth.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id
                    ) {

                        loadSelectedMonth();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                    }
                }
        );

        load12MonthSummary();
    }

    // ---------------------------------------------------------
    // MONTH LIST
    // ---------------------------------------------------------

    private void setupMonths() {

        ArrayList<String> monthNames =
                new ArrayList<>();

        SimpleDateFormat displayFormat =
                new SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                );

        Calendar calendar =
                Calendar.getInstance();

        /*
         * Last 3 months including current month.
         */

        for (int i = 0; i < 3; i++) {

            Calendar temp =
                    (Calendar) calendar.clone();

            temp.add(
                    Calendar.MONTH,
                    -i
            );

            monthNames.add(
                    displayFormat.format(
                            temp.getTime()
                    )
            );

            monthDates.add(
                    new SimpleDateFormat(
                            "yyyy-MM",
                            Locale.getDefault()
                    ).format(
                            temp.getTime()
                    )
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        monthNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerMonth.setAdapter(adapter);
    }

    // ---------------------------------------------------------
    // SELECTED MONTH
    // ---------------------------------------------------------

    private void loadSelectedMonth() {

        if (monthDates.isEmpty()) {
            return;
        }

        int position =
                spinnerMonth.getSelectedItemPosition();

        if (position < 0 ||
                position >= monthDates.size()) {
            return;
        }

        String month =
                monthDates.get(position);

        String startDate =
                month + "-01";

        Calendar calendar =
                Calendar.getInstance();

        calendar.set(
                Calendar.MONTH,
                calendar.get(
                        Calendar.MONTH
                ) - position
        );

        int lastDay =
                calendar.getActualMaximum(
                        Calendar.DAY_OF_MONTH
                );

        String endDate =
                String.format(
                        Locale.getDefault(),
                        "%s-%02d",
                        month,
                        lastDay
                );

        Cursor cursor =
                database.getHistory(
                        startDate,
                        endDate
                );

        StringBuilder history =
                new StringBuilder();

        int breakfastCount = 0;
        int lunchCount = 0;
        int dinnerCount = 0;

        while (cursor.moveToNext()) {

            String date =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    MealDatabaseHelper.COL_DATE
                            )
                    );

            boolean breakfast =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    MealDatabaseHelper.COL_BREAKFAST
                            )
                    ) == 1;

            boolean lunch =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    MealDatabaseHelper.COL_LUNCH
                            )
                    ) == 1;

            boolean dinner =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    MealDatabaseHelper.COL_DINNER
                            )
                    ) == 1;

            if (breakfast) {
                breakfastCount++;
            }

            if (lunch) {
                lunchCount++;
            }

            if (dinner) {
                dinnerCount++;
            }

            history.append(date)
                    .append("\n");

            history.append(
                    "Breakfast: "
            ).append(
                    breakfast ? "ON" : "OFF"
            );

            history.append("   ");

            history.append(
                    "Lunch: "
            ).append(
                    lunch ? "ON" : "OFF"
            );

            history.append("   ");

            history.append(
                    "Dinner: "
            ).append(
                    dinner ? "ON" : "OFF"
            );

            history.append("\n\n");
        }

        cursor.close();

        tvBreakfastCount.setText(
                "Breakfast: " + breakfastCount
        );

        tvLunchCount.setText(
                "Lunch: " + lunchCount
        );

        tvDinnerCount.setText(
                "Dinner: " + dinnerCount
        );

        int totalOff =
                countOffMeals(
                        startDate,
                        endDate
                );

        tvOffCount.setText(
                "Off meals: " + totalOff
        );

        if (history.length() == 0) {

            tvHistory.setText(
                    "No meal history available."
            );

        } else {

            tvHistory.setText(
                    history.toString()
            );
        }
    }

    // ---------------------------------------------------------
    // COUNT OFF MEALS
    // ---------------------------------------------------------

    private int countOffMeals(
            String startDate,
            String endDate
    ) {

        Cursor cursor =
                database.getHistory(
                        startDate,
                        endDate
                );

        int offCount = 0;

        while (cursor.moveToNext()) {

            if (cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            MealDatabaseHelper.COL_BREAKFAST
                    )
            ) == 0) {
                offCount++;
            }

            if (cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            MealDatabaseHelper.COL_LUNCH
                    )
            ) == 0) {
                offCount++;
            }

            if (cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            MealDatabaseHelper.COL_DINNER
                    )
            ) == 0) {
                offCount++;
            }
        }

        cursor.close();

        return offCount;
    }

    // ---------------------------------------------------------
    // 12 MONTH SUMMARY
    // ---------------------------------------------------------

    private void load12MonthSummary() {

        Cursor cursor =
                database.getYearSummary();

        /*
         * At the moment this data is available
         * through SQLite.
         *
         * You can later display it in a
         * RecyclerView / chart.
         */

        cursor.close();
    }

    @Override
    protected void onDestroy() {

        if (database != null) {
            database.close();
        }

        super.onDestroy();
    }
}