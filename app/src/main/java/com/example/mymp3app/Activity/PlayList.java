package com.example.mymp3app.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.MusicAdapter;
import com.example.mymp3app.R;
import com.example.mymp3app.Request.SelectPlayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayList extends AppCompatActivity {
    public static final int PLAY_LIST = 3;
    private static RecyclerView recyclerPlayList;
    private static MusicAdapter adapter;
    private static ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private static ArrayList<MusicData> myPlayList = new ArrayList<MusicData>();
    private ArrayList<String> playList = new ArrayList<String>();
    private static SwipeRefreshLayout swiper;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        musicList = intent.getParcelableArrayListExtra("musicList");
        myPlayList = intent.getParcelableArrayListExtra("myPlayList");

        swiper = findViewById(R.id.swiper);

        recyclerPlayList = findViewById(R.id.recyclerPlayList);

        adapter = new MusicAdapter(getApplicationContext(), myPlayList, PLAY_LIST);
        recyclerPlayList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerPlayList.setAdapter(adapter);





    }

    public static void deleteList(int position){
        myPlayList.remove(position);
    }

    public static void refresh(){
        //adapter.notifyDataSetChanged();
    }





}