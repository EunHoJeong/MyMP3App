package com.example.mymp3app.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mymp3app.DownloadFilesTask;
import com.example.mymp3app.Fragment.FragmentHome;
import com.example.mymp3app.Fragment.FragmentMyMusic;
import com.example.mymp3app.Fragment.FragmentSearch;
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.R;
import com.example.mymp3app.Request.CountRequest;
import com.example.mymp3app.Request.DeleteRequest;
import com.example.mymp3app.Request.InsertPositionRequest;
import com.example.mymp3app.Request.InsertRequest;
import com.example.mymp3app.Request.MusicDataRequest;
import com.example.mymp3app.Request.SelectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static ImageButton imgbtnGood, imgbtnPosterPrev, imgbtnPosterPlay, imgbtnPosterNext;
    private static TextView tvPosterTitle, tvPosterSinger, tvTimeIng, tvTimeLast;
    private static SeekBar sbMP3;
    private static ImageView imgPoster;

    private static ImageView imgMusicIcon;
    private static TextView tvSinger, tvMusicTitle, tvHome, tvMyMusic, tvSearch;
    private static ImageButton imgbtnPrev, imgbtnPlay, imgbtnNext, imgbtnList, imgbtnHome, imgbtnMyMusic, imgbtnSearch;
    private static DrawerLayout drawerLayout;
    public  static String id;
    private static ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private static ArrayList<MusicData> myMusicList = new ArrayList<MusicData>();
    private static ArrayList<MusicData> myPlayList = new ArrayList<MusicData>();
    private ArrayList<MusicData> topMusicList = new ArrayList<MusicData>();

    private static MediaPlayer mp = new MediaPlayer();
    private static int playPosition;

    private static long backKeyPressedTime = 0;
    private boolean good = false;
    private static final int PLAY = 0;
    private static final int PASUE = 1;
    private static final int REPLAY = 2;

    private static boolean exit = false;
    private static boolean start = false;
    private static boolean flag = false;

    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        musicList = getIntent().getParcelableArrayListExtra("musicList");
        myMusicList = getIntent().getParcelableArrayListExtra("myMusicList");
        myPlayList = getIntent().getParcelableArrayListExtra("myPlayList");
        topMusicList = getIntent().getParcelableArrayListExtra("myPlayList");
        int position = (getIntent().getIntExtra("position", 0));
        id = getIntent().getStringExtra("id");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicList", musicList);



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();




        출처: https://commin.tistory.com/63 [Commin의 일상코딩]





        setTitle("홈");

        findViewByIdFunc();

        eventHandlerFunc();
        eventHandelerPoster();

        firstSet(position);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f_Home = new FragmentHome();
        f_Home.setArguments(bundle);
        ft.replace(R.id.frameLayout, f_Home);
        ft.commit();


    }


    private void firstSet(int position){
        try{
            new DownloadFilesTask(imgMusicIcon).execute(myPlayList.get(position).getAlbumArt());
            tvMusicTitle.setText(myPlayList.get(position).getTitle());
            tvSinger.setText(myPlayList.get(position).getArtist());
            String music = myPlayList.get(position).getPlay();

            mp.setDataSource(music);
            mp.prepare();
        }catch(NullPointerException e){

            new DownloadFilesTask(imgMusicIcon).execute(musicList.get(position).getAlbumArt());
            tvMusicTitle.setText(musicList.get(position).getTitle());
            tvSinger.setText(musicList.get(position).getArtist());
            String music = musicList.get(position).getPlay();
            try {
                mp.setDataSource(music);
                mp.prepare();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }catch (RuntimeException e){

        }

    }

    private void eventHandlerFunc() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicList", musicList);

        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList("myMusicList", myMusicList);

        tvHome.setTextColor(0xFF87CEEB);


        imgbtnNext.setOnClickListener(v -> {
            imgbtnPlay.setBackgroundResource(R.drawable.pause);
            try {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                if(playPosition == myPlayList.size()-1){
                    playPosition = 0;
                }else {
                    playPosition++;
                }
                musicCount();

                new DownloadFilesTask(imgMusicIcon).execute(myPlayList.get(playPosition).getAlbumArt());
                tvMusicTitle.setText(myPlayList.get(playPosition).getTitle());
                tvSinger.setText(myPlayList.get(playPosition).getArtist());
                String music = myPlayList.get(playPosition).getPlay();

                mp.setDataSource(music);
                mp.prepare();
                mp.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        imgbtnPrev.setOnClickListener(v -> {
            imgbtnPlay.setBackgroundResource(R.drawable.pause);
            try {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                if(playPosition == 0){
                    playPosition = myPlayList.size()-1;
                }else {
                    playPosition--;
                }
                musicCount();

                new DownloadFilesTask(imgMusicIcon).execute(myPlayList.get(playPosition).getAlbumArt());
                tvMusicTitle.setText(myPlayList.get(playPosition).getTitle());
                tvSinger.setText(myPlayList.get(playPosition).getArtist());
                String music = myPlayList.get(playPosition).getPlay();
                mp.setDataSource(music);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        imgbtnPlay.setOnClickListener(v -> {
            if(start){
                mp.pause();
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigplay);
                imgbtnPlay.setBackgroundResource(R.drawable.play_music);
                start = false;
            }else{
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
                imgbtnPlay.setBackgroundResource(R.drawable.pause);
                mp.start();
                start = true;
            }
        });




        imgbtnHome.setOnClickListener(v -> {
            tvHome.setTextColor(0xFF87CEEB);
            tvMyMusic.setTextColor(Color.BLACK);
            tvSearch.setTextColor(Color.BLACK);
            setTitle("홈");

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f_Home = new FragmentHome();
            ft.replace(R.id.frameLayout, f_Home);
            f_Home.setArguments(bundle);
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
            f_MyMusic.setArguments(bundle1);
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
            f_Search.setArguments(bundle);
            ft.commit();
        });


        imgMusicIcon.setOnClickListener(v -> {
            openPoster();
        });

        imgbtnList.setOnClickListener(v -> {

            Intent intent1 = new Intent(this, PlayList.class);
            intent1.putParcelableArrayListExtra("myPlayList", myPlayList);
            intent1.putParcelableArrayListExtra("musicList", musicList);
            startActivity(intent1);
        });
    }

    private void eventHandelerPoster() {
        imgbtnGood.setOnClickListener(v -> {

            if(!good){
                imgbtnGood.setBackgroundResource(R.drawable.good);
                myMusicList.add(myPlayList.get(playPosition));
                insertGoodMusic();
            }else{
                imgbtnGood.setBackgroundResource(R.drawable.not_good);
                for(int i = 0; i < myMusicList.size(); i++){
                    if(myMusicList.get(i).getTitle().equals(myPlayList.get(playPosition).getTitle())){
                        myMusicList.remove(i);
                        break;
                    }
                }
                deleteGoodMusic();
            }

        });

        imgbtnPosterPlay.setOnClickListener(v -> {

            if(start){

                mp.pause();
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigplay);
                imgbtnPlay.setBackgroundResource(R.drawable.play_music);
                start = false;
            }else{
                Thread thread = new Thread(){
                    @Override
                    public void run() {
                        if(mp == null) return;

                        sbMP3.setMax(mp.getDuration());


                        while(mp.isPlaying()){
                            runOnUiThread(()->{
                                sbMP3.setProgress(mp.getCurrentPosition());
                                tvTimeIng.setText(sdf.format(mp.getCurrentPosition()));
                            });
                            SystemClock.sleep(500);
                        }
                        return;
                    }
                };

                imgbtnPlay.setBackgroundResource(R.drawable.pause);
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
                mp.start();
                thread.start();
                start = true;
            }
        });// end of play

        imgbtnPosterNext.setOnClickListener(v -> {
            imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
            try {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                if(playPosition == myPlayList.size()-1){
                    playPosition = 0;
                }else {
                    playPosition++;
                }
                musicCount();
                checkGoodMusic();

                int p = playPosition;

                tvPosterTitle.setText(myPlayList.get(p).getTitle());
                tvPosterSinger.setText(myPlayList.get(p).getArtist());
                tvTimeLast.setText(sdf.format(myPlayList.get(p).getDuration()));
                new DownloadFilesTask(imgPoster).execute(myPlayList.get(p).getAlbumArt());

                String music = myPlayList.get(p).getPlay();
                mp.setDataSource(music);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        imgbtnPosterPrev.setOnClickListener(v -> {

            imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
            try {
                mp.stop();
                mp.release();
                mp = new MediaPlayer();
                if(playPosition == 0){
                    playPosition = myPlayList.size()-1;
                }else {
                    playPosition--;
                }
                musicCount();
                checkGoodMusic();

                int p = playPosition;

                tvPosterTitle.setText(myPlayList.get(p).getTitle());
                tvPosterSinger.setText(myPlayList.get(p).getArtist());
                tvTimeLast.setText(sdf.format(myPlayList.get(p).getDuration()));
                new DownloadFilesTask(imgPoster).execute(myPlayList.get(p).getAlbumArt());

                String music = myPlayList.get(playPosition).getPlay();
                mp.setDataSource(music);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                playPosition = myPlayList.size()-1;
            }
        });

        sbMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean flag) {
                if(flag){
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public ArrayList<MusicData> getData(){
        return musicList;
    }
    //Poster 화면에 띄움

    public void openPoster(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                if(mp == null) return;

                sbMP3.setMax(mp.getDuration());


                while(mp.isPlaying()){
                    runOnUiThread(()->{
                        sbMP3.setProgress(mp.getCurrentPosition());
                        tvTimeIng.setText(sdf.format(mp.getCurrentPosition()));
                    });
                    SystemClock.sleep(500);
                }
                return;
            }

        };
        thread.start();
        checkGoodMusic();
        drawerLayout.setVisibility(View.VISIBLE);
        exit = true;

        new DownloadFilesTask(imgPoster).execute(myPlayList.get(playPosition).getAlbumArt());
        tvPosterTitle.setText(myPlayList.get(playPosition).getTitle());
        tvPosterSinger.setText(myPlayList.get(playPosition).getArtist());
        tvTimeLast.setText(sdf.format(myPlayList.get(playPosition).getDuration()));

    }
    //내가 좋아요했는지 안했는지

    public void checkGoodMusic(){
        good = false;
        for(int i = 0; i < myMusicList.size(); i++){
            if(myPlayList.get(playPosition).getTitle().equals(myMusicList.get(i).getTitle())){
                good = true;
                imgbtnGood.setBackgroundResource(R.drawable.good);
                break;
            }
        }
        if(!good){
            imgbtnGood.setBackgroundResource(R.drawable.not_good);
        }
    }



    public static void setInformation(String title){

        mp.stop();
        mp.release();
        mp = new MediaPlayer();
        imgbtnPlay.setBackgroundResource(R.drawable.pause);
        imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
        start = true;

        flag = false;

        for(int i = 0; i < myPlayList.size(); i++){

            if(title.equals(myPlayList.get(i).getTitle())){
                playPosition = i;
                flag = true;
                break;
            }
        }

        if(!flag){
            for(int i = 0; i < musicList.size(); i++){
                if(title.equals(musicList.get(i).getTitle())){
                    playPosition = myPlayList.size();
                    myPlayList.add(musicList.get(i));
                    break;
                }
            }
        }





        new DownloadFilesTask(imgMusicIcon).execute(myPlayList.get(playPosition).getAlbumArt());
        tvMusicTitle.setText(myPlayList.get(playPosition).getTitle());
        tvSinger.setText(myPlayList.get(playPosition).getArtist());
        String music = myPlayList.get(playPosition).getPlay();

        Log.d("position", "/"+playPosition);

        try {
            mp.setDataSource(music);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void musicCount() {
        CountRequest countRequest = new CountRequest(musicList.get(playPosition).getTitle());
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(countRequest);
    }

    public static void deleteList(int position){
        myPlayList.remove(position);
    }

    public static void insertList(MusicData m){
        myPlayList.add(m);
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

        drawerLayout = findViewById(R.id.drawerLayout);

        imgbtnGood = findViewById(R.id.imgbtnGood);
        imgbtnPosterPrev = findViewById(R.id.imgbtnPosterPrev);
        imgbtnPosterPlay = findViewById(R.id.imgbtnPosterPlay);
        imgbtnPosterNext = findViewById(R.id.imgbtnPosterNext);

        tvPosterTitle = findViewById(R.id.tvPosterTitle);
        tvPosterSinger = findViewById(R.id.tvPosterSinger);
        tvTimeIng = findViewById(R.id.tvTimeIng);
        tvTimeLast = findViewById(R.id.tvTimeLast);

        imgPoster = findViewById(R.id.imgPoster);

        sbMP3 = findViewById(R.id.sbMP3);
    }

    public void insertGoodMusic(){
        String url = "http://gh888.dothome.co.kr/InsertGood.php";
        String title = musicList.get(playPosition).getTitle();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        InsertRequest insert = new InsertRequest(id, title, url, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(insert);
    }

    public void deleteGoodMusic(){
        String url = "http://gh888.dothome.co.kr/DeleteGood.php";
        String title = musicList.get(playPosition).getTitle();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    boolean success = jo.getBoolean("success");
                    if(success){
                        Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DeleteRequest delete = new DeleteRequest(id, title, url, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(delete);
    }

    @Override
    public void onBackPressed() {


        if(exit){
            drawerLayout.setVisibility(View.INVISIBLE);
            exit = false;
        }else{
            if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show();

                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String url ="http://gh888.dothome.co.kr/SetPosition.php";
        InsertPositionRequest setPosition = new InsertPositionRequest(id, playPosition, url);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(setPosition);

        mp.release();
    }


}