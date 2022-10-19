package com.subhrajit.sutapamusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView songName,songArtist,startDuration,totalDuration;
    ImageView btnBack,btnMenu,btnShuffle,cover_art,btnPrevious,btnNext,btnRepeat;
    FloatingActionButton btnPlayPause;
    SeekBar seekBar;
    //Track Position in ArrayList
    int position = -1;
    //Music Tracks' Array
    ArrayList<SongModel> deviceMusicFiles = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //View initialization
        intiViews();
        getIntentMethods();


    }

    private void getIntentMethods() {
        position = getIntent().getIntExtra("TrackPosition",-1);
        deviceMusicFiles.addAll((ArrayList<SongModel>) getIntent().getSerializableExtra("AllMusics"));

        if (deviceMusicFiles != null){
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            uri = Uri.parse(deviceMusicFiles.get(position).getPath());
        }

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();

            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }


    }

    private void intiViews() {
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        btnBack = findViewById(R.id.btnBack);
        btnMenu = findViewById(R.id.btnMenu);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnNext = findViewById(R.id.btnNext);
        btnRepeat = findViewById(R.id.btnRepeat);

        cover_art = findViewById(R.id.cover_art);

        seekBar = findViewById(R.id.seekBar);
    }
}