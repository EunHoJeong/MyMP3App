package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {
    private Map<String, String> map;
    private String title;

    public SearchRequest(String title, String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        this.title = title;

        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("title", title);
        return map;
    }
}
