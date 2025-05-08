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

public class Home extends AppCompatActivity {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CardView bmiCV, funCV;
    private TextView tvDate, seeAllFoodTV, seeAllTaskTV, seeAllPrescriptions;
    private Button addPrescriptions, addFood;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setCurrentDate();

        bmiCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, BMICalculateActivity.class));
        });

        seeAllFoodTV.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, FoodScheduleActivity.class);
            startActivity(intent);
        });

        seeAllPrescriptions.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, PrescriptionsScheaduleActivity.class);
            startActivity(intent);
        });

        //-----------2 btn-----------
        addPrescriptions.setOnClickListener(v -> {
            showAddPrescriptionDialog();
        });

        addFood.setOnClickListener(v -> {
            showAddFoodDialog();
        });
        //---------------------------

        seeAllTaskTV.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, TasksActivity.class);
            startActivity(intent);
        });

        funCV.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, TicTacToeActivity.class));
        });

        // Set click listener for profile image
        ivProfile.setOnClickListener(v -> showProfilePopupMenu(v));
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
                        etMedicationTime.setText(formattedTime);
                    },
                    hour,
                    minute,
                    false
            );
            timePickerDialog.show();
        });

        btnSavePrescription.setOnClickListener(v -> {
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

            // Here you would save the prescription to your database or list
            Toast.makeText(this, "Prescription added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
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

            // Here you would save the meal to your database or list
            Toast.makeText(this, "Meal added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showProfilePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_layout, popupMenu.getMenu());

        // Force show icons if needed (optional)
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

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        bmiCV = findViewById(R.id.cardBmi);
        seeAllFoodTV = findViewById(R.id.seeAllFoodTV);
        seeAllTaskTV = findViewById(R.id.seeAllTaskTV);
        funCV = findViewById(R.id.funCV);
        ivProfile = findViewById(R.id.ivProfile);
        seeAllPrescriptions = findViewById(R.id.tvSeeAllPrescriptions);
        addFood = findViewById(R.id.btnAddFood);
        addPrescriptions = findViewById(R.id.btnAddPrescription);
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
}