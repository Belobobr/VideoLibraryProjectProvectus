package com.miiskin.videolibraryproject.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.miiskin.videolibraryproject.content.IContentChangeObserver;
import com.miiskin.videolibraryproject.content.dao.VideoDAO;
import com.miiskin.videolibraryproject.content.webapi.VideoLibraryRestClient;
import com.miiskin.videolibraryproject.service.background.BackgroundExecutorService;
import com.miiskin.videolibraryproject.service.background.BackgroundJob;
import com.miiskin.videolibraryproject.service.background.impl.QueryVideoList;
import com.miiskin.videolibraryproject.utils.ApplicationUtils;
import com.miiskin.videolibraryproject.utils.NetworkStateBroadcastReceiver;

/**
 * Created by ustimov on 30.07.2015.
 */
public class VideoLibraryClientService extends LiveLongAndProsperIntentService
        implements NetworkStateBroadcastReceiver.OnNetworkStateChangeListener {

    private static final String NAME = "VideoLibraryClientService";

    private static final String ACTION_QUERY_SUGGESTS = "WeatherClientService.ACTION_QUERY_SUGGESTS";
    private static final String ACTION_SWITCH_WEATHER_API_ENDPOINT = "WeatherClientService.ACTION_SWITCH_WEATHER_API_ENDPOINT";

    private static final String EXTRA_RESULT_RECEIVER = "result_receiver";

    public static void queryVideoList(final Context context) {
        final Intent intent = createActionIntent(context, ACTION_QUERY_SUGGESTS);
        context.startService(intent);
    }

    public static void switchWeatherEndpoint(final Context context) {
        final Intent intent = createActionIntent(context, ACTION_SWITCH_WEATHER_API_ENDPOINT);
        context.startService(intent);
    }

    @NonNull
    private static Intent createActionIntent(final Context context, final String action) {
        final Intent intent = new Intent(context, VideoLibraryClientService.class);
        intent.setAction(action);
        return intent;
    }

    private BackgroundExecutorService mBackgroundExecutorService;
    private IContentChangeObserver mNetworkStateBroadcastReceiver;
    private VideoLibraryRestClient mVideoLibraryRestClient;
    private VideoDAO mVideoDAO;

    public VideoLibraryClientService() {
        super(NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mBackgroundExecutorService = new BackgroundExecutorService();

        final boolean scanNetworkChanges = ApplicationUtils.hasPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        mNetworkStateBroadcastReceiver = new NetworkStateBroadcastReceiver(this, scanNetworkChanges);
        mNetworkStateBroadcastReceiver.register(this);

        mVideoLibraryRestClient = new VideoLibraryRestClient(this);
        mVideoDAO = new VideoDAO(this);
    }

    @Override
    public void onNetworkStateChanged(@Nullable final NetworkInfo networkInfo) {
        mBackgroundExecutorService.adjustThreadCount(networkInfo);
    }

    @Override
    public void onDestroy() {
        mBackgroundExecutorService.shutdown();
        mNetworkStateBroadcastReceiver.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@NonNull final Intent intent) {
        final String action = intent.getAction();
        if (ACTION_QUERY_SUGGESTS.equals(action)) {
            onQuerySuggestsAction();
        } else if (ACTION_SWITCH_WEATHER_API_ENDPOINT.equals(action)) {
            onSwitchWeatherApiEndpoint();

        }
    }

    private void onQuerySuggestsAction() {
        final BackgroundJob<?> job = new QueryVideoList(this, mVideoLibraryRestClient, mVideoDAO);
        mBackgroundExecutorService.submit(job);
    }

    private void onSwitchWeatherApiEndpoint() {
        mVideoLibraryRestClient.switchWeatherApiEndpoint(this);
    }

}
