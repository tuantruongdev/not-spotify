package com.jtinteractive.notspotify;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.CommandButton;
import androidx.media3.session.MediaConstants;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.MediaStyleNotificationHelper;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionCommands;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;

public class PlaybackService extends MediaSessionService {
    NotificationCompat.Builder builder = null;
    private ExoPlayer player = null;
    private MediaSession mediaSession = null;
    private final MediaSession.Callback callback = new MyCallback();
    private  Context context;
    private final int NOTIFICATION_ID = 200;
    private final String NOTIFICATION_CHANNEL_NAME = "notification channel 1";
    private final String NOTIFICATION_CHANNEL_ID = "notification channel id 1";


    public PlaybackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        //player = new ExoPlayer.Builder(context).build();

        // Create a MediaSession
        Bundle extras = new Bundle();
        extras.putBoolean(MediaConstants.EXTRAS_KEY_SLOT_RESERVATION_SEEK_TO_NEXT, true);
        //mediaSession = new MediaSession.Builder(context, player).setCallback(callback).setId("my media session").setExtras(extras).build();
        MySingleton.getInstance().InitData(context,callback,extras);
        player = MySingleton.getInstance().getPlayer();
        mediaSession = MySingleton.getInstance().getMediaSession();
        Uri mp3 = Uri.parse("http://localhost:8080/song/402/stream");
        MediaItem mediaItem = MediaItem.fromUri(mp3);
        // Set the media item to be played.
        player.setMediaItem(mediaItem);
        // loop mode
        player.setRepeatMode(player.REPEAT_MODE_ALL);
        // Prepare the player.
        player.prepare();
        // Play
        player.setPlayWhenReady(true);

        setMediaNotificationProvider(new MyMediaNotificationProvider(getApplicationContext()));
        createNotificationChannel();
        startForegroundNotification(this);
    }

    private void startForegroundNotification(MediaSessionService mediaSessionService) {
        Notification notificationCompat = builder.build();
        mediaSessionService.startForeground(NOTIFICATION_ID, notificationCompat);

    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("title").setContentText("text").setPriority(NotificationManager.IMPORTANCE_DEFAULT).setContentIntent(pendingIntent).setFullScreenIntent(pendingIntent, true).setStyle(new MediaStyleNotificationHelper.MediaStyle(mediaSession));
    }

    @Override
    public void onDestroy() {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    public class MyCallback implements MediaSession.Callback {
        @Override
        public MediaSession.ConnectionResult onConnect(MediaSession session, MediaSession.ControllerInfo controller) {
            MediaSession.ConnectionResult connectionResult = MediaSession.Callback.super.onConnect(session, controller);
            SessionCommands sessionCommands = connectionResult.availableSessionCommands.buildUpon()
                    // Add custom commands
                    .add(new SessionCommand("SAVE_TO_FAVORITES", new Bundle())).build();
            return MediaSession.ConnectionResult.accept(sessionCommands, connectionResult.availablePlayerCommands);
        }


        @Override
        public void onPostConnect(MediaSession session, MediaSession.ControllerInfo controller) {
            // Display a button for the custom command
            CommandButton favoriteButton = new CommandButton.Builder().setDisplayName("Save to favorites").setIconResId(R.drawable.ic_launcher_foreground).setSessionCommand(new SessionCommand("SAVE_TO_FAVORITES", new Bundle())).build();
            CommandButton likeButton = new CommandButton.Builder().setDisplayName("Like").setIconResId(R.drawable.frame_27758).setSessionCommand(new SessionCommand(SessionCommand.COMMAND_CODE_SESSION_SET_RATING)).build();
            CommandButton playPauseButton = new CommandButton.Builder().setDisplayName(session.getPlayer().isPlaying() ? "Play" : "Pause").setIconResId(session.getPlayer().isPlaying() ? R.drawable.mask_group_5 : R.drawable.frame_27758).setPlayerCommand(Player.COMMAND_PLAY_PAUSE).build();

            session.setCustomLayout(
//                    controller,
                    ImmutableList.of(/*playPauseButton, likeButton,*/favoriteButton));
            MediaSession.Callback.super.onPostConnect(session, controller);
        }

        @Override
        public int onPlayerCommandRequest(MediaSession session, MediaSession.ControllerInfo controller, int playerCommand) {
            return MediaSession.Callback.super.onPlayerCommandRequest(session, controller, playerCommand);
        }

        @Override
        public ListenableFuture onCustomCommand(MediaSession session, MediaSession.ControllerInfo controller, SessionCommand customCommand, Bundle args) {
            if (customCommand.customAction.equals("SAVE_TO_FAVORITES")) {
                Log.d("truong1337", "fav");
//                 Do custom logic here
//                saveToFavorites(session.getPlayer().getCurrentMediaItem());
//                return Futures.immediateFuture(
//                        new SessionResult(SessionResult.RESULT_SUCCESS)
//                );
            }

            return MediaSession.Callback.super.onCustomCommand(session, controller, customCommand, args);
        }

    }
}