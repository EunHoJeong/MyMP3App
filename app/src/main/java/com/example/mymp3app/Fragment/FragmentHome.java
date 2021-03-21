package com.example.mymp3app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymp3app.MusicAdapter;
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.R;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    public static final int NEW = 0;
    public static final int RANK = 1;
    private RecyclerView recyclerNew, recyclerRank;
    private TextView tvKPop, tvRab, tvPop;
    private MusicAdapter adapterNew, adapterRank;

    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private ArrayList<MusicData> kpopList = new ArrayList<MusicData>();
    private ArrayList<MusicData> rabList = new ArrayList<MusicData>();
    private ArrayList<MusicData> popList = new ArrayList<MusicData>();

    boolean first = false;

    private int selectPosition;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_home, container, false);

        Bundle bundle = getArguments();
        musicList = bundle.getParcelableArrayList("musicList");


        recyclerNew = view.findViewById(R.id.recyclerNew);
        recyclerRank = view.findViewById(R.id.recyclerRank);
        tvKPop = view.findViewById(R.id.tvKPoP);
        tvRab = view.findViewById(R.id.tvRab);
        tvPop = view.findViewById(R.id.tvPop);

        estGenre();

        eventHandlerFunc();






        return view;
    }

    private void estGenre() {
        String kpop = "가요";
        String rab = "힙합";
        String pop = "발라드";
        for(int i = 0; i < musicList.size(); i++){
            String genre = musicList.get(i).getGenre();
            if(kpop.equals(genre)){
                kpopList.add(musicList.get(i));
                continue;

            }else if(rab.equals(genre)){
                rabList.add(musicList.get(i));
                continue;

            }else if(pop.equals(genre)){
                popList.add(musicList.get(i));
                continue;
            }
        }
    }

    private void eventHandlerFunc() {
        tvKPop.setTextColor(0xFF87CEEB);
        adapterNew = new MusicAdapter(getActivity(), kpopList, NEW);
        adapterRank = new MusicAdapter(getActivity(), musicList, RANK);
        recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRank.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerNew.setAdapter(adapterNew);
        recyclerRank.setAdapter(adapterRank);

        tvKPop.setOnClickListener(v -> {
            tvKPop.setTextColor(0xFF87CEEB);
            tvRab.setTextColor(Color.BLACK);
            tvPop.setTextColor(Color.BLACK);
            adapterNew = new MusicAdapter(getActivity(), kpopList, NEW);
            recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerNew.setAdapter(adapterNew);
        });

        tvRab.setOnClickListener(v -> {
            tvKPop.setTextColor(Color.BLACK);
            tvRab.setTextColor(0xFF87CEEB);
            tvPop.setTextColor(Color.BLACK);
            adapterNew = new MusicAdapter(getActivity(), rabList, NEW);
            recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerNew.setAdapter(adapterNew);
        });

        tvPop.setOnClickListener(v -> {
            tvKPop.setTextColor(Color.BLACK);
            tvRab.setTextColor(Color.BLACK);
            tvPop.setTextColor(0xFF87CEEB);
            adapterNew = new MusicAdapter(getActivity(), popList, NEW);
            recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerNew.setAdapter(adapterNew);
        });
    }


}
