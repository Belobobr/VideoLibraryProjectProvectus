package com.miiskin.videolibraryproject.content;

import android.content.Context;

/**
 * Created by ustimov.d on 13.01.2015.
 */
public interface IContentChangeObserver {

    void register(final Context context);

    void unregister(final Context context);

}
