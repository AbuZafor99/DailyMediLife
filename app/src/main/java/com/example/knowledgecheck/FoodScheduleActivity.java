package com.example.knowledgecheck;

import android.os.Bundle;
import android.widget.Button;
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

<<<<<<< Updated upstream
        // Initialize views
=======
        dbHelper = new MealDatabaseHelper(this);

>>>>>>> Stashed changes
        mealRecyclerView = findViewById(R.id.mealRV);
        btnAddMeal = findViewById(R.id.addMealBTN);

        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealAdapter(mealList);
        mealRecyclerView.setAdapter(adapter);

<<<<<<< Updated upstream
        // Load sample data
        loadSampleMeals();

        // Set click listeners
=======
        loadMealsFromDatabase();

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        // Implement your dialog to add new meals
        // After adding:
        // mealList.add(newMeal);
        // sortMealsByTime();
=======
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        Spinner spinnerMealType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMealTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etMealDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSaveMeal = dialogView.findViewById(R.id.btnSaveMeal);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

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
                newMeal.setId(id);

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
        showEditMealDialog(meal);
    }

    private void showEditMealDialog(Meal meal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_meal, null);
        builder.setView(dialogView);

        Spinner spinnerMealType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMealTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etMealDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSaveMeal = dialogView.findViewById(R.id.btnSaveMeal);

        spinnerMealType.setSelection(getIndex(spinnerMealType, meal.getMealType()));
        etMealTime.setText(meal.getTime());
        etMealDetails.setText(meal.getDescription());
        cbHasAlarm.setChecked(meal.isAlarmOn());

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

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

            if (mealTime.isEmpty()) {
                Toast.makeText(this, "Please select meal time", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mealDetails.isEmpty()) {
                Toast.makeText(this, "Please enter meal details", Toast.LENGTH_SHORT).show();
                return;
            }

            meal.setMealType(mealType);
            meal.setTime(mealTime);
            meal.setDescription(mealDetails);
            meal.setAlarmOn(hasAlarm);
            meal.setIconRes(getMealIcon(mealType));

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

    public void onDeleteClick(Meal meal) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Meal")
                .setMessage("Are you sure you want to delete this meal?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    dbHelper.deleteMeal(meal.getId());


                    int position = mealList.indexOf(meal);
                    mealList.remove(position);
                    adapter.notifyItemRemoved(position);

                    Toast.makeText(this, "Meal deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
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
>>>>>>> Stashed changes
    }
}