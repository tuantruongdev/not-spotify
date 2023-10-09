package com.jtinteractive.notspotify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.Tracks;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.session.MediaConstants;
import androidx.media3.ui.PlayerControlView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerControlView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize ExoPlayer

        player = new ExoPlayer.Builder(getApplicationContext()).build();
        playerView = findViewById(R.id.player_view);

        Intent intent = new Intent(getApplicationContext(), PlaybackService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }


    }
    void update(){
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, "test");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri("http://localhost:8080/song/402/stream"));
        playerView.setPlayer(player);//todo
        player.setMediaSource(mediaSource);
        //player.prepare();
        //player.setPlayWhenReady(true);
        player.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(Tracks tracks) {
                Player.Listener.super.onTracksChanged(tracks);
                //      showMediaArtwork();
            }
        });

    }
    void preparePlayer(){
        Bundle extras = new Bundle();
        extras.putBoolean(MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT, true);

    }


    @Override
    protected void onResume() {
        super.onResume();
        MySingleton mySingleton = MySingleton.getInstance();
        if (mySingleton.getMediaSession() != null && mySingleton.getPlayer() != null){
            player = mySingleton.getPlayer();
            update();
        }
    }


    private void showMediaArtwork() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] artwork = player.getMediaMetadata().artworkData;

        if (artwork != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            Drawable d = new BitmapDrawable(getResources(), bitmap);

        }
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}