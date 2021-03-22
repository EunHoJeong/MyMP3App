package com.example.mymp3app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymp3app.Activity.MainActivity;
import com.example.mymp3app.MusicAdapter;
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.R;

import java.util.ArrayList;

public class FragmentMyMusic extends Fragment {
    public static final int LIST_MUSIC = 2;
    private RecyclerView recyclerGood;
    private MusicAdapter adapter;
    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_mymusic, container, false);

        Bundle bundle = getArguments();
        musicList = bundle.getParcelableArrayList("myMusicList");

        recyclerGood = view.findViewById(R.id.recyclerGood);

        adapter = new MusicAdapter(getActivity(), musicList, LIST_MUSIC);
        recyclerGood.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerGood.setAdapter(adapter);



        return view;
    }


}
