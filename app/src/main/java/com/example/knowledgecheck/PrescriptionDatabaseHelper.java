package com.example.knowledgecheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrescriptionDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "prescriptions.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "prescriptions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_HAS_ALARM = "has_alarm";
    private static final String COLUMN_ICON = "icon";

    public PrescriptionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_HAS_ALARM + " INTEGER, " +
                COLUMN_ICON + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addPrescription(Prescription prescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, prescription.getMedicationType());
        values.put(COLUMN_TIME, prescription.getTime());
        values.put(COLUMN_DETAILS, prescription.getDetails());
        values.put(COLUMN_HAS_ALARM, prescription.hasAlarm() ? 1 : 0);
        values.put(COLUMN_ICON, prescription.getIconRes());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_TIME,
                        COLUMN_DETAILS, COLUMN_HAS_ALARM, COLUMN_ICON},
                null, null, null, null,
                COLUMN_TIME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Prescription prescription = new Prescription(
                        cursor.getLong(0),    // id
                        cursor.getString(1),  // type
                        cursor.getString(2),  // time
                        cursor.getString(3),  // details
                        cursor.getInt(4) == 1, // has_alarm
                        cursor.getInt(5)      // icon
                );
                prescriptions.add(prescription);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return prescriptions;
    }

    public Prescription getNextMedication() {
        SQLiteDatabase db = this.getReadableDatabase();
        String currentTime = getCurrentTimeAMPM();

        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE time >= ? ORDER BY time ASC LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{currentTime});

        Prescription prescription = null;
        if (cursor.moveToFirst()) {
            prescription = new Prescription(
                    cursor.getLong(0),    // id
                    cursor.getString(1),  // type
                    cursor.getString(2),  // time
                    cursor.getString(3),  // details
                    cursor.getInt(4) == 1, // has_alarm
                    cursor.getInt(5)      // icon
            );
        }
        cursor.close();
        db.close();
        return prescription;
    }

    public int getPrescriptionCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int updatePrescription(Prescription prescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, prescription.getMedicationType());
        values.put(COLUMN_TIME, prescription.getTime());
        values.put(COLUMN_DETAILS, prescription.getDetails());
        values.put(COLUMN_HAS_ALARM, prescription.hasAlarm() ? 1 : 0);
        values.put(COLUMN_ICON, prescription.getIconRes());

        return db.update(TABLE_NAME, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(prescription.getId())});
    }

    public void deletePrescription(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    private String getCurrentTimeAMPM() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return convertToAMPM(hour, minute);
    }

    private String convertToAMPM(int hour, int minute) {
        String amPm = hour < 12 ? "AM" : "PM";
        int displayHour = hour == 0 ? 12 : (hour > 12 ? hour - 12 : hour);
        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm);
    }
}