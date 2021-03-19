package com.example.mymp3app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
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

import com.example.mymp3app.Activity.MusicActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int NEW = 0;
    public static final int RANK = 1;
    public static final int LIST_MUSIC = 2;
    public static final int POSTER = 3;

    private Context context;
    private ArrayList<MusicData> musicList;
    private int item;

    private MediaPlayer mPlayer;
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
            case POSTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_poster, parent, false);
                viewHolder = new PosterViewHolder(view);
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
                }

                break;
            case LIST_MUSIC:

                if(viewHolder instanceof ListViewHolder) {
                    ListViewHolder listVH = (ListViewHolder) viewHolder;
                    musicSet(listVH.imgListAlbum, listVH.tvListTitle, listVH.tvListSinger, position);
                }
                break;

            case POSTER:

                if(viewHolder instanceof PosterViewHolder) {
                    PosterViewHolder posterVH = (PosterViewHolder) viewHolder;

                    String url = musicList.get(position).getAlbumArt();
                    ImageLoad task = new ImageLoad(url, posterVH.imgPosterAlbum);
                    task.execute();
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
        ImageLoad task = new ImageLoad(url, albumArt);
        task.execute();
        title.setText(musicList.get(position).getTitle());
        singer.setText(musicList.get(position).getArtist());

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

                Intent intent = new Intent(view.getContext(), MusicActivity.class);
                intent.putExtra("musicList", musicList);
                intent.putExtra("position", getAdapterPosition());
                view.getContext().startActivity(intent);
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


        }
    }
    
    public class ListViewHolder extends RecyclerView.ViewHolder{
        private ImageButton imgbtnListPlay;
        private ImageView imgListAlbum;
        private TextView tvListTitle, tvListSinger;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbtnListPlay = itemView.findViewById(R.id.imgbtnListPlay);
            imgListAlbum = itemView.findViewById(R.id.imgListAlbum);
            tvListTitle = itemView.findViewById(R.id.tvListTitle);
            tvListSinger = itemView.findViewById(R.id.tvListSinger);
        }
    }
    
    
    public class PosterViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPosterAlbum;

        public PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPosterAlbum = itemView.findViewById(R.id.imgPosterAlbum);
        }
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    
}
