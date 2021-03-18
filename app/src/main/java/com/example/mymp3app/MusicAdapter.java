package com.example.mymp3app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
                    NewViewHolder newViewHolder = (NewViewHolder) viewHolder;
                    Bitmap albumImg = getAlbumImg(context, Integer.parseInt(musicList.get(position).getAlbumArt()), 200);
                    try{
                        newViewHolder.imgNewAlbumArt.setImageBitmap(albumImg);
                        newViewHolder.tvNewTitle.setText(musicList.get(position).getTitle());
                        newViewHolder.tvNewSinger.setText(musicList.get(position).getArtists());
                    }catch (Exception e){

                    }
                }
                break;
            case RANK:
                if(viewHolder instanceof RankViewHolder) {
                    RankViewHolder rankViewHolder = (RankViewHolder) viewHolder;
                    Bitmap albumImg = getAlbumImg(context, Integer.parseInt(musicList.get(position).getAlbumArt()), 50);
                    try{
                        rankViewHolder.imgRankAlbumArt.setImageBitmap(albumImg);
                        rankViewHolder.tvRankTitle.setText(musicList.get(position).getTitle());
                        rankViewHolder.tvRankSinger.setText(musicList.get(position).getArtists());
                    }catch (Exception e){

                    }
                }
                break;
            case LIST_MUSIC:
                if(viewHolder instanceof ListViewHolder) {
                    ListViewHolder listViewHolder = (ListViewHolder) viewHolder;
                    Bitmap albumImg = getAlbumImg(context, Integer.parseInt(musicList.get(position).getAlbumArt()), 50);
                    try{
                        listViewHolder.imgListAlbum.setImageBitmap(albumImg);
                        listViewHolder.tvListTitle.setText(musicList.get(position).getTitle());
                        listViewHolder.tvListSinger.setText(musicList.get(position).getArtists());
                    }catch (Exception e){

                    }
                }
                break;
            case POSTER:
                if(viewHolder instanceof PosterViewHolder) {
                    PosterViewHolder posterViewHolder = (PosterViewHolder) viewHolder;
                    Bitmap albumImg = getAlbumImg(context, Integer.parseInt(musicList.get(position).getAlbumArt()), 50);
                    try{
                        posterViewHolder.imgPosterAlbum.setImageBitmap(albumImg);

                    }catch (Exception e){

                    }
                }
                break;
            default:
                Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show();
                break;
        }






    }

    public Bitmap getAlbumImg(Context context, int albumArt, int imgMaxSize) {
        Log.d("Adapter", "getAlbumImg");
        /*컨텐트 프로바이더(Content Provider)는 앱 간의 데이터 공유를 위해 사용됨.
        특정 앱이 다른 앱의 데이터를 직접 접근해서 사용할 수 없기 때문에
        무조건 컨텐트 프로바이더를 통해 다른 앱의 데이터를 사용해야만 한다.
        다른 앱의 데이터를 사용하고자 하는 앱에서는 Uri를 이용하여 컨텐트 리졸버(Content Resolver)를 통해
        다른 앱의 컨텐트 프로바이더에게 데이터를 요청하게 되는데
        요청받은 컨텐트 프로바이더는 Uri를 확인하고 내부에서 데이터를 꺼내어 컨텐트 리졸버에게 전달한다.
        */
        BitmapFactory.Options options=new BitmapFactory.Options();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri=Uri.parse("content://media/external/audio/albumart/"+albumArt);

        if(uri != null){
            ParcelFileDescriptor fd = null;
            try {
                fd = contentResolver.openFileDescriptor(uri, "r");

                //메모리할당을 하지 않으면서 해당된 정보를 읽어올수 있음.
                options.inJustDecodeBounds = true;
                int scale = 0;

                if(options.outHeight > imgMaxSize || options.outWidth > imgMaxSize){
                    scale = (int)Math.pow(2,(int) Math.round(Math.log(imgMaxSize /
                            (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
                }

                //비트맵을 위해서 메모리를 할당하겠다.
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;

                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(),null,options);

                if(bitmap != null){
                    if(options.outWidth != imgMaxSize || options.outHeight != imgMaxSize){
                        Bitmap tmp = Bitmap.createScaledBitmap(bitmap, imgMaxSize, imgMaxSize,true);
                        bitmap.recycle();
                        bitmap = tmp;
                    }
                }

                return  bitmap;

            } catch (FileNotFoundException e) {
                Log.d("MusicAdapter","컨텐트 리졸버 에러발생");
            } finally {
                if(fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
        return null;
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
