package com.example.knowledgecheck;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodScheduleActivity extends AppCompatActivity implements MealAdapter.OnMealClickListener {

    private RecyclerView mealRecyclerView;
    private Button btnAddMeal;
    private List<Meal> mealList = new ArrayList<>();
    private MealAdapter adapter;
    private MealDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_schedule);

        // Initialize database helper
        dbHelper = new MealDatabaseHelper(this);

        // Initialize views
        mealRecyclerView = findViewById(R.id.mealRV);
        btnAddMeal = findViewById(R.id.addMealBTN);

        // Setup RecyclerView
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealAdapter(mealList, this);
        mealRecyclerView.setAdapter(adapter);

        // Load meals from database
        loadMealsFromDatabase();

        // Set click listener for add button
        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
    }

    private void loadMealsFromDatabase() {
        mealList.clear();
        mealList.addAll(dbHelper.getAllMeals());
        sortMealsByTime();
    }

    private void sortMealsByTime() {
        Collections.sort(mealList, (m1, m2) -> {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            try {
                Date time1 = format.parse(m1.getTime());
                Date time2 = format.parse(m2.getTime());
                return time1.compareTo(time2);
            } catch (ParseException e) {
                return 0;
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showAddMealDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        // Initialize dialog views
        Spinner spinnerMealType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMealTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etMealDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSaveMeal = dialogView.findViewById(R.id.btnSaveMeal);

        // Setup spinner with meal types
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

        // Time picker click listener
        btnTimePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        String amPm;
                        if (selectedHour < 12) {
                            amPm = "AM";
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                        } else {
                            amPm = "PM";
                            if (selectedHour > 12) {
                                selectedHour -= 12;
                            }
                        }

                        String formattedTime = String.format(Locale.getDefault(),
                                "%02d:%02d %s", selectedHour, selectedMinute, amPm);
                        etMealTime.setText(formattedTime);
                    },
                    hour,
                    minute,
                    false
            );
            timePickerDialog.show();
        });

        btnSaveMeal.setOnClickListener(v -> {
            String mealType = spinnerMealType.getSelectedItem().toString();
            String mealTime = etMealTime.getText().toString().trim();
            String mealDetails = etMealDetails.getText().toString().trim();
            boolean hasAlarm = cbHasAlarm.isChecked();

            // Validate inputs
            if (mealTime.isEmpty()) {
                Toast.makeText(this, "Please select meal time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mealDetails.isEmpty()) {
                Toast.makeText(this, "Please enter meal details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get appropriate icon based on meal type
            int iconId = getMealIcon(mealType);

            // Create and add new meal to database
            Meal newMeal = new Meal(mealType, mealTime, mealDetails, hasAlarm, iconId);
            long id = dbHelper.addMeal(newMeal);

            if (id != -1) {
                // Set the ID returned from database
                newMeal.setId(id);

                // Add to the list and refresh
                mealList.add(newMeal);
                sortMealsByTime();

                Toast.makeText(this, "Meal added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add meal", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onMealClick(Meal meal) {
        // Handle meal click (you can implement edit functionality here)
        showEditMealDialog(meal);
    }

    private void showEditMealDialog(Meal meal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        // Initialize dialog views
        Spinner spinnerMealType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMealTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etMealDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSaveMeal = dialogView.findViewById(R.id.btnSaveMeal);

        // Set initial values
        spinnerMealType.setSelection(getIndex(spinnerMealType, meal.getMealType()));
        etMealTime.setText(meal.getTime());
        etMealDetails.setText(meal.getDescription());
        cbHasAlarm.setChecked(meal.isAlarmOn());

        // Setup spinner with meal types
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

        // Time picker click listener
        btnTimePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        String amPm;
                        if (selectedHour < 12) {
                            amPm = "AM";
                            if (selectedHour == 0) {
                                selectedHour = 12;
                            }
                        } else {
                            amPm = "PM";
                            if (selectedHour > 12) {
                                selectedHour -= 12;
                            }
                        }

                        String formattedTime = String.format(Locale.getDefault(),
                                "%02d:%02d %s", selectedHour, selectedMinute, amPm);
                        etMealTime.setText(formattedTime);
                    },
                    hour,
                    minute,
                    false
            );
            timePickerDialog.show();
        });

        btnSaveMeal.setOnClickListener(v -> {
            String mealType = spinnerMealType.getSelectedItem().toString();
            String mealTime = etMealTime.getText().toString().trim();
            String mealDetails = etMealDetails.getText().toString().trim();
            boolean hasAlarm = cbHasAlarm.isChecked();

            // Validate inputs
            if (mealTime.isEmpty()) {
                Toast.makeText(this, "Please select meal time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mealDetails.isEmpty()) {
                Toast.makeText(this, "Please enter meal details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the meal
            meal.setMealType(mealType);
            meal.setTime(mealTime);
            meal.setDescription(mealDetails);
            meal.setAlarmOn(hasAlarm);
            meal.setIconRes(getMealIcon(mealType));

            // Update in database
            int rowsAffected = dbHelper.updateMeal(meal);

            if (rowsAffected > 0) {
                sortMealsByTime();
                Toast.makeText(this, "Meal updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to update meal", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onAlarmToggle(Meal meal, boolean isOn) {
        // Update alarm status in database
        meal.setAlarmOn(isOn);
        dbHelper.updateMeal(meal);
    }

    private int getMealIcon(String mealType) {
        switch (mealType.toLowerCase()) {
            case "breakfast": return R.drawable.breakfast_icon;
            case "lunch": return R.drawable.lunch_icon;
            case "dinner": return R.drawable.dinner_icon;
            case "snack": return R.drawable.snack_icon;
            default: return R.drawable.meal_icon;
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}