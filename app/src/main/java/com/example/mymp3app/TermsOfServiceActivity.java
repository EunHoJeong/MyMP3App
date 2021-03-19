package com.example.mymp3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.mymp3app.Activity.SignUpActivity;

public class TermsOfServiceActivity extends AppCompatActivity {
    private CheckBox cbServiceAgree, cbPrivacyAgree;
    Button btnNextSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_ofservice);

        CheckBox cbServiceAgree = findViewById(R.id.cbServiceAgree);
        CheckBox cbPrivacyAgree = findViewById(R.id.cbPrivacyAgree);
        Button btnNextSignUp = findViewById(R.id.btnNextSignUp);

        btnNextSignUp.setOnClickListener(v -> {
            if(cbServiceAgree.isChecked() && cbPrivacyAgree.isChecked()){
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }



}