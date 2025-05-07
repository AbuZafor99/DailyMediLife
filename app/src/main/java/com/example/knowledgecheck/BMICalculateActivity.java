package com.example.knowledgecheck;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BMICalculateActivity extends AppCompatActivity {
    private EditText etHeight, etWeight;
    private TextView tvBmiValue, tvBmiCategory;
    private Button btnEdit, btnCalculate;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmicalculate);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        tvBmiValue = findViewById(R.id.tvBmiValue);
        tvBmiCategory = findViewById(R.id.tvBmiCategory);
        btnEdit = findViewById(R.id.btnEdit);
        btnCalculate = findViewById(R.id.btnSave);

        // Initialize in non-editing mode
        setEditMode(false);

        btnEdit.setOnClickListener(v -> toggleEditMode());
        btnCalculate.setOnClickListener(v -> {
            if (calculateAndDisplayBMI()) {
                // Only disable editing if calculation was successful
                setEditMode(false);
            }
        });
    }

    private void toggleEditMode() {
        setEditMode(!isEditing);
    }

    private void setEditMode(boolean editing) {
        isEditing = editing;

        etHeight.setEnabled(isEditing);
        etWeight.setEnabled(isEditing);
        btnCalculate.setEnabled(isEditing);
        btnCalculate.setBackgroundTintList(ColorStateList.valueOf(
                isEditing ? getResources().getColor(R.color.darkBlue) :
                        getResources().getColor(R.color.gray)));

        btnEdit.setText(isEditing ? "Cancel" : "Edit");

        // Clear focus when exiting edit mode
        if (!isEditing) {
            etHeight.clearFocus();
            etWeight.clearFocus();
        }
    }

    private boolean calculateAndDisplayBMI() {
        try {
            // Get input values
            float height = Float.parseFloat(etHeight.getText().toString());
            float weight = Float.parseFloat(etWeight.getText().toString());

            // Validate input
            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Calculate BMI
            float bmi = calculateBMI(height, weight);

            // Display results
            displayBMIResults(bmi);
            return true;

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter numeric values", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private float calculateBMI(float heightCm, float weightKg) {
        // Convert height from cm to meters
        float heightM = heightCm / 100;
        // Calculate BMI: weight(kg) / (height(m)^2)
        return weightKg / (heightM * heightM);
    }

    private void displayBMIResults(float bmi) {
        // Round to 1 decimal place
        float roundedBMI = Math.round(bmi * 10) / 10f;
        tvBmiValue.setText(String.valueOf(roundedBMI));

        // Determine category
        String category;
        int colorRes;

        if (bmi < 18.5) {
            category = "Underweight";
            colorRes = R.color.bmi_underweight;
        } else if (bmi < 25) {
            category = "Normal";
            colorRes = R.color.bmi_normal;
        } else if (bmi < 30) {
            category = "Overweight";
            colorRes = R.color.bmi_overweight;
        } else {
            category = "Obese";
            colorRes = R.color.bmi_obese;
        }

        // Update UI
        tvBmiCategory.setText(category);
        tvBmiCategory.setBackgroundResource(colorRes);

        // Show result message
        String message = "Your BMI: " + roundedBMI + " (" + category + ")";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
