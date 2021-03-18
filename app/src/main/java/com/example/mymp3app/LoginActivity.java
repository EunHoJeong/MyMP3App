package com.example.mymp3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        Button btnLogin = findViewById(R.id.btnLogin);

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermsOfServiceActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

    }
}