package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
    private TextView tvDate, seeAllFoodTV, seeAllTaskTV,seeAllPrescriptions;
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
        seeAllPrescriptions=findViewById(R.id.tvSeeAllPrescriptions);
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