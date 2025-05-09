package com.example.knowledgecheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class BMIDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bmi.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bmi_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_BMI = "bmi";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public BMIDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HEIGHT + " REAL, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_BMI + " REAL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean saveBMIRecord(float height, float weight, float bmi, String category) {
        // First delete all existing records (we only want to keep the most recent)
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

        // Insert new record
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_BMI, bmi);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public float[] getLatestBMIRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_HEIGHT, COLUMN_WEIGHT, COLUMN_BMI, COLUMN_CATEGORY},
                null, null, null, null,
                COLUMN_TIMESTAMP + " DESC", "1");

        float[] record = null;
        if (cursor.moveToFirst()) {
            record = new float[]{
                    cursor.getFloat(0), // height
                    cursor.getFloat(1), // weight
                    cursor.getFloat(2), // bmi
            };
        }
        cursor.close();
        db.close();
        return record;
    }
    public String getLatestBMIValue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_BMI},
                null, null, null, null,
                COLUMN_TIMESTAMP + " DESC", "1");

        String bmiValue = "N/A";
        if (cursor.moveToFirst()) {
            float bmi = cursor.getFloat(0);
            bmiValue = String.format(Locale.getDefault(), "%.1f", bmi);
        }
        cursor.close();
        db.close();
        return bmiValue;
    }
}