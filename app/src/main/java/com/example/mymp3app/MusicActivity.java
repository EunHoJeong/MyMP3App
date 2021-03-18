package com.example.mymp3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    private ImageButton imgbtnGood, imgbtnPosterPrev, imgbtnPosterPlay, imgbtnPosterNext;
    private TextView tvPosterTitle, tvPosterSinger, tvTimeIng, tvTimeLast;
    private SeekBar sbMP3;
    private MediaPlayer mPlayer;
    private ArrayList<MusicData> musicList;
    private boolean good = false;
    private boolean flag = false;
    private int selectedPosition;

    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent data = getIntent();

        musicList = (ArrayList<MusicData>) data.getSerializableExtra("musicList");
        selectedPosition = data.getIntExtra("position", 0);
        findViewByIdFunc();

        eventHandlerFunc();
    }

    private void eventHandlerFunc() {
        imgbtnGood.setOnClickListener(v -> {
            if(good){
                imgbtnGood.setBackgroundResource(R.drawable.not_good);
                good = false;
            }else{
                imgbtnGood.setBackgroundResource(R.drawable.good);
                good = true;
            }

        });

        imgbtnPosterPlay.setOnClickListener(v -> {
            if(mPlayer.isPlaying()){
                mPlayer.pause();
                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigplay);
            }else{
                if(flag){
                    mPlayer.start();
                }else{
                    mPlayer = new MediaPlayer();
                    MusicData musicData = musicList.get(selectedPosition);
                    Uri musicUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicData.getId());
                    try {
                        mPlayer.setDataSource(this, musicUri);
                        mPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlayer.start();
                    flag = true;

                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            if(mPlayer == null) return;

                            sbMP3.setMax(mPlayer.getDuration());

                            while(mPlayer.isPlaying()){
                                runOnUiThread(()->{
                                    sbMP3.setProgress(mPlayer.getCurrentPosition());
                                    tvTimeIng.setText(sdf.format(mPlayer.getCurrentPosition()));
                                });
                                SystemClock.sleep(500);
                            }
                        }
                    };
                    thread.start();
                }

                imgbtnPosterPlay.setBackgroundResource(R.drawable.bigpause);
            }
        });

        sbMP3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean flag) {
                if(flag){
                    mPlayer.seekTo(i);
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

    private void findViewByIdFunc() {
        imgbtnGood = findViewById(R.id.imgbtnGood);
        imgbtnPosterPrev = findViewById(R.id.imgbtnPosterPrev);
        imgbtnPosterPlay = findViewById(R.id.imgbtnPosterPlay);
        imgbtnPosterNext = findViewById(R.id.imgbtnPosterNext);

        tvPosterTitle = findViewById(R.id.tvPosterTitle);
        tvPosterSinger = findViewById(R.id.tvPosterSinger);

        sbMP3 = findViewById(R.id.sbMP3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}