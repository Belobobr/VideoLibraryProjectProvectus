package com.miiskin.videolibraryproject.content;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.Loader;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ustimov.d on 15.05.2015.
 */
public class MultipleUrisChangeObserver implements IContentChangeObserver {

    private final Loader<?> mLoader;
    private final Map<Uri, IContentChangeObserver> mContentChangeObservers;

    public MultipleUrisChangeObserver(final Loader<?> loader) {
        mLoader = loader;
        mContentChangeObservers = new LinkedHashMap<Uri, IContentChangeObserver>();
    }

    public void add(final Uri contentUri) {
        mContentChangeObservers.put(contentUri,
                new ContentProviderChangeObserver(mLoader, contentUri));
    }

    @Override
    public void register(final Context context) {
        final Collection<IContentChangeObserver> observers = mContentChangeObservers.values();
        for (final IContentChangeObserver contentChangeObserver : observers) {
            contentChangeObserver.register(context);
        }
    }

    @Override
    public void unregister(final Context context) {
        final Collection<IContentChangeObserver> observers = mContentChangeObservers.values();
        for (final IContentChangeObserver contentChangeObserver : observers) {
            contentChangeObserver.unregister(context);
        }
    }

}
