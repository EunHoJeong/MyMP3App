package com.example.mymp3app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.R;
import com.example.mymp3app.Request.IdCheckRequest;
import com.example.mymp3app.Request.MusicDataRequest;
import com.example.mymp3app.Request.PositionRequest;
import com.example.mymp3app.Request.SelectPlayRequest;
import com.example.mymp3app.Request.SelectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private ArrayList<MusicData> myMusicList = new ArrayList<MusicData>();
    private ArrayList<MusicData> myPlayList = new ArrayList<MusicData>();
    private ArrayList<MusicData> topList = new ArrayList<MusicData>();
    private ArrayList<String> goodList = new ArrayList<String>();
    private ArrayList<String> playList = new ArrayList<String>();

    private Button btnLogin;
    private EditText edtID, edtPassword;
    private TextView tvSignUp;

    private boolean flag = false;
    private boolean check = false;
    private String id;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getMusicData();

        findViewByIdFunc();

        eventHandlerFunc();




    }

    private void eventHandlerFunc() {

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!check){
                    getGoodMusic(edtID.getText().toString());
                    getPlayList(edtID.getText().toString());
                    getPosition(edtID.getText().toString());
                    check = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 5){
                    checkIdPassword();
                }
            }
        });


        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {


            if(flag){

                dataClassification();


                Intent intent = new Intent(this, MainActivity.class);
                intent.putParcelableArrayListExtra("musicList", musicList);
                intent.putParcelableArrayListExtra("myMusicList", myMusicList);
                intent.putParcelableArrayListExtra("myPlayList", myPlayList);
                intent.putExtra("position", position);
                intent.putExtra("id", edtID.getText().toString());
                startActivity(intent);
            }else{
                Toast.makeText(this, "아이디 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                check = false;
            }

        });
    }

    public void dataClassification(){
        for(int i = 0; i < goodList.size(); i++){
            for(int j = 0; j < musicList.size(); j++){
                if(goodList.get(i).equals(musicList.get(j).getTitle())){
                    myMusicList.add(musicList.get(j));
                    break;
                }
            }
        }

        for(int i = 0; i < playList.size(); i++){
            for(int j = 0; j < musicList.size(); j++){
                if(playList.get(i).equals(musicList.get(j).getTitle())){
                    myPlayList.add(musicList.get(j));
                    break;
                }
            }
        }




    }

    public void checkIdPassword() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    flag = jo.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IdCheckRequest idCheckRequest = new IdCheckRequest(edtID.getText().toString(), listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(idCheckRequest);

    }

    public void findViewByIdFunc() {
        btnLogin = findViewById(R.id.btnLogin);

        edtID = findViewById(R.id.edtID);
        edtPassword = findViewById(R.id.edtPassword);

        tvSignUp = findViewById(R.id.tvSignUp);
    }

    public void getPosition(String id){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);

                    position = jo.getInt("position");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        PositionRequest positionRequest = new PositionRequest(id, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(positionRequest);
    }

    public void getMusicData() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String play = jo.getString("play");
                        String artist = jo.getString("artist");
                        String title = jo.getString("title");
                        String albumArt = jo.getString("albumArt");
                        int duration = jo.getInt("duration");
                        String genre = jo.getString("genre");
                        int count = jo.getInt("count");
                        musicList.add(new MusicData(play, artist, title, albumArt, duration, genre, count));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MusicDataRequest musicDataRequest = new MusicDataRequest(listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(musicDataRequest);

    }

    public void getPlayList(String id){
        Response.Listener<String> goodListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String title = jo.getString("title");
                        goodList.add(title);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        String url = "http://gh888.dothome.co.kr/SelectGood.php";
        SelectRequest select = new SelectRequest(id, url, goodListener);
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(select);
    }

    public void getGoodMusic(String id){
        Response.Listener<String> playListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String title = jo.getString("title");
                        playList.add(title);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        String playListUrl = "http://gh888.dothome.co.kr/SelectPlayList.php";
        SelectPlayRequest plsySelect = new SelectPlayRequest(id, playListUrl, playListener);
        RequestQueue rqPlay = Volley.newRequestQueue(getApplicationContext());
        rqPlay.add(plsySelect);
    }
}