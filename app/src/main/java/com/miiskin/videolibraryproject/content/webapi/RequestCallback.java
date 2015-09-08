package com.miiskin.videolibraryproject.content.webapi;

import android.util.Log;

import com.miiskin.videolibraryproject.BuildConfig;
import com.miiskin.videolibraryproject.Config;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created on 03.07.2015.
 */
public class RequestCallback<T> implements Callback<T> {

    @Override
    public void success(T t, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {
        if (BuildConfig.DEBUG) {
            Log.d(Config.LOG_TAG, "Got response error: " + error.getMessage());
        }
    }
}
