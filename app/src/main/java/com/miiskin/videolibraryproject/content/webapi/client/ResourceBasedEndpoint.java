package com.miiskin.videolibraryproject.content.webapi.client;

import android.content.Context;
import android.support.annotation.StringRes;

import retrofit.Endpoint;

/**
 * Created by ustimov.d on 04.01.2015.
 */
public class ResourceBasedEndpoint implements Endpoint {

    private static final String DEFAULT_NAME = "default";

    @StringRes
    private int mApiUrlRes;

    private String mApiUrl;

    public ResourceBasedEndpoint(final Context context, @StringRes final int apiUrlRes) {
        invalidate(context, apiUrlRes);
    }

    @Override
    public String getName() {
        return DEFAULT_NAME;
    }

    @Override
    public String getUrl() {
        return mApiUrl;
    }

    public void invalidate(final Context context) {
        invalidate(context, mApiUrlRes);
    }

    public void invalidate(final Context context, @StringRes final int apiUrlRes) {
        mApiUrlRes = apiUrlRes;
        mApiUrl = context.getString(mApiUrlRes);
    }

}
