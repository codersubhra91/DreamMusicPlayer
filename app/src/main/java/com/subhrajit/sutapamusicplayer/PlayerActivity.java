package com.subhrajit.sutapamusicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView songName, songArtist, startDuration, totalDuration;
    ImageView btnBack, btnMenu, btnShuffle, cover_art, btnPrevious, btnNext, btnRepeat;
    FloatingActionButton btnPlayPause;
    SeekBar seekBar;
    //Track Position in ArrayList
    int position = -1;
    //Music Tracks' Array
    ArrayList<SongModel> deviceMusicFiles = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private Thread playPauseThread, previousThread, nextThread, repeatThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //View initialization
        intiViews();
        getIntentMethods();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
        });
        metaData(uri);

    }

    @Override
    protected void onResume() {
        playPauseThreadBtn();
        nextThreadButton();
        previousThreadBtn();
        repeatThreadBtn();
        super.onResume();
    }

    private void repeatThreadBtn() {
        repeatThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btnRepeat.setOnClickListener(v->{
                    repeatBtnClicked();
                });
            }
        };
        repeatThread.start();
    }

    private void repeatBtnClicked() {
        if (mediaPlayer.isLooping()){
            btnRepeat.setImageResource(R.drawable.ic_repeat_off);
            mediaPlayer.setLooping(false);
        }else{
            btnRepeat.setImageResource(R.drawable.ic_repeat_on);
            mediaPlayer.setLooping(true);
        }
    }
    private void playPauseThreadBtn() {
        playPauseThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btnPlayPause.setOnClickListener(v->{
                    playPauseBtnClicked();
                });
            }
        };
        playPauseThread.start();
     }

    private void playPauseBtnClicked() {
        if (mediaPlayer.isPlaying()){
            btnPlayPause.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
             PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
         });
        }else{
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
           });

        }
    }

    private void previousThreadBtn() {
        previousThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btnPrevious.setOnClickListener(v->{
                    previousBtnClicked();
                });
            }
        };
        previousThread.start();
    }

    private void previousBtnClicked() {
         if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (deviceMusicFiles.size() - 1) :  (position - 1));
            uri = Uri.parse(deviceMusicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(deviceMusicFiles.get(position).getTitle());
            songArtist.setText(deviceMusicFiles.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
           });
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (deviceMusicFiles.size() - 1) :  (position - 1));
            uri = Uri.parse(deviceMusicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(deviceMusicFiles.get(position).getTitle());
            songArtist.setText(deviceMusicFiles.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
           });
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }

    }

    private void nextThreadButton(){
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btnNext.setOnClickListener(v->{
                    nextBtnClicked();
                });
            }
        };
        nextThread.start();

    }

    private void nextBtnClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % deviceMusicFiles.size());
            uri = Uri.parse(deviceMusicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(deviceMusicFiles.get(position).getTitle());
            songArtist.setText(deviceMusicFiles.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
           });
            btnPlayPause.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % deviceMusicFiles.size());
            uri = Uri.parse(deviceMusicFiles.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            metaData(uri);
            songName.setText(deviceMusicFiles.get(position).getTitle());
            songArtist.setText(deviceMusicFiles.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int playerCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(playerCurrentPosition);
                    startDuration.setText(formattedText(playerCurrentPosition));
                }
                handler.postDelayed(this, 1000);
            }
           });
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    private String formattedText(int playerCurrentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(playerCurrentPosition % 60);
        String minutes = String.valueOf(playerCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;

        if (seconds.length() == 1) {
            return totalNew;
        } else {
            return totalOut;
        }
    }

    private void metaData(Uri uri) {
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        mr.setDataSource(uri.toString());
        int durationTotal = Integer.parseInt(deviceMusicFiles.get(position).getDuration()) /1000;
        totalDuration.setText(formattedText(durationTotal));
        songName.setText(deviceMusicFiles.get(position).getTitle());
        songArtist.setText(deviceMusicFiles.get(position).getArtist());
        byte[] art = mr.getEmbeddedPicture();
        Bitmap bitMap;

        if (art != null) {
            Glide.with(this).asBitmap()
                    .load(art)
                    .into(cover_art);
            bitMap = BitmapFactory.decodeByteArray(art, 0, art.length);
            Palette.from(bitMap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null){
                        ImageView gradient = findViewById(R.id.cover_art);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0x00000000,swatch.getRgb()});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0x000000,swatch.getRgb()});
                        mContainer.setBackground(gradientDrawableBg);
                        songName.setTextColor(Color.WHITE);
                        songArtist.setTextColor(swatch.getTitleTextColor());

                    }else{
                        ImageView gradient = findViewById(R.id.cover_art);
                        RelativeLayout mContainer = findViewById(R.id.mContainer);
                        gradient.setBackgroundResource(R.drawable.gradient_bg);
                        mContainer.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0xff000000});
                        gradient.setBackground(gradientDrawable);

                        GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{0xff000000,0xff000000});
                        mContainer.setBackground(gradientDrawableBg);

                        songName.setTextColor(Color.WHITE);
                        songArtist.setTextColor(Color.DKGRAY);
                    }

                }
            });

        }else
    {
        Glide.with(this).asBitmap()
                .load(R.drawable.disk)
                .into(cover_art);
        ImageView gradient = findViewById(R.id.cover_art);
        RelativeLayout mContainer = findViewById(R.id.mContainer);
        RelativeLayout layout_top_button = findViewById(R.id.layout_top_button);
        gradient.setBackgroundResource(R.drawable.gradient_bg);
        mContainer.setBackgroundResource(R.drawable.main_bg);
        songName.setTextColor(Color.WHITE);
        songArtist.setTextColor(Color.DKGRAY);

    }

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
            try{
                mediaPlayer.prepare();
            }catch (Exception ex){

            }
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            try{
                mediaPlayer.prepare();
            }catch (Exception ex){

            }
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextBtnClicked();
            }
        });
    }

    private void intiViews() {
        songName = findViewById(R.id.songName);
        songArtist = findViewById(R.id.songArtist);
        startDuration = findViewById(R.id.startDuration);
        totalDuration = findViewById(R.id.totalDuration);
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