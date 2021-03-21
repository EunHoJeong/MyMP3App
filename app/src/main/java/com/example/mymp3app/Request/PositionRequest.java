package com.example.mymp3app.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PositionRequest extends StringRequest {
    private final static String URL ="http://gh888.dothome.co.kr/MemberPosition.php";
    private Map<String, String> map;
    private String id;

    public PositionRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        this.id = id;

        map = new HashMap<String, String>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        map.put("id", id);
        return map;
    }
}
