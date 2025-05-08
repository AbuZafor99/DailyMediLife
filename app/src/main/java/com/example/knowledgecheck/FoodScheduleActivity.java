package com.example.knowledgecheck;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class FoodScheduleActivity extends AppCompatActivity {

    private RecyclerView mealRecyclerView;
    private Button btnAddMeal;
    private ArrayList<Meal> mealList = new ArrayList<>();
    private MealAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_schedule);

        // Initialize views
        mealRecyclerView = findViewById(R.id.mealRV);
        btnAddMeal = findViewById(R.id.addMealBTN);

        // Setup RecyclerView
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealAdapter(mealList);
        mealRecyclerView.setAdapter(adapter);

        // Load sample data
        loadSampleMeals();

        // Set click listener for add button
        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
    }

    private void loadSampleMeals() {
        mealList.add(new Meal("Breakfast", "08:00 AM", "Oatmeal with fruits", false, R.drawable.breakfast_icon));
        mealList.add(new Meal("Lunch", "01:00 PM", "Grilled chicken with rice", false, R.drawable.lunch_icon));
        mealList.add(new Meal("Dinner", "07:30 PM", "Vegetable salad with fish", true, R.drawable.dinner_icon));

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
        EditText etMealDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSaveMeal = dialogView.findViewById(R.id.btnSaveMeal);

        // Setup spinner with meal types
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

        btnSaveMeal.setOnClickListener(v -> {
            String mealType = spinnerMealType.getSelectedItem().toString();
            String mealTime = etMealTime.getText().toString().trim();
            String mealDetails = etMealDetails.getText().toString().trim();
            boolean hasAlarm = cbHasAlarm.isChecked();

            // Validate inputs
            if (mealTime.isEmpty() || mealDetails.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate time format
            if (!isValidTimeFormat(mealTime)) {
                Toast.makeText(this, "Please enter time in correct format (e.g., 08:00 AM)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get appropriate icon based on meal type
            int iconId = getMealIcon(mealType);

            // Create and add new meal
            Meal newMeal = new Meal(mealType, mealTime, mealDetails, hasAlarm, iconId);
            mealList.add(newMeal);
            sortMealsByTime();

            dialog.dismiss();
        });

        dialog.show();
    }

    private boolean isValidTimeFormat(String time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            format.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private int getMealIcon(String mealType) {
        switch (mealType.toLowerCase()) {
            case "breakfast":
                return R.drawable.breakfast_icon;
            case "lunch":
                return R.drawable.lunch_icon;
            case "dinner":
                return R.drawable.dinner_icon;
            case "snack":
                return R.drawable.snack_icon;
            default:
                return R.drawable.meal_icon;
        }
    }
}