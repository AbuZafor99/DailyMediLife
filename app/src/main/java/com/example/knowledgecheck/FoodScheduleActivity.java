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

        // Initialize views
        mealRecyclerView = findViewById(R.id.mealRV);
        btnAddMeal = findViewById(R.id.addMealBTN);

        // Setup RecyclerView
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealAdapter(mealList);
        mealRecyclerView.setAdapter(adapter);

        // Load sample data
        loadSampleMeals();

        // Set click listeners
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
        // Implement your dialog to add new meals
        // After adding:
        // mealList.add(newMeal);
        // sortMealsByTime();
    }
}