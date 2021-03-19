package com.example.mymp3app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymp3app.MusicAdapter;
import com.example.mymp3app.MusicData;
import com.example.mymp3app.R;

import java.util.ArrayList;

public class FragmentMyMusic extends Fragment {


    private RecyclerView recyclerGood;
    private MusicAdapter adapter;
    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_mymusic, container, false);


        recyclerGood = view.findViewById(R.id.recyclerGood);

        adapter = new MusicAdapter(getActivity(), musicList, 2);
        recyclerGood.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerGood.setAdapter(adapter);

        return view;
    }


}
