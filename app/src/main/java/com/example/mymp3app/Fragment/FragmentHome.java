package com.example.mymp3app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mymp3app.MusicAdapter;
import com.example.mymp3app.MusicData;
import com.example.mymp3app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FragmentHome extends Fragment {
    private RecyclerView recyclerNew, recyclerRank;
    private MusicAdapter adapterNew, adapterRank;
    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();

    private int selectPosition;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_home, container, false);

        getMusicData();

        recyclerNew = view.findViewById(R.id.recyclerNew);
        recyclerRank = view.findViewById(R.id.recyclerRank);


        adapterNew = new MusicAdapter(getActivity(), musicList, 0);
        adapterRank = new MusicAdapter(getActivity(), musicList, 1);
        recyclerNew.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerRank.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerNew.setAdapter(adapterNew);
        recyclerRank.setAdapter(adapterRank);

        return view;
    }

    private void getMusicData() {

    }


}
