package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    FirebaseAuth mAuth;
    CardView cardTextSummary, cardFlashcards, cardMathSolver, cardCodeGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();

        cardTextSummary = findViewById(R.id.cardTextSummary);
        cardFlashcards = findViewById(R.id.cardFlashcards);
        cardMathSolver = findViewById(R.id.cardMathSolver);
        cardCodeGenerator = findViewById(R.id.cardCodeGenerator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        // Set onClick listeners
        cardTextSummary.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, BMICalculateActivity.class);
            startActivity(intent);
        });

        cardFlashcards.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, BMICalculateActivity.class);
            startActivity(intent);
        });

        cardMathSolver.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, BMICalculateActivity.class);
            startActivity(intent);
        });

        cardCodeGenerator.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, BMICalculateActivity.class);
            startActivity(intent);
        });    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.logoutid){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}