package com.example.mymp3app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
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
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.R;
import com.example.mymp3app.Request.SearchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class FragmentSearch extends Fragment {
    private RecyclerView recyclerSearch;
    private MusicAdapter adapter;
    private SearchView searchView;
    private ArrayList<MusicData> musicList = new ArrayList<MusicData>();
    private ArrayList<MusicData> searchList = new ArrayList<MusicData>();


    private ArrayList<String> text = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.frag_search, container, false);

        Bundle bundle = getArguments();
        musicList = bundle.getParcelableArrayList("musicList");
        searchView = view.findViewById(R.id.searchView);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);

        searchView.setMaxWidth(Integer.MAX_VALUE);


        MusicAdapter musicAdapter = new MusicAdapter(getActivity(), musicList, 2);

        adapter = new MusicAdapter(getActivity(), searchList, 2);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerSearch.setAdapter(musicAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //text = search(newText);
                changeMusic(newText);

                recyclerSearch.setAdapter(adapter);


                return true;
            }
        });



        return view;
    }




    private void changeMusic(String text) {
        searchList.clear();
        for(int i = 0; i < musicList.size(); i++){
            if(musicList.get(i).getTitle().contains(text)){
                searchList.add(musicList.get(i));
            }
        }


    }

    public ArrayList<String> search(String title){
        ArrayList<String> searchTitle = new ArrayList<String>();
        String url = "http://gh888.dothome.co.kr/SearchMusic.php";
        Response.Listener<String> searchListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ja = new JSONArray(response);
                    for(int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        searchTitle.add(jo.getString("title"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SearchRequest searchRequest = new SearchRequest(title, url, searchListener);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(searchRequest);

        return searchTitle;
    }


}