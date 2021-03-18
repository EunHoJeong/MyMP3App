package com.example.mymp3app;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdCheckRequest extends StringRequest {
    private final static String URL ="http://gh888.dothome.co.kr/MusicIDCheck.php";
    private String id;
    private Map<String, String> map;

    public IdCheckRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        this.id = id;
        map = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        return map;
    }
}
