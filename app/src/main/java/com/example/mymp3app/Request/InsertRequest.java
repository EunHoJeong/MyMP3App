package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertRequest extends StringRequest {
    private Map<String, String> map;
    private String id;
    private String title;

    public InsertRequest(String id, String title, String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        this.id = id;
        this.title = title;

        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        map.put("title", title);
        return map;
    }
}
