package com.example.knowledgecheck;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrescriptionsScheaduleActivity extends AppCompatActivity {
    private RecyclerView prescriptionsRecyclerView;
    private Button btnAddPrescription;
    private List<Prescription> prescriptionList = new ArrayList<>();
    private PrescriptionAdapter adapter;
    private PrescriptionDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions_scheadule);

        // Initialize database helper
        dbHelper = new PrescriptionDatabaseHelper(this);

        // Initialize views
        prescriptionsRecyclerView = findViewById(R.id.mealRV);
        btnAddPrescription = findViewById(R.id.addMealBTN);

        // Setup RecyclerView
        prescriptionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrescriptionAdapter(prescriptionList);
        prescriptionsRecyclerView.setAdapter(adapter);

        // Load prescriptions from database
        loadPrescriptionsFromDatabase();

        // Set click listener for add button
        btnAddPrescription.setOnClickListener(v -> showAddPrescriptionDialog());
    }

    private void loadPrescriptionsFromDatabase() {
        prescriptionList.clear();
        prescriptionList.addAll(dbHelper.getAllPrescriptions());
        adapter.notifyDataSetChanged();
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
            long id = dbHelper.addPrescription(newPrescription);

            if (id != -1) {
                // Set the ID returned from database
                newPrescription.setId(id);

                // Add to the list and refresh
                prescriptionList.add(newPrescription);
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Prescription added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add prescription", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private int getMedicationIcon(String medicationType) {
        switch (medicationType.toLowerCase()) {
            case "antibiotic":
                return R.drawable.antibiotic_icon;
            case "painkiller":
                return R.drawable.antibiotic_icon;
            case "vitamins":
                return R.drawable.antibiotic_icon;
            default:
                return R.drawable.antibiotic_icon;
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    // Prescription Adapter class
    private class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder> {
        private List<Prescription> prescriptions;

        public PrescriptionAdapter(List<Prescription> prescriptions) {
            this.prescriptions = prescriptions;
        }

        @Override
        public PrescriptionViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.prescription_list, parent, false);
            return new PrescriptionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PrescriptionViewHolder holder, int position) {
            Prescription prescription = prescriptions.get(position);
            holder.ivMedicationIcon.setImageResource(prescription.getIconRes());
            holder.tvMedicationType.setText(prescription.getMedicationType());
            holder.tvMedicationDetails.setText(prescription.getTime() + " - " + prescription.getDetails());
            holder.btnAlarm.setImageResource(prescription.hasAlarm() ?
                    R.drawable.alarm_on_icon : R.drawable.alarm_off_icon);
        }

        @Override
        public int getItemCount() {
            return prescriptions.size();
        }

        class PrescriptionViewHolder extends RecyclerView.ViewHolder {
            ImageView ivMedicationIcon;
            TextView tvMedicationType;
            TextView tvMedicationDetails;
            ImageButton btnAlarm;

            public PrescriptionViewHolder(View itemView) {
                super(itemView);
                ivMedicationIcon = itemView.findViewById(R.id.ivMealIcon);
                tvMedicationType = itemView.findViewById(R.id.tvMealType);
                tvMedicationDetails = itemView.findViewById(R.id.tvMealDetails);
                btnAlarm = itemView.findViewById(R.id.btnAlarm);
            }
        }
    }
}