package com.example.classwork7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView gotoRegister;
    EditText email, password;
    Button login;
    //private String Email;

    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.EmailTxt);
        password = findViewById(R.id.PasswordTxt);
        login = findViewById(R.id.LoginBtn);
        gotoRegister = findViewById(R.id.Register);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        gotoRegister.setOnClickListener(v -> {

            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            finish();
        });


        login.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            CheckAllValidation();

        });

    }

    private void CheckAllValidation() {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();


        if (TextUtils.isEmpty(Email)) {
            email.setError("Please Enter Email");

        } else if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError("Please Enter Valid Email");

        } else if (TextUtils.isEmpty(Password)) {
            password.setError("Please Enter Password");
        } else {
            PerformAuthentication(Email, Password);
        }

    }

    private void PerformAuthentication(String email, String password) {
        progressDialog.setMessage("please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Not Authenticated", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}