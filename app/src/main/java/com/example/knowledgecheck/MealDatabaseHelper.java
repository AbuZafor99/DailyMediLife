package com.example.knowledgecheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meals.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "meals";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_HAS_ALARM = "has_alarm";
    private static final String COLUMN_ICON = "icon";

    public MealDatabaseHelper(Context context) {
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

    public long addMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, meal.getMealType());
        values.put(COLUMN_TIME, meal.getTime());
        values.put(COLUMN_DETAILS, meal.getDescription());
        values.put(COLUMN_HAS_ALARM, meal.isAlarmOn() ? 1 : 0);
        values.put(COLUMN_ICON, meal.getIconRes());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_TIME,
                        COLUMN_DETAILS, COLUMN_HAS_ALARM, COLUMN_ICON},
                null, null, null, null,
                COLUMN_TIME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Meal meal = new Meal(
                        cursor.getLong(0),    // id
                        cursor.getString(1),  // type
                        cursor.getString(2),  // time
                        cursor.getString(3),  // details
                        cursor.getInt(4) == 1, // has_alarm
                        cursor.getInt(5)      // icon
                );
                meals.add(meal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return meals;
    }

    public int updateMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, meal.getMealType());
        values.put(COLUMN_TIME, meal.getTime());
        values.put(COLUMN_DETAILS, meal.getDescription());
        values.put(COLUMN_HAS_ALARM, meal.isAlarmOn() ? 1 : 0);
        values.put(COLUMN_ICON, meal.getIconRes());

        return db.update(TABLE_NAME, values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(meal.getId())});
    }

    public void deleteMeal(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Meal getNextMeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Meal meal = null;

        // Get current time in 12-hour format (HH:MM AM/PM)
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        // Query to get the next meal after current time
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE time > ? ORDER BY time ASC LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{currentTime});

        if (cursor.moveToFirst()) {
            meal = new Meal(
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HAS_ALARM)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON))
            );
        }

        cursor.close();

        // If no future meals found, get the first meal of the day
        if (meal == null) {
            String firstMealQuery = "SELECT * FROM " + TABLE_NAME +
                    " ORDER BY time ASC LIMIT 1";
            cursor = db.rawQuery(firstMealQuery, null);

            if (cursor.moveToFirst()) {
                meal = new Meal(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HAS_ALARM)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON))
                );
            }
            cursor.close();
        }

        db.close();
        return meal;
    }
}