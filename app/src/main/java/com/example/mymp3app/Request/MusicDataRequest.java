package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MusicDataRequest extends StringRequest {
    public static final String URL = "http://gh888.dothome.co.kr/MusicData.php";
    private Map<String, String> map;


    public MusicDataRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
