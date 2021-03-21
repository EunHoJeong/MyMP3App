package com.example.mymp3app.Request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SelectRequest extends StringRequest {
    private Map<String, String> map;
    private String id;

    public SelectRequest(String id, String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        this.id = id;
        map = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        return map;
    }
}
