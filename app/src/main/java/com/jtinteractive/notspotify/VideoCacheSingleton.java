package com.jtinteractive.notspotify;

import android.content.Context;


import androidx.media3.database.ExoDatabaseProvider;
import androidx.media3.datasource.cache.Cache;
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor;
import androidx.media3.datasource.cache.SimpleCache;

import java.io.File;

public class VideoCacheSingleton {
    private static final int MAX_VIDEO_CACHE_SIZE_IN_BYTES = 200 * 1024 * 1024;  // 200MB

    private static Cache sInstance;

    public static Cache getInstance(Context context) {
        if (sInstance != null) return sInstance;
        else return sInstance = new SimpleCache(new File(context.getCacheDir(), "video"), new LeastRecentlyUsedCacheEvictor(MAX_VIDEO_CACHE_SIZE_IN_BYTES), new ExoDatabaseProvider(context));
    }
}
