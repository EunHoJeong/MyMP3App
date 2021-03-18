package com.example.mymp3app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView imgMusicIcon;
    private TextView tvSinger, tvMusicTitle, tvHome, tvMyMusic, tvSearch;
    private ImageButton imgbtnPrev, imgbtnPlay, imgbtnNext, imgbtnList, imgbtnHome, imgbtnMyMusic, imgbtnSearch;

    private ArrayList<MusicData> sdCardList = new ArrayList<MusicData>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("홈");

        findViewByIdFunc();

        eventHandlerFunc();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f_Home = new FragmentHome();
        ft.replace(R.id.frameLayout, f_Home);
        ft.commit();

    }

    public ArrayList<MusicData> getData(){
        return sdCardList;
    }


    private void eventHandlerFunc() {
        tvHome.setTextColor(0xFF87CEEB);

        imgbtnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicActivity.class);
            startActivity(intent);
        });



        imgbtnHome.setOnClickListener(v -> {
            tvHome.setTextColor(0xFF87CEEB);
            tvMyMusic.setTextColor(Color.BLACK);
            tvSearch.setTextColor(Color.BLACK);
            setTitle("홈");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f_Home = new FragmentHome();
            ft.replace(R.id.frameLayout, f_Home);
            ft.commit();

        });

        imgbtnMyMusic.setOnClickListener(v -> {
            tvHome.setTextColor(Color.BLACK);
            tvMyMusic.setTextColor(0xFF87CEEB);
            tvSearch.setTextColor(Color.BLACK);
            setTitle("내음악");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f_MyMusic = new FragmentMyMusic();
            ft.replace(R.id.frameLayout, f_MyMusic);
            ft.commit();

        });

        imgbtnSearch.setOnClickListener(v -> {
            tvHome.setTextColor(Color.BLACK);
            tvMyMusic.setTextColor(Color.BLACK);
            tvSearch.setTextColor(0xFF87CEEB);
            setTitle("검색");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f_Search = new FragmentSearch();
            ft.replace(R.id.frameLayout, f_Search);
            ft.commit();
        });
    }

    private void findViewByIdFunc() {
        imgMusicIcon = findViewById(R.id.imgMusicIcon);

        tvSinger = findViewById(R.id.tvSinger);
        tvMusicTitle = findViewById(R.id.tvMusicTitle);
        tvHome = findViewById(R.id.tvHome);
        tvMyMusic = findViewById(R.id.tvMyMusic);
        tvSearch = findViewById(R.id.tvSearch);

        imgbtnPrev = findViewById(R.id.imgbtnPrev);
        imgbtnPlay = findViewById(R.id.imgbtnPlay);
        imgbtnNext = findViewById(R.id.imgbtnNext);
        imgbtnList = findViewById(R.id.imgbtnList);

        imgbtnHome = findViewById(R.id.imgbtnHome);
        imgbtnMyMusic = findViewById(R.id.imgbtnMyMusic);
        imgbtnSearch = findViewById(R.id.imgbtnSearch);
    }
}