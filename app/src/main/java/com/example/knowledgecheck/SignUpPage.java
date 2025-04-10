package com.example.knowledgecheck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpPage extends AppCompatActivity {
    private EditText  email ,pass ;
    private Button signup;
    private TextView login;

    private FirebaseAuth mAuth;
    private ProgressBar myprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);

        //------------initialization---------------
//        name=findViewById(R.id.nameET);
        email=findViewById(R.id.emailET);
        pass=findViewById(R.id.passET);
//        confirmPass=findViewById(R.id.comfirmPassET);
        signup=findViewById(R.id.signupBT);
        login=findViewById(R.id.gologin);
        mAuth=FirebaseAuth.getInstance();
        myprogress=findViewById(R.id.progress);

        //-------------sign up button click handle----------
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String user_name= name.getText().toString();
//                String user_email= email.getText().toString();
//                String user_pass= pass.getText().toString();
//                String user_cpass= confirmPass.getText().toString();
//
//                String user_info=user_name+" , "+user_email+" , "+user_pass+" , "+user_cpass;
//                Toast.makeText(SignUpPage.this,""+user_info,Toast.LENGTH_LONG).show();
                userRegister();
            }
        });

        //--------------have account click handle-----------
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpPage.this, MainActivity.class));
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void userRegister() {
//        String user_name= name.getText().toString().trim();
        String user_email= email.getText().toString().trim();
        String user_pass= pass.getText().toString().trim();
        myprogress.setVisibility(View.VISIBLE);
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
        mAuth.createUserWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                myprogress.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(SignUpPage.this, Home.class));
                } else {
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User already exist..",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error: ."+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}