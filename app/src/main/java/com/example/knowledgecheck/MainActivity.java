package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email,pass;
    private Button loginButton;
    private TextView gosignup,forgotPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.emailET);
        pass=findViewById(R.id.passET);
        loginButton=findViewById(R.id.logintBT);
        forgotPass=findViewById(R.id.forgotPass);
        gosignup=findViewById(R.id.gosignUpTV);
        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String user_email= email.getText().toString();
//                String user_pass= pass.getText().toString();
//                String user_info=user_email+" , "+user_pass;
//                Toast.makeText(MainActivity.this,""+user_info,Toast.LENGTH_LONG).show();
                userLogin();
            }
        });


        gosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpPage.class));
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void userLogin() {
        String user_email= email.getText().toString().trim();
        String user_pass= pass.getText().toString().trim();
//
//        if (user_name.isEmpty()){
//            name.setError("Enter a name.");
//            email.requestFocus();
//            return;
//        }
        if (user_email.isEmpty()){
            email.setError("Enter an email.");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            email.setError("Enter a valid email.");
            email.requestFocus();
            return;
        }
        if (user_pass.isEmpty()){
            pass.setError("Enter a password.");
            pass.requestFocus();
            return;
        }
        if (user_pass.length()<6){
            pass.setError("Minimum length of a password should be 6.");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, Home.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"Login Unsuccessfull.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}