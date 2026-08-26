package com.example.smarthallmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "payments.db";

    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PAYMENTS =
            "payments";

    public static final String COL_ID =
            "id";

    public static final String COL_TYPE =
            "payment_type";

    public static final String COL_DESCRIPTION =
            "description";

    public static final String COL_AMOUNT =
            "amount";

    public static final String COL_METHOD =
            "payment_method";

    public static final String COL_REFERENCE =
            "reference_number";

    public static final String COL_STATUS =
            "status";

    public static final String COL_PAYMENT_DATE =
            "payment_date";

    public static final String COL_CREATED_AT =
            "created_at";

    public static final String COL_UPDATED_AT =
            "updated_at";


    public PaymentDatabaseHelper(Context context) {

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

        String createTable =
                "CREATE TABLE " +
                        TABLE_PAYMENTS +
                        " (" +

                        COL_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COL_TYPE +
                        " TEXT NOT NULL, " +

                        COL_DESCRIPTION +
                        " TEXT, " +

                        COL_AMOUNT +
                        " REAL NOT NULL, " +

                        COL_METHOD +
                        " TEXT, " +

                        COL_REFERENCE +
                        " TEXT, " +

                        COL_STATUS +
                        " TEXT NOT NULL DEFAULT 'Pending', " +

                        COL_PAYMENT_DATE +
                        " TEXT, " +

                        COL_CREATED_AT +
                        " TEXT NOT NULL, " +

                        COL_UPDATED_AT +
                        " TEXT NOT NULL" +

                        ")";

        db.execSQL(createTable);
    }


    // ==========================================
    // DATABASE UPGRADE
    // ==========================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_PAYMENTS
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
    // INSERT PAYMENT
    // ==========================================

    public long insertPayment(
            String type,
            String description,
            double amount,
            String method,
            String reference,
            String status,
            String paymentDate
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
                COL_DESCRIPTION,
                description
        );


        values.put(
                COL_AMOUNT,
                amount
        );


        values.put(
                COL_METHOD,
                method
        );


        values.put(
                COL_REFERENCE,
                reference
        );


        values.put(
                COL_STATUS,
                status
        );


        values.put(
                COL_PAYMENT_DATE,
                paymentDate
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
                TABLE_PAYMENTS,
                null,
                values
        );
    }


    // ==========================================
    // GET ALL PAYMENTS
    // ==========================================

    public Cursor getAllPayments() {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_PAYMENTS,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );
    }


    // ==========================================
    // GET PAYMENT BY ID
    // ==========================================

    public Cursor getPaymentById(
            long id
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        return db.query(
                TABLE_PAYMENTS,
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
    // UPDATE PAYMENT STATUS
    // ==========================================

    public boolean updatePaymentStatus(
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
                        TABLE_PAYMENTS,
                        values,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );


        return result > 0;
    }


    // ==========================================
    // TOTAL PAYMENT COUNT
    // ==========================================

    public int getTotalPayments() {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_PAYMENTS,
                        null
                );


        int count = 0;


        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }


        cursor.close();

        return count;
    }


    // ==========================================
    // COUNT BY STATUS
    // ==========================================

    public int getCountByStatus(
            String status
    ) {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_PAYMENTS +
                                " WHERE " +
                                COL_STATUS +
                                "=?",
                        new String[]{
                                status
                        }
                );


        int count = 0;


        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }


        cursor.close();

        return count;
    }


    // ==========================================
    // TOTAL PAID AMOUNT
    // ==========================================

    public double getTotalPaidAmount() {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT SUM(" +
                                COL_AMOUNT +
                                ") FROM " +
                                TABLE_PAYMENTS +
                                " WHERE " +
                                COL_STATUS +
                                "=?",
                        new String[]{
                                "Paid"
                        }
                );


        double total = 0;


        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {

                total =
                        cursor.getDouble(0);
            }
        }


        cursor.close();

        return total;
    }


    // ==========================================
    // TOTAL DUE AMOUNT
    // ==========================================

    public double getTotalDueAmount() {

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT SUM(" +
                                COL_AMOUNT +
                                ") FROM " +
                                TABLE_PAYMENTS +
                                " WHERE " +
                                COL_STATUS +
                                "=? OR " +
                                COL_STATUS +
                                "=?",
                        new String[]{
                                "Pending",
                                "Overdue"
                        }
                );


        double total = 0;


        if (cursor.moveToFirst()) {

            if (!cursor.isNull(0)) {

                total =
                        cursor.getDouble(0);
            }
        }


        cursor.close();

        return total;
    }


    // ==========================================
    // DELETE PAYMENT
    // ==========================================

    public boolean deletePayment(
            long id
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        int result =
                db.delete(
                        TABLE_PAYMENTS,
                        COL_ID + "=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );


        return result > 0;
    }
}