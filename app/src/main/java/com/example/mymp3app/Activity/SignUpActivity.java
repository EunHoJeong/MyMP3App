package com.example.mymp3app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mymp3app.R;
import com.example.mymp3app.Request.IdCheckRequest;
import com.example.mymp3app.Request.SignUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtSUID, edtSUPassword, edtSUPWCheck, edtSUEmailID, edtSUEmail, edtSUPhone;
    private Button btnOverlapCheckID, btnJoin;
    private TextView tvCheckID, tvCheckPassword, tvCheckPasswordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        findViewByIdFunc();


        eventHandlerFunc();


    }

    private void eventHandlerFunc() {


        btnOverlapCheckID.setOnClickListener(v -> {

            String id = edtSUID.getText().toString();

            boolean flag = checkID(id);

            if (flag){
                Toast.makeText(this, "아이디를 조건에맞게 입력해주세요.", Toast.LENGTH_SHORT).show();
            }else{

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject((response));
                            boolean success = jsonObject.getBoolean("success");

                            if(!success){
                                tvCheckID.setText("사용 가능한 아이디입니다.");
                                tvCheckID.setTextColor(0xFF87CEEB);

                            }else{
                                tvCheckID.setText("이미 존재하는 아이디입니다.");
                                tvCheckID.setTextColor(Color.RED);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };//end of Response

                IdCheckRequest idCheckRequest = new IdCheckRequest(id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                queue.add(idCheckRequest);


            }


        });//end of setOn

        edtSUID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvCheckID.setText("영문 소문자와 숫자만 사용하여 5~12자의 아이디를 입력해주세요.");
                tvCheckID.setTextColor(Color.RED);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSUPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = edtSUPassword.getText().toString();

                boolean flag = checkPassword(password);

                if(flag){
                    tvCheckPassword.setText("사용 가능한 비밀번호입니다.");
                    tvCheckPassword.setTextColor(0xFF87CEEB);
                }else{
                    tvCheckPassword.setText("영문 소문자와 특수문자 1가지 이상을 조합하여 6~20자로 입력해주세요. (공백제외)");
                    tvCheckPassword.setTextColor(Color.RED);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSUPWCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtSUPassword.getText().toString().equals(edtSUPWCheck.getText().toString())){
                    tvCheckPasswordCheck.setText("비밀번호가 일치합니다.");
                    tvCheckPasswordCheck.setTextColor(0xFF87CEEB);
                }else{
                    tvCheckPasswordCheck.setText("비밀번호가 일치하지않습니다.");
                    tvCheckPasswordCheck.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnJoin.setOnClickListener(v -> {
                String id = edtSUID.getText().toString();
                String password = edtSUPassword.getText().toString();
                String email = edtSUEmailID.getText().toString() + "@" + edtSUEmail.getText().toString();
                String phone = edtSUPhone.getText().toString();

                Response.Listener<String> responseListener = insertListener();

                SignUpRequest registerRequest = new SignUpRequest(id,password,email,phone, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignUpActivity.this);
                queue.add(registerRequest);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

        });//end of btnJoin

    }//end of event

    private Response.Listener<String> insertListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject((response));
                    boolean success = jsonObject.getBoolean("success");

                    if(success){
                        Toast.makeText(getApplicationContext(),"회원등록성공",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };//end of Response
    }

    private boolean checkPassword(String password) {
        boolean flag = false;
        boolean flagPassword = false;

        if(password.length() >=5 && password.length() <=12) {
            for(int i = 0; i < password.length(); i++) {
                if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                    continue;
                }else if(password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
                    continue;
                }else if(password.charAt(i) >= '0' && password.charAt(i) <= '9'){
                    continue;
                }else if(password.charAt(i) == ' '){
                    flagPassword = true;
                    break;
                }else{
                    flag = true;
                }
            }//end of for
        }

        if(flag) {
            if(flagPassword) {
                System.out.println("비밀번호 사용 불가능합니다.");
                flag = false;
            }else {
                System.out.println("비밀번호 사용 가능합니다.");

            }

        }

        return flag;
    }


    private boolean checkID(String id) {
        boolean flag = false;

        if (id.length() >= 5 && id.length() <= 12) {

            for (int i = 0; i < id.length(); i++) {

                if (id.charAt(i) >= 'a' && id.charAt(i) <= 'z') {
                    continue;
                } else if (id.charAt(i) >= '0' && id.charAt(i) <= '9') {
                    continue;
                } else {
                    flag = true;
                    break;
                }//end of if

            }//end of for
        }else{
            flag = true;
        }//end of if

        return flag;
    }

    private void findViewByIdFunc() {
        edtSUID = findViewById(R.id.edtSUID);
        edtSUPassword = findViewById(R.id.edtSUPassword);
        edtSUPWCheck = findViewById(R.id.edtSUPWCheck);
        edtSUEmailID = findViewById(R.id.edtSUEmailID);
        edtSUEmail = findViewById(R.id.edtSUEmail);
        edtSUPhone = findViewById(R.id.edtSUPhone);

        btnOverlapCheckID = findViewById(R.id.btnOverlapCheckID);
        btnJoin = findViewById(R.id.btnJoin);

        tvCheckID = findViewById(R.id.tvCheckID);
        tvCheckPassword = findViewById(R.id.tvCheckPassword);
        tvCheckPasswordCheck = findViewById(R.id.tvCheckPasswordCheck);
    }
}