package com.miiskin.videolibraryproject.content.webapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.content.webapi.client.DebugLog;
import com.miiskin.videolibraryproject.content.webapi.client.ResourceBasedEndpoint;
import com.miiskin.videolibraryproject.content.webapi.client.VideoErrorHandler;
import com.miiskin.videolibraryproject.content.webapi.client.VideoJsonConverter;
import com.miiskin.videolibraryproject.content.webapi.client.WeatherOkHttpClient;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.Converter;

/**
 * Created by ustimov on 01.08.2015.
 */
public class VideoLibraryRestClient {

    private static final String LOG_TAG = "WeatherRestClient";

    private final VideoApi mVideoApi;

    private final ResourceBasedEndpoint mVideoApiEndpoint;

    public VideoLibraryRestClient(final Context context) {

        final Context appContext = context.getApplicationContext();

        final Client client = WeatherOkHttpClient.create(appContext);
        final RestAdapter.Log log = new DebugLog(LOG_TAG);
        final ErrorHandler errorHandler = new VideoErrorHandler();
        final Converter converter = VideoJsonConverter.create();

        mVideoApiEndpoint = new ResourceBasedEndpoint(appContext, getVideoApiRes());
        final RestAdapter videoRestAdapter = new RestAdapter.Builder()
                .setEndpoint(mVideoApiEndpoint).setClient(client).setErrorHandler(errorHandler)
                .setConverter(converter)
                .setLog(log).setLogLevel(DebugLog.LOG_LEVEL)
                .build();
        mVideoApi = videoRestAdapter.create(VideoApi.class);
    }

    public void switchWeatherApiEndpoint(final Context context) {
        final Context appContext = context.getApplicationContext();
        mVideoApiEndpoint.invalidate(appContext, getVideoApiRes());
    }

    @StringRes
    private int getVideoApiRes() {
        return R.string.end_point_video; // single api
    }

    @NonNull
    public VideoApi getVideoApi() {
        return mVideoApi;
    }

    @NonNull
    public String getVideoSource() {
        return mVideoApiEndpoint.getName();
    }

}