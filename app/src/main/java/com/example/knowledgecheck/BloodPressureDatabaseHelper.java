package com.example.knowledgecheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BloodPressureDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "blood_pressure.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bp_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SYSTOLIC = "systolic";
    private static final String COLUMN_DIASTOLIC = "diastolic";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public BloodPressureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SYSTOLIC + " INTEGER, " +
                COLUMN_DIASTOLIC + " INTEGER, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean saveBPRecord(int systolic, int diastolic) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null); // Delete old records

        ContentValues values = new ContentValues();
        values.put(COLUMN_SYSTOLIC, systolic);
        values.put(COLUMN_DIASTOLIC, diastolic);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public String getLatestBPRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_SYSTOLIC, COLUMN_DIASTOLIC},
                null, null, null, null,
                COLUMN_TIMESTAMP + " DESC", "1");

        String bpValue = "N/A";
        if (cursor.moveToFirst()) {
            int systolic = cursor.getInt(0);
            int diastolic = cursor.getInt(1);
            bpValue = systolic + "/" + diastolic;
        }
        cursor.close();
        db.close();
        return bpValue;
    }
}