package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertPositionRequest extends StringRequest {
    private Map<String, String> map;
    private String id;
    private int position;

    public InsertPositionRequest(String id, int position, String url) {
        super(Method.POST, url, null, null);
        this.id = id;
        this.position = position;

        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        map.put("position", String.valueOf(position));
        return map;
    }
}
