package com.jtinteractive.notspotify;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.CommandButton;
import androidx.media3.session.DefaultMediaNotificationProvider;
import androidx.media3.session.MediaNotification;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaStyleNotificationHelper;

import com.google.common.collect.ImmutableList;
@UnstableApi public class MyMediaNotificationProvider implements MediaNotification.Provider {

    private Context context;
    public MyMediaNotificationProvider(Context context) {
        this.context = context;
    }

    @Override
    public MediaNotification createNotification(
            MediaSession mediaSession,
            ImmutableList<CommandButton> customLayout,
            MediaNotification.ActionFactory actionFactory,
            Callback onNotificationChangedCallback) {

        // Create the notification
        return new DefaultMediaNotificationProvider(context).createNotification(
                mediaSession,
                customLayout,
                actionFactory,
                onNotificationChangedCallback);
    }

    @Override
    public boolean handleCustomCommand(
            MediaSession session,
            String action,
            Bundle extras) {
        return false;
    }
}