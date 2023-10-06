package com.jtinteractive.notspotify;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.CommandButton;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionCommands;
import androidx.media3.session.SessionResult;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

class PlaybackService extends MediaSessionService {
    private ExoPlayer player = null;
    private MediaSession mediaSession = null;
    private MediaSession.Callback callback = new MyCallback();

    @Override
    public void onCreate() {
        super.onCreate();
        player = new ExoPlayer.Builder(this).build();
        // Create a MediaSession
        mediaSession = new MediaSession.Builder(this, player).setCallback(callback).build();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return null;
    }

    private class MyCallback implements MediaSession.Callback {
        @Override
        public MediaSession.ConnectionResult onConnect(
                MediaSession session,
                MediaSession.ControllerInfo controller
        ) {
            MediaSession.ConnectionResult connectionResult =
                    MediaSession.Callback.super.onConnect(session, controller);
            SessionCommands sessionCommands = connectionResult.availableSessionCommands
                    .buildUpon()
                    // Add custom commands
                    .add(new SessionCommand("SAVE_TO_FAVORITES", new Bundle()))
                    .build();
            return MediaSession.ConnectionResult.accept(
                    sessionCommands, connectionResult.availablePlayerCommands
            );
        }

        @Override
        public void onPostConnect(
                MediaSession session,
                MediaSession.ControllerInfo controller
        ) {
            // Display a button for the custom command
            CommandButton favoriteButton = new CommandButton.Builder()
                    .setDisplayName("Save to favorites")
                    .setIconResId(R.drawable.ic_launcher_background)
                    .setSessionCommand(new SessionCommand("SAVE_TO_FAVORITES", new Bundle()))
                    .build();
            session.setCustomLayout(
                    controller,
                    ImmutableList.of(/*playPauseButton, likeButton,*/ favoriteButton)
            );

            MediaSession.Callback.super.onPostConnect(session, controller);
        }

        @Override
        public ListenableFuture onCustomCommand(
                MediaSession session,
                MediaSession.ControllerInfo controller,
                SessionCommand customCommand,
                Bundle args
        ) {
            if(customCommand.customAction == "SAVE_TO_FAVORITES") {
                // Do custom logic here
//                saveToFavorites(session.getPlayer().getCurrentMediaItem());
//                return Futures.immediateFuture(
//                        new SessionResult(SessionResult.RESULT_SUCCESS)
//                );
            }

          return MediaSession.Callback.super.onCustomCommand(session, controller, customCommand, args);
        }
    }
}