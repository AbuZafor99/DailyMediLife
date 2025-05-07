package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private CardView bmiCV;
    private TextView tvDate, seeAllFoodTV, seeAllTaskTV;


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

        seeAllTaskTV.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, TasksActivity.class);
            startActivity(intent);
        });

    }

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        bmiCV = findViewById(R.id.cardBmi);
        seeAllFoodTV = findViewById(R.id.seeAllFoodTV);
        seeAllTaskTV = findViewById(R.id.seeAllTaskTV);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true; // Return true to show the menu
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