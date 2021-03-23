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
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int PLAY = 0;
    private static final int PASUE = 1;
    private static final int REPLAY = 2;
    private static final int ONE = 2;
    private static final int ALL = 0;
    private static final int NO = 1;
    //메인
    private static ImageButton imgbtnGood, imgbtnPosterPrev, imgbtnPosterPlay, imgbtnPosterNext;
    private static ImageView imgPoster;
    private static TextView tvPosterTitle, tvPosterSinger, tvTimeIng, tvTimeLast;
    private static SeekBar sbMP3;
    //포스터
    private static ImageButton imgbtnPrev, imgbtnPlay, imgbtnNext, imgbtnList, imgbtnHome, imgbtnMyMusic, imgbtnSearch, imgbtnShuffle, imgbtnLoop;
    private static ImageView imgMusicIcon;
    private static TextView tvSinger, tvMusicTitle, tvHome, tvMyMusic, tvSearch;
    private static DrawerLayout drawerLayout;

    private static ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private static ArrayList<MusicData> myMusicList = new ArrayList<MusicData>();
    private static ArrayList<MusicData> myPlayList = new ArrayList<MusicData>();

    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    private static MediaPlayer mp = new MediaPlayer();

    private static boolean exit = false;
    private static boolean start = false;
    private static boolean flag = false;
    public  static String id;
    private static long backKeyPressedTime = 0;
    private static int playPosition;
    private static String title;
    private static int position;

    private boolean good = false;
    private boolean shuffle = false;
    private boolean reStart = false;
    private int loop = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        musicList = getIntent().getParcelableArrayListExtra("musicList");
        myMusicList = getIntent().getParcelableArrayListExtra("myMusicList");
        myPlayList = getIntent().getParcelableArrayListExtra("myPlayList");
        int position = (getIntent().getIntExtra("position", 0));
        playPosition = position;

        id = getIntent().getStringExtra("id");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicList", musicList);

        findViewByIdFunc();

        //메인 이벤트
        eventHandlerFunc();
        //포스터 이벤트
        eventHandelerPoster();

        tvMusicTitle.setSelected(true);
        firstSet(position);

        mpPlayAuto();

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


    private void mpPlayAuto(){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp3) {
                if(loop != 2 || shuffle){
                    if(shuffle && loop != 0){
                        playPosition = (int)(Math.random()*myPlayList.size());
                    }else{
                        playPosition= (playPosition +loop)%myPlayList.size();
                    }

                    try {
                        mp3.reset();
                        mp3.setDataSource(myPlayList.get(playPosition).getPlay());

                        mp = mp3;

                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sbMP3.setProgress(0);

                    mp.start();
                    new DownloadFilesTask(imgPoster).execute(myPlayList.get(playPosition).getAlbumArt());
                    tvPosterTitle.setText(myPlayList.get(playPosition).getTitle());
                    tvPosterSinger.setText(myPlayList.get(playPosition).getArtist());
                    tvTimeLast.setText(sdf.format(myPlayList.get(playPosition).getDuration()));
                    startSeekBar();
                }else{
                    sbMP3.setProgress(0);
                    tvTimeIng.setText("00:00");
                    imgbtnPosterPlay.setBackgroundResource(R.drawable.bigplay);
                    imgbtnPlay.setBackgroundResource(R.drawable.play_music);
                    start = false;
                    mp3.reset();
                    try {
                        mp3.setDataSource(myPlayList.get(playPosition).getPlay());
                        mp = mp3;

                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //reStart = true;

                }

            }
        });
    }

    private void eventHandlerFunc() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicList", musicList);

        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList("myMusicList", myMusicList);

        tvHome.setTextColor(0xFF87CEEB);


        // 다음 버튼
        imgbtnNext.setOnClickListener(v -> {
            imgbtnPlay.setBackgroundResource(R.drawable.pause);
            try {
                mp.stop();

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
                MediaPlayer mp3 = new MediaPlayer();
                mp3.setDataSource(music);
                mp = mp3;
                mp.prepare();
                mp.start();
                mpPlayAuto();


            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //이전 버튼
        imgbtnPrev.setOnClickListener(v -> {
            imgbtnPlay.setBackgroundResource(R.drawable.pause);
            try {
                mp.stop();
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
                MediaPlayer mp3 = new MediaPlayer();
                mp3.setDataSource(music);
                mp = mp3;
                mp.prepare();
                mp.start();
                mpPlayAuto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //플레이 버튼
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

                imgbtnPlay.setBackgroundResource(R.drawable.pause);
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
                mp.start();
                startSeekBar();
                start = true;
            }
        });// end of play

        imgbtnPosterNext.setOnClickListener(v -> {
            imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
            try {

                mp.stop();
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
                MediaPlayer mp3 = new MediaPlayer();
                mp3.setDataSource(music);
                mp = mp3;
                mp.prepare();
                mp.start();
                mpPlayAuto();
                startSeekBar();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        imgbtnPosterPrev.setOnClickListener(v -> {

            imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
            try {
                mp.stop();

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
                MediaPlayer mp3 = new MediaPlayer();
                mp3.setDataSource(music);
                mp = mp3;
                mp.prepare();
                mp.start();
                mpPlayAuto();
                startSeekBar();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                playPosition = myPlayList.size()-1;
            }
        });


        imgbtnLoop.setOnClickListener(v -> {
            switch(loop){
                case ALL:
                    imgbtnLoop.setBackgroundResource(R.drawable.loop);
                    Toast.makeText(this, "전체재생", Toast.LENGTH_SHORT).show();
                    loop = 1;
                    mpPlayAuto();
                    break;
                case NO:
                    imgbtnLoop.setBackgroundResource(R.drawable.un_loop);
                    loop = 2;
                    break;
                case ONE:
                    imgbtnLoop.setBackgroundResource(R.drawable.one_loop);
                    loop = 0;
                    Toast.makeText(this, "현재 곡 반복재생", Toast.LENGTH_SHORT).show();
                    mpPlayAuto();
                    break;
            }
        });

        imgbtnShuffle.setOnClickListener(v -> {
            if(shuffle){
                imgbtnShuffle.setBackgroundResource(R.drawable.un_shuffle);
                Toast.makeText(this, "순서대로 재생", Toast.LENGTH_SHORT).show();
               shuffle = false;
            }else{
                imgbtnShuffle.setBackgroundResource(R.drawable.shuffle);
                mpPlayAuto();
                Toast.makeText(this, "랜덤 재생", Toast.LENGTH_SHORT).show();
                shuffle = true;
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

    public void startSeekBar(){
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

            }

        };
        thread.start();

    }

    //Poster 화면에 띄움
    public void openPoster(){
        startSeekBar();
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

        MediaPlayer mp3 = new MediaPlayer();
        try {
            mp3.setDataSource(music);
            mp = mp3;
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
        imgbtnShuffle = findViewById(R.id.imgbtnShuffle);
        imgbtnLoop = findViewById(R.id.imgbtnLoop);

        tvPosterTitle = findViewById(R.id.tvPosterTitle);
        tvPosterSinger = findViewById(R.id.tvPosterSinger);
        tvTimeIng = findViewById(R.id.tvTimeIng);
        tvTimeLast = findViewById(R.id.tvTimeLast);

        imgPoster = findViewById(R.id.imgPoster);

        sbMP3 = findViewById(R.id.sbMP3);
    }

    public void insertGoodMusic(){
        String url = "http://gh888.dothome.co.kr/InsertGood.php";
        String title = myPlayList.get(playPosition).getTitle();

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
            FragmentMyMusic.refresh();
            new DownloadFilesTask(imgMusicIcon).execute(myPlayList.get(playPosition).getAlbumArt());
            tvMusicTitle.setText(myPlayList.get(playPosition).getTitle());
            tvSinger.setText(myPlayList.get(playPosition).getArtist());
            String music = myPlayList.get(playPosition).getPlay();
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

        mp.stop();
        mp.release();
    }



}