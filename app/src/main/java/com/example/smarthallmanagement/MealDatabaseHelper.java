package com.example.smarthallmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "meals.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MEALS = "meal_records";

    public static final String COL_ID = "id";
    public static final String COL_DATE = "meal_date";
    public static final String COL_BREAKFAST = "breakfast";
    public static final String COL_LUNCH = "lunch";
    public static final String COL_DINNER = "dinner";
    public static final String COL_UPDATED_AT = "updated_at";

    public MealDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_MEALS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT UNIQUE NOT NULL, " +
                COL_BREAKFAST + " INTEGER DEFAULT 1, " +
                COL_LUNCH + " INTEGER DEFAULT 1, " +
                COL_DINNER + " INTEGER DEFAULT 1, " +
                COL_UPDATED_AT + " TEXT" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    // ---------------------------------------------------------
    // DATE FORMAT
    // ---------------------------------------------------------

    public static String getTodayDate() {

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return sdf.format(new Date());
    }

    public static String formatDate(Calendar calendar) {

        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return sdf.format(calendar.getTime());
    }

    // ---------------------------------------------------------
    // CHECK WHETHER DATE IS LOCKED
    // ---------------------------------------------------------

    /*
     * A meal date can be modified only before 12:00 AM
     * of that meal date.
     *
     * Example:
     *
     * 26 Aug meal -> can modify until 25 Aug 11:59 PM
     * 27 Aug meal -> can modify until 26 Aug 11:59 PM
     */

    public static boolean isMealDateLocked(String mealDate) {

        try {

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date selectedDate = sdf.parse(mealDate);
            Date today = sdf.parse(getTodayDate());

            return selectedDate == null ||
                    !selectedDate.after(today);

        } catch (Exception e) {

            return true;
        }
    }

    // ---------------------------------------------------------
    // GET RECORD
    // ---------------------------------------------------------

    public Cursor getMeal(String date) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_MEALS,
                null,
                COL_DATE + "=?",
                new String[]{date},
                null,
                null,
                null
        );
    }

    // ---------------------------------------------------------
    // SAVE MEAL
    // ---------------------------------------------------------

    public boolean saveMeal(
            String date,
            boolean breakfast,
            boolean lunch,
            boolean dinner
    ) {

        if (isMealDateLocked(date)) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_DATE, date);
        values.put(COL_BREAKFAST, breakfast ? 1 : 0);
        values.put(COL_LUNCH, lunch ? 1 : 0);
        values.put(COL_DINNER, dinner ? 1 : 0);

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                );

        values.put(
                COL_UPDATED_AT,
                sdf.format(new Date())
        );

        long result = db.insertWithOnConflict(
                TABLE_MEALS,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        return result != -1;
    }

    // ---------------------------------------------------------
    // GET MEAL STATUS
    // ---------------------------------------------------------

    public boolean getBreakfast(String date) {

        return getMealValue(date, COL_BREAKFAST);
    }

    public boolean getLunch(String date) {

        return getMealValue(date, COL_LUNCH);
    }

    public boolean getDinner(String date) {

        return getMealValue(date, COL_DINNER);
    }

    private boolean getMealValue(
            String date,
            String column
    ) {

        Cursor cursor = getMeal(date);

        try {

            if (cursor != null && cursor.moveToFirst()) {

                return cursor.getInt(
                        cursor.getColumnIndexOrThrow(column)
                ) == 1;
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        // Default = meal ON
        return true;
    }

    // ---------------------------------------------------------
    // GET HISTORY
    // ---------------------------------------------------------

    public Cursor getHistory(
            String startDate,
            String endDate
    ) {

        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_MEALS,
                null,
                COL_DATE + " BETWEEN ? AND ?",
                new String[]{
                        startDate,
                        endDate
                },
                null,
                null,
                COL_DATE + " DESC"
        );
    }

    // ---------------------------------------------------------
    // 12 MONTH SUMMARY
    // ---------------------------------------------------------

    public Cursor getYearSummary() {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT " +
                        "strftime('%Y-%m', " + COL_DATE + ") AS month, " +
                        "SUM(" + COL_BREAKFAST + ") AS breakfast, " +
                        "SUM(" + COL_LUNCH + ") AS lunch, " +
                        "SUM(" + COL_DINNER + ") AS dinner " +
                        "FROM " + TABLE_MEALS + " " +
                        "WHERE " + COL_DATE + " >= date('now','-12 months') " +
                        "GROUP BY strftime('%Y-%m', " + COL_DATE + ") " +
                        "ORDER BY month DESC",
                null
        );
    }
}