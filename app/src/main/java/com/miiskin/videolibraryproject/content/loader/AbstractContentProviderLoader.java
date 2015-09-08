package com.miiskin.videolibraryproject.content.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.miiskin.videolibraryproject.content.ContentProviderChangeObserver;
import com.miiskin.videolibraryproject.content.IContentChangeObserver;

public abstract class AbstractContentProviderLoader<E> extends AbstractLoader<E> {

    private final Uri mUri;

    public AbstractContentProviderLoader(final Context context, final Uri uri) {
        super(context);
        mUri = uri;
    }

    @Nullable
    @Override
    public IContentChangeObserver onCreateContentObserver() {
        return new ContentProviderChangeObserver(this, mUri);
    }

}