package com.example.smarthallmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApplicationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_hall.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_APPLICATIONS =
            "applications";

    public static final String COL_ID =
            "id";

    public static final String COL_TYPE =
            "application_type";

    public static final String COL_SUBJECT =
            "subject";

    public static final String COL_REASON =
            "reason";

    public static final String COL_DETAILS =
            "details";

    public static final String COL_STATUS =
            "status";

    public static final String COL_CREATED_AT =
            "created_at";

    public static final String COL_UPDATED_AT =
            "updated_at";


    public ApplicationDatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable =
                "CREATE TABLE " +
                        TABLE_APPLICATIONS +
                        " (" +

                        COL_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COL_TYPE +
                        " TEXT NOT NULL, " +

                        COL_SUBJECT +
                        " TEXT NOT NULL, " +

                        COL_REASON +
                        " TEXT NOT NULL, " +

                        COL_DETAILS +
                        " TEXT, " +

                        COL_STATUS +
                        " TEXT NOT NULL DEFAULT 'Pending', " +

                        COL_CREATED_AT +
                        " TEXT NOT NULL, " +

                        COL_UPDATED_AT +
                        " TEXT NOT NULL" +

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
                        TABLE_APPLICATIONS
        );

        onCreate(db);
    }


    // ==========================================
    // CURRENT DATE/TIME
    // ==========================================

    private String getCurrentDateTime() {

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                );

        return sdf.format(
                new Date()
        );
    }


    // ==========================================
    // INSERT APPLICATION
    // ==========================================

    public long insertApplication(
            String type,
            String subject,
            String reason,
            String details
    ) throws android.database.SQLException {

        SQLiteDatabase db =
                getWritableDatabase();

        String currentTime =
                getCurrentDateTime();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_TYPE,
                type
        );

        values.put(
                COL_SUBJECT,
                subject
        );

        values.put(
                COL_REASON,
                reason
        );

        values.put(
                COL_DETAILS,
                details
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

        return db.insertOrThrow(
                TABLE_APPLICATIONS,
                null,
                values
        );
    }


    // ==========================================
    // GET ALL APPLICATIONS
    // ==========================================

    public Cursor getAllApplications() {

        try {
            SQLiteDatabase db =
                    getReadableDatabase();

            return db.query(
                    TABLE_APPLICATIONS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    COL_ID + " DESC"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ==========================================
    // GET APPLICATION BY ID
    // ==========================================

    public Cursor getApplicationById(
            long id
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_APPLICATIONS,
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
    // UPDATE STATUS
    // ==========================================

    public boolean updateStatus(
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
                        TABLE_APPLICATIONS,
                        values,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }


    // ==========================================
    // COUNT ALL
    // ==========================================

    public int getTotalApplications() {

        return getCount(
                null
        );
    }


    // ==========================================
    // COUNT BY STATUS
    // ==========================================

    public int getCountByStatus(
            String status
    ) {

        return getCount(
                status
        );
    }


    private int getCount(
            String status
    ) {

        try {
            SQLiteDatabase db =
                    getReadableDatabase();

            Cursor cursor;

            if (status == null) {

                cursor =
                        db.rawQuery(
                                "SELECT COUNT(*) FROM " +
                                        TABLE_APPLICATIONS,
                                null
                        );

            } else {

                cursor =
                        db.rawQuery(
                                "SELECT COUNT(*) FROM " +
                                        TABLE_APPLICATIONS +
                                        " WHERE " +
                                        COL_STATUS +
                                        "=?",
                                new String[]{
                                        status
                                }
                        );
            }

            int count = 0;

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
                cursor.close();
            }

            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    // ==========================================
    // DELETE APPLICATION
    // ==========================================

    public boolean deleteApplication(
            long id
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        TABLE_APPLICATIONS,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }
}