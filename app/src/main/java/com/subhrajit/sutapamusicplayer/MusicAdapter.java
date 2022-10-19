package com.subhrajit.sutapamusicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
    ArrayList<SongModel>musicFiles;
    Context context;

    public MusicAdapter(ArrayList<SongModel> musicFiles, Context context) {
        this.musicFiles = musicFiles;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_songs_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.songName.setText(musicFiles.get(position).getTitle());
        holder.songArtistName.setText(musicFiles.get(position).getArtist());
        holder.songDuration.setText(musicFiles.get(position).getDuration());

        //AlbumArt showing
        byte[] image = getAlbumArt(musicFiles.get(position).getPath());
        if (image != null){
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.songAlbumArt);
        }else{
            Glide.with(context).asBitmap()
                    .load(R.drawable.disk);
        }

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("TrackPosition",position);
            intent.putExtra("AllMusics",musicFiles);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView songAlbumArt;
        TextView songName,songArtistName,songDuration;
        RelativeLayout audioItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songAlbumArt = itemView.findViewById(R.id.music_img);
            songName = itemView.findViewById(R.id.music_file_name);
            songArtistName = itemView.findViewById(R.id.music_artist);
            songDuration = itemView.findViewById(R.id.music_duration);
            audioItem = itemView.findViewById(R.id.audioItem);

            //Enabling Marque
            songName.setSelected(true);
            songArtistName.setSelected(true);
        }
    }
    //AlbumArt fetching and showing
    byte[] getAlbumArt(String uri){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(uri);
        byte[] art = metadataRetriever.getEmbeddedPicture();
        metadataRetriever.release();
        return art;

    }
}
