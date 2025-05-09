package com.example.knowledgecheck;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Views
    private CardView bmiCV, waterIntakeCV, funCV, cardHealthTips;
    private TextView tvDate, seeAllFoodTV, seeAllTaskTV, seeAllPrescriptions, tvBmiValue, tvWaterValue;
    private TextView tvNextMeal, tvNextMed, tvMedCount, tvTipTitle, tvTipContent;
    private Button addPrescriptions, addFood;
    private ImageView ivProfile;

    // Database Helpers
    private BMIDatabaseHelper bmiDbHelper;
    private BloodPressureDatabaseHelper bpDbHelper;
    private PrescriptionDatabaseHelper prescriptionDbHelper;
    private MealDatabaseHelper mealDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize database helpers
        bmiDbHelper = new BMIDatabaseHelper(this);
        bpDbHelper = new BloodPressureDatabaseHelper(this);
        prescriptionDbHelper = new PrescriptionDatabaseHelper(this);
        mealDbHelper = new MealDatabaseHelper(this);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        setUserGreeting();
        setCurrentDate();


        loadLatestBMI();
        loadLatestBP();
        loadPrescriptionData();
        loadNextMeal();

        fetchHealthTip();

        setupClickListeners();
    }

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        bmiCV = findViewById(R.id.cardBmi);
        waterIntakeCV = findViewById(R.id.cardWaterIntake);
        seeAllFoodTV = findViewById(R.id.seeAllFoodTV);
        seeAllTaskTV = findViewById(R.id.seeAllTaskTV);
        funCV = findViewById(R.id.funCV);
        ivProfile = findViewById(R.id.ivProfile);
        seeAllPrescriptions = findViewById(R.id.tvSeeAllPrescriptions);
        addFood = findViewById(R.id.btnAddFood);
        addPrescriptions = findViewById(R.id.btnAddPrescription);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvWaterValue = findViewById(R.id.tvWaterValue);
        tvNextMed = findViewById(R.id.tvNextMed);
        tvMedCount = findViewById(R.id.tvMedCount);
        tvNextMeal = findViewById(R.id.tvNextMeal);
        tvTipTitle = findViewById(R.id.tvTipTitle);
        tvTipContent = findViewById(R.id.tvTipContent);
        cardHealthTips = findViewById(R.id.cardHealthTips);
    }

    private void setupClickListeners() {
        // BMI Card
        bmiCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, BMICalculateActivity.class));
        });

        // Water Intake (Blood Pressure) Card
        waterIntakeCV.setOnClickListener(v -> {
            showBPInputDialog();
        });

        // See All Food
        seeAllFoodTV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, FoodScheduleActivity.class));
        });

        // See All Prescriptions
        seeAllPrescriptions.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, PrescriptionsScheaduleActivity.class));
        });

        // See All Tasks
        seeAllTaskTV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TasksActivity.class));
        });

        // Fun Card
        funCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TicTacToeActivity.class));
        });

        // Profile Image
        ivProfile.setOnClickListener(v -> showProfilePopupMenu(v));

        // Add Prescription Button
        addPrescriptions.setOnClickListener(v -> {
            showAddPrescriptionDialog();
        });

        // Add Food Button
        addFood.setOnClickListener(v -> {
            showAddFoodDialog();
        });

        // Health Tips Card - Refresh on click
        cardHealthTips.setOnClickListener(v -> fetchHealthTip());
    }

    private String getTimeBasedGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }

    private void setUserGreeting() {
        TextView tvGreeting = findViewById(R.id.tvGreeting);

        String userName = "Shahriar";

        String greeting = getTimeBasedGreeting() + ", " + userName + "!";
        tvGreeting.setText(greeting);
    }

    private void fetchHealthTip() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.adviceslip.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HealthTipApi api = retrofit.create(HealthTipApi.class);

        api.getRandomHealthTip().enqueue(new Callback<HealthTipResponse>() {
            @Override
            public void onResponse(Call<HealthTipResponse> call, Response<HealthTipResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String advice = response.body().getSlip().getAdvice();
                    tvTipTitle.setText("Health Tip");
                    tvTipContent.setText(advice);
                } else {
                    showDefaultTip();
                }
            }

            @Override
            public void onFailure(Call<HealthTipResponse> call, Throwable t) {
                showDefaultTip();
            }
        });
    }

    private void showDefaultTip() {
        String[] defaultTips = {
                "Drink at least 8 glasses of water daily",
                "Get 7-8 hours of sleep each night",
                "Exercise for 30 minutes daily",
                "Eat plenty of fruits and vegetables",
                "Take breaks from sitting every 30 minutes",
                "Practice deep breathing for stress relief",
                "Limit processed food and sugar intake"
        };

        Random random = new Random();
        String tip = defaultTips[random.nextInt(defaultTips.length)];

        tvTipTitle.setText("Health Tip");
        tvTipContent.setText(tip);
    }

    private void loadPrescriptionData() {
        // Get next medication from database
        Prescription nextMed = prescriptionDbHelper.getNextMedication();
        int prescriptionCount = prescriptionDbHelper.getPrescriptionCount();

        // Update UI with the data
        if (nextMed != null) {
            tvNextMed.setText(nextMed.getMedicationType() + " - " + nextMed.getTime());
        } else {
            tvNextMed.setText("No upcoming medications");
        }

        tvMedCount.setText(prescriptionCount + " active prescriptions");
    }

    private void loadNextMeal() {
        Meal nextMeal = mealDbHelper.getNextMeal();

        if (nextMeal != null) {
            tvNextMeal.setText(nextMeal.getMealType() + " - " + nextMeal.getTime());
        } else {
            tvNextMeal.setText("No upcoming meals");
        }
    }

    private void showAddPrescriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_prescriptions, null);
        builder.setView(dialogView);

        // Initialize dialog views
        Spinner spinnerMedicationType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMedicationTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etPrescriptionDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSavePrescription = dialogView.findViewById(R.id.btnSaveMeal);

        // Setup spinner with medication types
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.medication_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicationType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();

        // Time picker click listener
        btnTimePicker.setOnClickListener(v -> {
            showTimePicker(etMedicationTime);
        });

        // Save prescription click listener
        btnSavePrescription.setOnClickListener(v -> {
            savePrescription(
                    spinnerMedicationType,
                    etMedicationTime,
                    etPrescriptionDetails,
                    cbHasAlarm,
                    dialog
            );
        });

        dialog.show();
    }

    private void showTimePicker(EditText timeEditText) {
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
                    timeEditText.setText(formattedTime);
                },
                hour,
                minute,
                false
        );
        timePickerDialog.show();
    }

    private void savePrescription(Spinner spinnerMedicationType, EditText etMedicationTime,
                                  EditText etPrescriptionDetails, CheckBox cbHasAlarm, AlertDialog dialog) {
        String medicationType = spinnerMedicationType.getSelectedItem().toString();
        String medicationTime = etMedicationTime.getText().toString().trim();
        String prescriptionDetails = etPrescriptionDetails.getText().toString().trim();
        boolean hasAlarm = cbHasAlarm.isChecked();

        // Validate inputs
        if (medicationTime.isEmpty()) {
            Toast.makeText(this, "Please select medication time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (prescriptionDetails.isEmpty()) {
            Toast.makeText(this, "Please enter prescription details", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get appropriate icon based on medication type
        int iconId = getMedicationIcon(medicationType);

        // Create and add new prescription to database
        Prescription newPrescription = new Prescription(medicationType, medicationTime, prescriptionDetails, hasAlarm, iconId);
        long id = prescriptionDbHelper.addPrescription(newPrescription);

        if (id != -1) {
            Toast.makeText(this, "Prescription added", Toast.LENGTH_SHORT).show();
            loadPrescriptionData(); // Refresh the display
            dialog.dismiss();
        } else {
            Toast.makeText(this, "Failed to add prescription", Toast.LENGTH_SHORT).show();
        }
    }

    private int getMedicationIcon(String medicationType) {
        switch (medicationType.toLowerCase()) {
            case "antibiotic": return R.drawable.antibiotic_icon;
            case "painkiller": return R.drawable.antibiotic_icon;
            case "vitamins": return R.drawable.antibiotic_icon;
            default: return R.drawable.antibiotic_icon;
        }
    }

    private void showAddFoodDialog() {
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
            showTimePicker(etMealTime);
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
            long id = mealDbHelper.addMeal(newMeal);

            if (id != -1) {
                Toast.makeText(this, "Meal added", Toast.LENGTH_SHORT).show();
                loadNextMeal(); // Refresh the next meal display
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add meal", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
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

    private void loadLatestBMI() {
        String latestBMI = bmiDbHelper.getLatestBMIValue();
        tvBmiValue.setText(latestBMI);
    }

    private void loadLatestBP() {
        String latestBP = bpDbHelper.getLatestBPRecord();
        tvWaterValue.setText(latestBP);
    }

    private void showBPInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_bp_input, null);
        builder.setView(dialogView);

        EditText etSystolic = dialogView.findViewById(R.id.etSystolic);
        EditText etDiastolic = dialogView.findViewById(R.id.etDiastolic);
        Button btnSave = dialogView.findViewById(R.id.btnSaveBP);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            try {
                int systolic = Integer.parseInt(etSystolic.getText().toString());
                int diastolic = Integer.parseInt(etDiastolic.getText().toString());

                if (systolic <= 0 || diastolic <= 0) {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (bpDbHelper.saveBPRecord(systolic, diastolic)) {
                    loadLatestBP();
                    Toast.makeText(this, "Blood pressure saved", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter numeric values", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showProfilePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layout, popupMenu.getMenu());

        // Force show icons if needed
        try {
            java.lang.reflect.Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    java.lang.reflect.Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logoutid) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void setCurrentDate() {
        if (tvDate == null) return;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
            tvDate.setText(dateFormat.format(new Date()));
        } catch (Exception e) {
            tvDate.setText("Date unavailable");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh values when returning to the activity
        loadLatestBMI();
        loadLatestBP();
        loadPrescriptionData();
        loadNextMeal();
        fetchHealthTip();
    }

    @Override
    protected void onDestroy() {
        bmiDbHelper.close();
        bpDbHelper.close();
        prescriptionDbHelper.close();
        mealDbHelper.close();
        super.onDestroy();
    }
}