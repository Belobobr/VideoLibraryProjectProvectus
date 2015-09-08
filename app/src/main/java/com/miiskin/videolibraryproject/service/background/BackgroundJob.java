package com.miiskin.videolibraryproject.service.background;

import android.support.annotation.Nullable;

/**
 * Created by ustimov on 01.08.2015.
 */
public abstract class BackgroundJob<R> implements Runnable {

    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MEDIUM = 0;
    public static final int PRIORITY_HIGH = 1;

    private int mPriority;

    public BackgroundJob() {
        this(PRIORITY_MEDIUM);
    }

    public BackgroundJob(final int priority) {
        mPriority = priority;
    }

    /* package */ int getPriority() {
        return mPriority;
    }

    @Override
    public void run() {
        final R result = doInBackground();
        onCompleted(result);
    }

    @Nullable
    protected abstract R doInBackground();

    public void onCompleted(@Nullable final R result) {
    }

}
