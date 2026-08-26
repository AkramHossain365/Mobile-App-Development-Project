package com.example.smarthallmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComplaintDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "complaints.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COMPLAINTS = "complaints";

    public static final String COL_ID = "id";
    public static final String COL_CATEGORY = "category";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PRIORITY = "priority";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    public ComplaintDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " + TABLE_COMPLAINTS + " (" +

                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COL_CATEGORY + " TEXT NOT NULL, " +

                        COL_SUBJECT + " TEXT NOT NULL, " +

                        COL_DESCRIPTION + " TEXT NOT NULL, " +

                        COL_PRIORITY + " TEXT NOT NULL, " +

                        COL_STATUS + " TEXT NOT NULL DEFAULT 'Pending', " +

                        COL_CREATED_AT + " TEXT NOT NULL, " +

                        COL_UPDATED_AT + " TEXT NOT NULL" +

                        ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_COMPLAINTS
        );

        onCreate(db);
    }

    // ==========================================
    // CURRENT DATE AND TIME
    // ==========================================

    private String getCurrentDateTime() {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                );

        return sdf.format(new Date());
    }

    public static String formatComplaintDate(String dateTime) {

        try {

            SimpleDateFormat inputFormat =
                    new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                    );

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                    );

            Date date =
                    inputFormat.parse(dateTime);

            if (date != null) {
                return outputFormat.format(date);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return dateTime;
    }

    // ==========================================
    // INSERT COMPLAINT
    // ==========================================

    public long insertComplaint(
            String category,
            String subject,
            String description,
            String priority
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        String currentTime =
                getCurrentDateTime();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_CATEGORY,
                category
        );

        values.put(
                COL_SUBJECT,
                subject
        );

        values.put(
                COL_DESCRIPTION,
                description
        );

        values.put(
                COL_PRIORITY,
                priority
        );

        values.put(
                COL_STATUS,
                "Pending"
        );

        values.put(
                COL_CREATED_AT,
                currentTime
        );

        values.put(
                COL_UPDATED_AT,
                currentTime
        );

        return db.insert(
                TABLE_COMPLAINTS,
                null,
                values
        );
    }

    // ==========================================
    // GET ALL COMPLAINTS
    // ==========================================

    public Cursor getAllComplaints() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_COMPLAINTS,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );
    }

    // ==========================================
    // GET COMPLAINTS COUNT
    // ==========================================

    public int getComplaintsCount(String status) {

        try {
            SQLiteDatabase db =
                    getReadableDatabase();

            String selection = null;
            String[] selectionArgs = null;

            if (status != null) {
                selection = COL_STATUS + "=?";
                selectionArgs = new String[]{status};
            }

            try (Cursor cursor = db.query(
                    TABLE_COMPLAINTS,
                    new String[]{COL_ID},
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            )) {
                if (cursor != null) {
                    return cursor.getCount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalComplaintsCount() {
        return getComplaintsCount(null);
    }

    // ==========================================
    // GET SINGLE COMPLAINT
    // ==========================================

    public Cursor getComplaintById(long id) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_COMPLAINTS,
                null,
                COL_ID + "=?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null
        );
    }

    // ==========================================
    // UPDATE COMPLAINT STATUS
    // ==========================================

    public boolean updateComplaintStatus(
            long id,
            String status
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_STATUS,
                status
        );

        values.put(
                COL_UPDATED_AT,
                getCurrentDateTime()
        );

        int result =
                db.update(
                        TABLE_COMPLAINTS,
                        values,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }

    // ==========================================
    // DELETE COMPLAINT
    // ==========================================

    public boolean deleteComplaint(long id) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        TABLE_COMPLAINTS,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }
}