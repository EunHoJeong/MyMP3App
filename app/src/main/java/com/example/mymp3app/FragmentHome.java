package com.example.mymp3app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerNew, recyclerRank;
    private MusicAdapter adapterNew, adapterRank;
    private ArrayList<MusicData> sdCardList = new ArrayList<MusicData>();

    private int selectPosition;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_home, container, false);

        findContentProviderMP3ToArrayList();

        recyclerNew = view.findViewById(R.id.recyclerNew);
        recyclerRank = view.findViewById(R.id.recyclerRank);


        adapterNew = new MusicAdapter(getActivity(), sdCardList, 0);
        adapterRank = new MusicAdapter(getActivity(), sdCardList, 1);
        recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRank.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerNew.setAdapter(adapterNew);
        recyclerRank.setAdapter(adapterRank);

        return view;
    }

    private void findContentProviderMP3ToArrayList() {
        String[] data = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};

        // 전체 영역에서 음악파일 가져온다.
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                data,null,null,data[2] + " ASC");    // Cursor타입이 반환됨, 원하는 정보를 찾아줌
        // data가 내가 보고 싶은 항목, 그리고 TITLE 항목으로 오름차순으로 가져와라
        if(cursor != null){
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(data[0]));
                String artist = cursor.getString(cursor.getColumnIndex(data[1]));
                String title = cursor.getString(cursor.getColumnIndex(data[2]));
                String albumArt = cursor.getString(cursor.getColumnIndex(data[3]));
                String duration = cursor.getString(cursor.getColumnIndex(data[4]));

                MusicData musicData = new MusicData(id,artist,title,albumArt,duration);
                sdCardList.add(musicData);
            }   // end of while
        }
    }

}
