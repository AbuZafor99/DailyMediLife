package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Home extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CardView bmiCV, funCV;
    private TextView tvDate, seeAllFoodTV, seeAllTaskTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

<<<<<<< Updated upstream
        // Handle edge-to-edge insets
=======
        bmiDbHelper = new BMIDatabaseHelper(this);
        bpDbHelper = new BloodPressureDatabaseHelper(this);
        prescriptionDbHelper = new PrescriptionDatabaseHelper(this);
        mealDbHelper = new MealDatabaseHelper(this);

>>>>>>> Stashed changes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
        setCurrentDate();
        setUserGreeting();

<<<<<<< Updated upstream
        bmiCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, BMICalculateActivity.class));

        });

        seeAllFoodTV.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, FoodScheduleActivity.class);
            startActivity(intent);
        });

        seeAllTaskTV.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, TasksActivity.class);
            startActivity(intent);
        });

        funCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TicTacToeActivity.class));
        });

=======
        loadLatestBMI();
        loadLatestBP();
        loadPrescriptionData();
        loadNextMeal();

        setupClickListeners();
>>>>>>> Stashed changes
    }

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        bmiCV = findViewById(R.id.cardBmi);
        seeAllFoodTV = findViewById(R.id.seeAllFoodTV);
        seeAllTaskTV = findViewById(R.id.seeAllTaskTV);
        funCV = findViewById(R.id.funCV);
<<<<<<< Updated upstream
=======
        ivProfile = findViewById(R.id.ivProfile);
        seeAllPrescriptions = findViewById(R.id.tvSeeAllPrescriptions);
        addFood = findViewById(R.id.btnAddFood);
        addPrescriptions = findViewById(R.id.btnAddPrescription);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvWaterValue = findViewById(R.id.tvWaterValue);
        tvNextMed = findViewById(R.id.tvNextMed);
        tvMedCount = findViewById(R.id.tvMedCount);
        tvNextMeal = findViewById(R.id.tvNextMeal);
    }

    private void setupClickListeners() {

        bmiCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, BMICalculateActivity.class));
        });


        waterIntakeCV.setOnClickListener(v -> {
            showBPInputDialog();
        });


        seeAllFoodTV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, FoodScheduleActivity.class));
        });


        seeAllPrescriptions.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, PrescriptionsScheaduleActivity.class));
        });


        seeAllTaskTV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TasksActivity.class));
        });


        funCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TicTacToeActivity.class));
        });


        ivProfile.setOnClickListener(v -> showProfilePopupMenu(v));


        addPrescriptions.setOnClickListener(v -> {
            showAddPrescriptionDialog();
        });


        addFood.setOnClickListener(v -> {
            showAddFoodDialog();
        });
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

    private void loadPrescriptionData() {
        Prescription nextMed = prescriptionDbHelper.getNextMedication();
        int prescriptionCount = prescriptionDbHelper.getPrescriptionCount();


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


        Spinner spinnerMedicationType = dialogView.findViewById(R.id.spinnerMealType);
        EditText etMedicationTime = dialogView.findViewById(R.id.etMealTime);
        Button btnTimePicker = dialogView.findViewById(R.id.btnTimePicker);
        EditText etPrescriptionDetails = dialogView.findViewById(R.id.etMealDetails);
        CheckBox cbHasAlarm = dialogView.findViewById(R.id.cbHasAlarm);
        Button btnSavePrescription = dialogView.findViewById(R.id.btnSaveMeal);


        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.medication_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicationType.setAdapter(spinnerAdapter);

        AlertDialog dialog = builder.create();


        btnTimePicker.setOnClickListener(v -> {
            showTimePicker(etMedicationTime);
        });


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


        if (medicationTime.isEmpty()) {
            Toast.makeText(this, "Please select medication time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (prescriptionDetails.isEmpty()) {
            Toast.makeText(this, "Please enter prescription details", Toast.LENGTH_SHORT).show();
            return;
        }

        int iconId = getMedicationIcon(medicationType);

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
            showTimePicker(etMealTime);
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


            int iconId = getMealIcon(mealType);


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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true; // Return true to show the menu
=======
    protected void onResume() {
        super.onResume();
        loadLatestBMI();
        loadLatestBP();
        loadPrescriptionData();
        loadNextMeal();
>>>>>>> Stashed changes
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutid) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}