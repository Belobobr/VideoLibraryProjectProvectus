package com.miiskin.videolibraryproject.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.miiskin.videolibraryproject.content.IContentChangeObserver;


/**
 * Created by ustimov.d on 28.04.2015.
 */
public class NetworkStateBroadcastReceiver extends BroadcastReceiver implements IContentChangeObserver {

    public static interface OnNetworkStateChangeListener {

        void onNetworkStateChanged(@Nullable final NetworkInfo networkInfo);

    }

    private static final String EXTRA_AIRPLANE_STATE = "state";

    private final OnNetworkStateChangeListener mOnNetworkStateChangeListener;
    private final boolean mScansNetworkChanges;

    private boolean mRegistered;

    public NetworkStateBroadcastReceiver(final OnNetworkStateChangeListener networkStateChangeListener,
                                         final boolean scansNetworkChanges) {
        mOnNetworkStateChangeListener = networkStateChangeListener;
        mScansNetworkChanges = scansNetworkChanges;
    }

    @Override
    public void register(final Context context) {
        if (!mRegistered) {
            final IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            if (mScansNetworkChanges) {
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            }
            context.registerReceiver(this, filter);

            mRegistered = true;
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // On some versions of Android this may be called with a null Intent,
        // also without extras (getExtras() == null), in such case we use defaults.
        if (intent == null) {
            return;
        }

        final String action = intent.getAction();
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
            //if (!intent.hasExtra(EXTRA_AIRPLANE_STATE)) {
            //	return; // No airplane state, ignore it. Should we query Utils.isAirplaneModeOn?
            //}
            // TODO: dispatcher.dispatchAirplaneModeChange(intent.getBooleanExtra(EXTRA_AIRPLANE_STATE, false));

        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            mOnNetworkStateChangeListener.onNetworkStateChanged(connectivityManager.getActiveNetworkInfo());
        }
    }

    @Override
    public void unregister(final Context context) {
        if (mRegistered) {
            context.unregisterReceiver(this);
            mRegistered = false;
        }
    }

}
