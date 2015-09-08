package com.miiskin.videolibraryproject.content.webapi.client;

import android.support.annotation.NonNull;
import android.util.Log;

import com.miiskin.videolibraryproject.BuildConfig;

import retrofit.RestAdapter;

/**
 * Created by ustimov on 01.08.2015.
 */
public class DebugLog implements RestAdapter.Log {

    public static final RestAdapter.LogLevel LOG_LEVEL = (BuildConfig.DEBUG ?
            RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC);

    private final String mLogTag;

    public DebugLog(@NonNull final String logTag) {
        mLogTag = logTag;
    }

    @Override
    public void log(final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(mLogTag, message);
        }
    }

}
