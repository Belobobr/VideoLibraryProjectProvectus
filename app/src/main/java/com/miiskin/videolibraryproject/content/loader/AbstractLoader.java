package com.miiskin.videolibraryproject.content.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.miiskin.videolibraryproject.content.IContentChangeObserver;


public abstract class AbstractLoader<E> extends AsyncTaskLoader<E> {

    protected E mData;

    private IContentChangeObserver mObserver;

    public AbstractLoader(final Context context) {
        super(context);
    }

    @Nullable
    protected E getData() {
        return mData;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (mObserver == null) {
            mObserver = createContentObserver();
            mObserver.register(getContext());
        }

        if (mData == null || takeContentChanged()) {
            forceLoad();
        }
    }

    @NonNull
    private IContentChangeObserver createContentObserver() {
        IContentChangeObserver observer = onCreateContentObserver();
        if (observer == null) {
            observer = new DummyObserver();
        }
        return observer;
    }

    @Nullable
    protected abstract IContentChangeObserver onCreateContentObserver();

    @Override
    public void deliverResult(final E data) {
        if (data != null) {
            mData = data;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        if (mObserver != null) {
            mObserver.unregister(getContext());
            mObserver = null;
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    private static final class DummyObserver implements IContentChangeObserver {

        public void register(final Context context) {
        }

        public void unregister(final Context context) {
        }

    }

}
