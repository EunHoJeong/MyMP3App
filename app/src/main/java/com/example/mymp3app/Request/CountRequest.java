package com.example.mymp3app.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CountRequest extends StringRequest {
    private final static String URL ="http://gh888.dothome.co.kr/MusicCount.php";
    private String title;
    private Map<String, String> map;

    public CountRequest(String title) {
        super(Method.POST, URL, null, null);
        this.title = title;
        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("title", title);
        return map;
    }
}
