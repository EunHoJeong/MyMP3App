package com.example.mymp3app.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicData implements Parcelable {
    private String play;
    private String artist;
    private String title;
    private String albumArt;
    private int duration;
    private String genre;
    private int count;

    public MusicData(String play, String artist, String title, String albumArt, int duration, String genre, int count) {
        this.play = play;
        this.artist = artist;
        this.title = title;
        this.albumArt = albumArt;
        this.duration = duration;
        this.genre = genre;
        this.count = count;
    }

    protected MusicData(Parcel in) {
        play = in.readString();
        artist = in.readString();
        title = in.readString();
        albumArt = in.readString();
        duration = in.readInt();
        genre = in.readString();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(play);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(albumArt);
        dest.writeInt(duration);
        dest.writeString(genre);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MusicData> CREATOR = new Creator<MusicData>() {
        @Override
        public MusicData createFromParcel(Parcel in) {
            return new MusicData(in);
        }

        @Override
        public MusicData[] newArray(int size) {
            return new MusicData[size];
        }
    };

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

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public int getCount() {
        return count;
    }
}