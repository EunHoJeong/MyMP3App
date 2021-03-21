package com.example.mymp3app;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.mymp3app.Activity.MainActivity;
import com.example.mymp3app.Activity.PlayList;
import com.example.mymp3app.Data.MusicData;
import com.example.mymp3app.Request.CountRequest;
import com.example.mymp3app.Request.DeleteRequest;
import com.example.mymp3app.Request.InsertRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int NEW = 0;
    public static final int RANK = 1;
    public static final int LIST_MUSIC = 2;
    public static final int PLAY_LIST = 3;
    private static final int PLAY = 0;
    private static final int PASUE = 1;
    private static final int REPLAY = 2;
    private int num = 0;


    private Context context;
    private ArrayList<MusicData> musicList;
    private int item;

    private MediaPlayer mp = new MediaPlayer();
    private OnItemClickListener mListener = null;

    public MusicAdapter(Context context, ArrayList<MusicData> musicList, int item) {
        this.context = context;
        this.musicList = musicList;
        this.item = item;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreateViewHolder");
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case NEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_new, parent, false);
                viewHolder = new NewViewHolder(view);
                break;
            case RANK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_rank, parent, false);
                viewHolder = new RankViewHolder(view);
                break;
            case LIST_MUSIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_listmusic, parent, false);
                viewHolder = new ListViewHolder(view);
                break;
            case PLAY_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist, parent, false);
                viewHolder = new PlayListViewHolder(view);
                break;
            default:
                Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show();
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Adapter", "getItemViewType");
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Log.d("Adapter", "onBindViewHolder");


        switch(item){
            case NEW:

                if(viewHolder instanceof NewViewHolder) {
                    NewViewHolder newVH = (NewViewHolder) viewHolder;
                    musicSet(newVH.imgNewAlbumArt, newVH.tvNewTitle, newVH.tvNewSinger, position);
                }

                break;
            case RANK:

                if(viewHolder instanceof RankViewHolder) {
                    RankViewHolder rankVH = (RankViewHolder) viewHolder;
                    musicSet(rankVH.imgRankAlbumArt, rankVH.tvRankTitle, rankVH.tvRankSinger, position);
                    rankVH.tvRank.setText(String.valueOf(position+1));
                }

                break;
            case LIST_MUSIC:

                if(viewHolder instanceof ListViewHolder) {
                    ListViewHolder listVH = (ListViewHolder) viewHolder;
                    musicSet(listVH.imgListAlbum, listVH.tvListTitle, listVH.tvListSinger, position);
                }
                break;
            case PLAY_LIST:

                if(viewHolder instanceof PlayListViewHolder) {
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    PlayListViewHolder play_listVH = (PlayListViewHolder) viewHolder;
                    musicSet(play_listVH.imgPlayAlbum, play_listVH.tvPlayTitle, play_listVH.tvPlaySinger, position);
                    play_listVH.tvPlayDuration.setText(sdf.format(musicList.get(position).getDuration()));
                }
                break;

            default:
                Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //MusicData 값 주는곳
    private void musicSet(ImageView albumArt, TextView title, TextView singer, int position) {


        String url = musicList.get(position).getAlbumArt();

        new DownloadFilesTask(albumArt).execute(url);

        title.setText(musicList.get(position).getTitle());
        singer.setText(musicList.get(position).getArtist());

    }

    private void ImageSet(){

    }


    @Override
    public int getItemCount() {
        Log.d("Adapter", "getItemCount");
        return (musicList != null) ? musicList.size() : 0;
    }




    public class NewViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgNewAlbumArt;
        private TextView tvNewTitle, tvNewSinger;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("Adapter", "NewViewHolder");

            imgNewAlbumArt = itemView.findViewById(R.id.imgNewAlbumArt);
            tvNewTitle = itemView.findViewById(R.id.tvNewTitle);
            tvNewSinger = itemView.findViewById(R.id.tvNewSinger);

            imgNewAlbumArt.setOnClickListener(view -> {
                musicCount(getAdapterPosition());
                insertPlayListRequest(musicList.get(getAdapterPosition()).getTitle(), getAdapterPosition());
                MainActivity.setInformation(musicList.get(getAdapterPosition()).getTitle());
            });

        }
    }

    public class RankViewHolder extends  RecyclerView.ViewHolder{
        private ImageButton imgbtnRankPlay;
        private ImageView imgRankAlbumArt;
        private TextView tvRankTitle, tvRankSinger, tvRank;


        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("Adapter", "RankViewHolder");

            imgbtnRankPlay = itemView.findViewById(R.id.imgbtnRankPlay);
            imgRankAlbumArt = itemView.findViewById(R.id.imgRankAlbumArt);
            tvRankTitle = itemView.findViewById(R.id.tvRankTitle);
            tvRankSinger = itemView.findViewById(R.id.tvRankSinger);
            tvRank = itemView.findViewById(R.id.tvRank);

            imgbtnRankPlay.setOnClickListener(v -> {
                musicCount(getAdapterPosition());
                insertPlayListRequest(musicList.get(getAdapterPosition()).getTitle(), getAdapterPosition());
                MainActivity.setInformation(musicList.get(getAdapterPosition()).getTitle());

            });


            imgRankAlbumArt.setOnClickListener(v -> {

            });



        }
    }
    
    public class ListViewHolder extends RecyclerView.ViewHolder{
        private ImageButton imgbtnListPlay;
        private ImageView imgListAlbum;
        private TextView tvListTitle, tvListSinger, tvRank;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbtnListPlay = itemView.findViewById(R.id.imgbtnListPlay);
            imgListAlbum = itemView.findViewById(R.id.imgListAlbum);
            tvListTitle = itemView.findViewById(R.id.tvListTitle);
            tvListSinger = itemView.findViewById(R.id.tvListSinger);
            tvRank = itemView.findViewById(R.id.tvRank);

            imgbtnListPlay.setOnClickListener(v -> {
                musicCount(getAdapterPosition());
                Toast.makeText(context, getAdapterPosition()+"", Toast.LENGTH_SHORT).show();
                insertPlayListRequest(musicList.get(getAdapterPosition()).getTitle(), getAdapterPosition());
                MainActivity.setInformation(musicList.get(getAdapterPosition()).getTitle());
            });

            imgListAlbum.setOnClickListener(v -> {

            });
        }
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPlayAlbum;
        private ImageButton imgbtnPlayPlay;
        private TextView tvPlayTitle, tvPlaySinger, tvPlayDuration;

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlayAlbum = itemView.findViewById(R.id.imgPlayAlbum);
            imgbtnPlayPlay = itemView.findViewById(R.id.imgbtnPlayPlay);
            tvPlayTitle = itemView.findViewById(R.id.tvPlayTitle);
            tvPlaySinger = itemView.findViewById(R.id.tvPlaySinger);
            tvPlayDuration = itemView.findViewById(R.id.tvPlayDuration);

            imgbtnPlayPlay.setOnClickListener(v -> {
                deletePlayListRequest(musicList.get(getAdapterPosition()).getTitle(), getAdapterPosition());
                MainActivity.deleteList(getAdapterPosition());
                PlayList.deleteList(getAdapterPosition());
                PlayList.refresh();
            });




        }
    }



    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }




    private void musicCount(int position) {
        CountRequest countRequest = new CountRequest(musicList.get(position).getTitle());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(countRequest);
    }

    public void deletePlayListRequest(String title, int position){
        String url = "http://gh888.dothome.co.kr/DeletePlayList.php";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    boolean success = jo.getBoolean("success");
                    if(success){
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DeleteRequest delete = new DeleteRequest(MainActivity.id, title, url, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(delete);
    }

    public void insertPlayListRequest(String title, int position){

        String url = "http://gh888.dothome.co.kr/InsertPlayList.php";


        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    boolean success = jo.getBoolean("success");
                    if(!success){
                        Toast.makeText(context, "재생목록에 추가합니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        InsertRequest insert = new InsertRequest(MainActivity.id, title, url, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(insert);
    }
}
