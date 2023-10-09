package com.jtinteractive.notspotify;

import android.content.Context;
import android.os.Bundle;

import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;

import java.util.function.Consumer;

public class MySingleton {
    private static MySingleton instance;
    private ExoPlayer player = null;
    private MediaSession mediaSession = null;

    private Consumer<Integer> consumer;
    private MySingleton() {

    }
    public void InitData(Context context, MediaSession.Callback callback, Bundle extras, Consumer<Integer> consumer) {
        player = new ExoPlayer.Builder(context).build();
        mediaSession = new MediaSession.Builder(context, player).setCallback(callback).setId(String.valueOf(System.currentTimeMillis())).setExtras(extras).build();
        this.consumer = consumer;
    }

    public static synchronized MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ExoPlayer player) {
        this.player = player;
    }

    public MediaSession getMediaSession() {
        return mediaSession;
    }

    public void setMediaSession(MediaSession mediaSession) {
        this.mediaSession = mediaSession;
    }

    public Consumer<Integer> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<Integer> consumer) {
        this.consumer = consumer;
    }
}