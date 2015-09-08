package com.miiskin.videolibraryproject.content;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by ustimov.d on 15.05.2015.
 */
public class ContentProviderChangeObserver extends ContentObserver
        implements IContentChangeObserver {

    private static final String LOG_TAG = "ContentChangeObserver";

    private final Loader<?> mLoader;
    private final Uri mUri;

    public ContentProviderChangeObserver(final Loader<?> loader, final Uri uri) {
        super(new Handler());
        mLoader = loader;
        mUri = uri;
    }

    @Override
    public void register(final Context context) {
        Log.i(LOG_TAG, "Register content observer on uri=" + mUri);
        context.getContentResolver().registerContentObserver(mUri, true, this);
    }

    @Override
    public void unregister(final Context context) {
        Log.i(LOG_TAG, "Unregister content observer on uri=" + mUri);
        context.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(final boolean selfChange) {
        Log.i(LOG_TAG, "Uri=" + mUri + " has been changed.");
        super.onChange(selfChange);
        mLoader.onContentChanged();
    }

}
