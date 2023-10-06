package com.jtinteractive.notspotify;

import static androidx.media3.ui.PlayerView.ARTWORK_DISPLAY_MODE_FIT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.Tracks;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultDataSourceFactory;
import androidx.media3.datasource.cache.CacheDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize ExoPlayer
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,"test");
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri("http://localhost:8080/song/353/stream"));
        player = new ExoPlayer.Builder(getApplicationContext()).build();
        playerView = findViewById(R.id.player_view);
        playerView.setPlayer(player);//todo
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(true);
        player.addListener(new Player.Listener() {
            @Override
            public void onTracksChanged(Tracks tracks) {
                Player.Listener.super.onTracksChanged(tracks);
          //      showMediaArtwork();
            }
        });
        playerView.setControllerShowTimeoutMs(0);
        playerView.setControllerHideOnTouch(false);

    }
    private void showMediaArtwork() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        byte[] artwork = player.getMediaMetadata().artworkData;

        if (artwork != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            playerView.setDefaultArtwork(d);
            playerView.setArtworkDisplayMode(ARTWORK_DISPLAY_MODE_FIT);
        }
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}