package com.example.mymp3app;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class MusicData {
    private String id;
    private String artists;
    private String title;
    private String albumArt;
    private String duration;

    public MusicData(String id, String artists, String title, String albumArt, String duration) {
        this.id = id;
        this.artists = artists;
        this.title = title;
        this.albumArt = albumArt;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicData musicData = (MusicData) o;
        return Objects.equals(id, musicData.id) &&
                Objects.equals(albumArt, musicData.albumArt);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id,albumArt);
    }
}
