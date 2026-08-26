package com.example.smarthallmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ==========================================
    // DATABASE
    // ==========================================

    private static final String DATABASE_NAME =
            "smart_hall.db";

    private static final int DATABASE_VERSION = 2;


    // ==========================================
    // STUDENT TABLE
    // ==========================================

    public static final String TABLE_STUDENTS =
            "students";

    public static final String COL_ID =
            "id";

    public static final String COL_STUDENT_ID =
            "student_id";

    public static final String COL_PASSWORD =
            "password";

    public static final String COL_NAME =
            "student_name";

    public static final String COL_DEPARTMENT =
            "department";

    public static final String COL_YEAR =
            "year";

    public static final String COL_SEMESTER =
            "semester";

    public static final String COL_MOBILE =
            "mobile";

    public static final String COL_EMAIL =
            "email";

    public static final String COL_HALL =
            "hall_name";

    public static final String COL_ROOM =
            "room_no";

    public static final String COL_CREATED_AT =
            "created_at";


    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

    }


    // ==========================================
    // CREATE DATABASE
    // ==========================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createStudentsTable =

                "CREATE TABLE " + TABLE_STUDENTS + " (" +

                        COL_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COL_STUDENT_ID +
                        " TEXT UNIQUE NOT NULL, " +

                        COL_PASSWORD +
                        " TEXT NOT NULL, " +

                        COL_NAME +
                        " TEXT, " +

                        COL_DEPARTMENT +
                        " TEXT, " +

                        COL_YEAR +
                        " TEXT, " +

                        COL_SEMESTER +
                        " TEXT, " +

                        COL_MOBILE +
                        " TEXT, " +

                        COL_EMAIL +
                        " TEXT, " +

                        COL_HALL +
                        " TEXT, " +

                        COL_ROOM +
                        " TEXT, " +

                        COL_CREATED_AT +
                        " TEXT" +

                        ")";

        db.execSQL(createStudentsTable);


        // Insert sample student
        insertSampleStudent(db);
    }


    // ==========================================
    // DATABASE UPGRADE
    // ==========================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL(
                "DROP TABLE IF EXISTS "
                        + TABLE_STUDENTS
        );

        onCreate(db);
    }


    // ==========================================
    // SAMPLE STUDENT
    // ==========================================

    private void insertSampleStudent(
            SQLiteDatabase db) {

        ContentValues values =
                new ContentValues();

        values.put(
                COL_STUDENT_ID,
                "2204001"
        );

        values.put(
                COL_PASSWORD,
                "123456"
        );

        values.put(
                COL_NAME,
                "Md. Akram Hossain"
        );

        values.put(
                COL_DEPARTMENT,
                "Computer Science & Engineering"
        );

        values.put(
                COL_YEAR,
                "3rd Year"
        );

        values.put(
                COL_SEMESTER,
                "2nd Semester"
        );

        values.put(
                COL_MOBILE,
                "01700000000"
        );

        values.put(
                COL_EMAIL,
                "student@duet.ac.bd"
        );

        values.put(
                COL_HALL,
                "DUET Hall"
        );

        values.put(
                COL_ROOM,
                "101"
        );

        values.put(
                COL_CREATED_AT,
                String.valueOf(
                        System.currentTimeMillis()
                )
        );

        db.insert(
                TABLE_STUDENTS,
                null,
                values
        );
    }


    // ==========================================
    // LOGIN CHECK
    // ==========================================

    public boolean checkLogin(
            String studentId,
            String password) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.query(

                TABLE_STUDENTS,

                new String[]{
                        COL_ID
                },

                COL_STUDENT_ID +
                        "=? AND " +
                        COL_PASSWORD +
                        "=?",

                new String[]{
                        studentId,
                        password
                },

                null,
                null,
                null
        );

        boolean exists =
                cursor != null &&
                        cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return exists;
    }


    // ==========================================
    // GET STUDENT INFORMATION
    // ==========================================

    public Cursor getStudent(
            String studentId) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(

                TABLE_STUDENTS,

                null,

                COL_STUDENT_ID + "=?",

                new String[]{
                        studentId
                },

                null,
                null,
                null
        );
    }


    // ==========================================
    // REGISTER STUDENT
    // ==========================================

    public boolean registerStudent(
            String studentId,
            String password,
            String name,
            String department,
            String year,
            String semester,
            String mobile,
            String email) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_STUDENT_ID,
                studentId
        );

        values.put(
                COL_PASSWORD,
                password
        );

        values.put(
                COL_NAME,
                name
        );

        values.put(
                COL_DEPARTMENT,
                department
        );

        values.put(
                COL_YEAR,
                year
        );

        values.put(
                COL_SEMESTER,
                semester
        );

        values.put(
                COL_MOBILE,
                mobile
        );

        values.put(
                COL_EMAIL,
                email
        );

        values.put(
                COL_HALL,
                "DUET Hall"
        );

        values.put(
                COL_ROOM,
                "Not Assigned"
        );

        values.put(
                COL_CREATED_AT,
                String.valueOf(
                        System.currentTimeMillis()
                )
        );

        long result =
                db.insert(
                        TABLE_STUDENTS,
                        null,
                        values
                );

        db.close();

        return result != -1;
    }


    // ==========================================
    // CHECK STUDENT ID
    // ==========================================

    public boolean studentExists(
            String studentId) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.query(

                TABLE_STUDENTS,

                new String[]{
                        COL_ID
                },

                COL_STUDENT_ID + "=?",

                new String[]{
                        studentId
                },

                null,
                null,
                null
        );

        boolean exists =
                cursor != null &&
                        cursor.moveToFirst();

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return exists;
    }


    // ==========================================
    // UPDATE PASSWORD
    // ==========================================

    public boolean updatePassword(
            String studentId,
            String newPassword) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COL_PASSWORD,
                newPassword
        );

        int result =
                db.update(

                        TABLE_STUDENTS,

                        values,

                        COL_STUDENT_ID + "=?",

                        new String[]{
                                studentId
                        }
                );

        db.close();

        return result > 0;
    }
}