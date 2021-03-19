package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {
    private final static String URL ="http://gh888.dothome.co.kr/MusicSignUp.php";
    private String id;
    private String password;
    private String email;
    private String phone;
    private Map<String, String> map;


    public SignUpRequest(String id, String password, String email, String phone, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        map = new HashMap<>();
        this.id = id;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        map.put("password", password);
        map.put("email", email);
        map.put("phone", phone);
        return map;
    }
}
