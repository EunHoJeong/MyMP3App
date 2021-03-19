package com.example.mymp3app;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class MusicData {
    private String play;
    private String artist;
    private String title;
    private String albumArt;
    private String duration;
    private String genre;
    private String count;

    public MusicData(String play, String artist, String title, String albumArt, String duration, String genre, String count) {
        this.play = play;
        this.artist = artist;
        this.title = title;
        this.albumArt = albumArt;
        this.duration = duration;
        this.genre = genre;
        this.count = count;
    }

    public String getPlay() {
        return play;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public String getCount() {
        return count;
    }
}