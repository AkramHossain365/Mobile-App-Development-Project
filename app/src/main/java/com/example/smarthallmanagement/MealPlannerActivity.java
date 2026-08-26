package com.example.smarthallmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealPlannerActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private CalendarView calendarView;

    private TextView tvSelectedDate;
    private TextView tvCutoff;

    private SwitchMaterial switchBreakfast;
    private SwitchMaterial switchLunch;
    private SwitchMaterial switchDinner;

    private Button btnSaveMeal;

    private MealDatabaseHelper database;

    private String selectedDate;

    private String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide status bar
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

        setContentView(R.layout.activity_meal_planner);

        database =
                new MealDatabaseHelper(this);

        // Get meal type
        mealType =
                getIntent().getStringExtra("meal_type");

        // Initialize views
        toolbar =
                findViewById(R.id.toolbarMealPlanner);

        calendarView =
                findViewById(R.id.calendarMeal);

        tvSelectedDate =
                findViewById(R.id.tvSelectedDate);

        tvCutoff =
                findViewById(R.id.tvCutoff);

        switchBreakfast =
                findViewById(R.id.switchBreakfast);

        switchLunch =
                findViewById(R.id.switchLunch);

        switchDinner =
                findViewById(R.id.switchDinner);

        btnSaveMeal =
                findViewById(R.id.btnSaveMeal);

        // Back
        toolbar.setNavigationOnClickListener(
                v -> finish()
        );

        // Initial date = tomorrow
        Calendar tomorrow =
                Calendar.getInstance();

        tomorrow.add(
                Calendar.DAY_OF_MONTH,
                1
        );

        calendarView.setDate(
                tomorrow.getTimeInMillis()
        );

        selectedDate =
                MealDatabaseHelper.formatDate(
                        tomorrow
                );

        updateDateUI();

        // Date changed
        calendarView.setOnDateChangeListener(
                (view, year, month, dayOfMonth) -> {

                    Calendar calendar =
                            Calendar.getInstance();

                    calendar.set(
                            year,
                            month,
                            dayOfMonth
                    );

                    selectedDate =
                            MealDatabaseHelper.formatDate(
                                    calendar
                            );

                    updateDateUI();
                }
        );

        // Save
        btnSaveMeal.setOnClickListener(v ->
                saveMeal()
        );
    }

    // ---------------------------------------------------------
    // UPDATE DATE UI
    // ---------------------------------------------------------

    private void updateDateUI() {

        tvSelectedDate.setText(
                "Selected date: " + selectedDate
        );

        boolean locked =
                MealDatabaseHelper
                        .isMealDateLocked(
                                selectedDate
                        );

        if (locked) {

            tvCutoff.setText(
                    "Meal selection is locked for this date."
            );

            switchBreakfast.setEnabled(false);
            switchLunch.setEnabled(false);
            switchDinner.setEnabled(false);

            btnSaveMeal.setEnabled(false);

            return;
        }

        tvCutoff.setText(
                "You can change this meal until 12:00 AM " +
                        "before the selected date."
        );

        switchBreakfast.setEnabled(true);
        switchLunch.setEnabled(true);
        switchDinner.setEnabled(true);

        btnSaveMeal.setEnabled(true);

        // Load saved data
        loadMealStatus();
    }

    // ---------------------------------------------------------
    // LOAD EXISTING MEAL STATUS
    // ---------------------------------------------------------

    private void loadMealStatus() {

        boolean breakfast =
                database.getBreakfast(
                        selectedDate
                );

        boolean lunch =
                database.getLunch(
                        selectedDate
                );

        boolean dinner =
                database.getDinner(
                        selectedDate
                );

        switchBreakfast.setChecked(
                breakfast
        );

        switchLunch.setChecked(
                lunch
        );

        switchDinner.setChecked(
                dinner
        );
    }

    // ---------------------------------------------------------
    // SAVE
    // ---------------------------------------------------------

    private void saveMeal() {

        boolean breakfast =
                switchBreakfast.isChecked();

        boolean lunch =
                switchLunch.isChecked();

        boolean dinner =
                switchDinner.isChecked();

        boolean saved =
                database.saveMeal(
                        selectedDate,
                        breakfast,
                        lunch,
                        dinner
                );

        if (saved) {

            Toast.makeText(
                    this,
                    "Meal plan saved for " +
                            selectedDate,
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Meal selection is closed for this date.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        if (database != null) {
            database.close();
        }

        super.onDestroy();
    }
}