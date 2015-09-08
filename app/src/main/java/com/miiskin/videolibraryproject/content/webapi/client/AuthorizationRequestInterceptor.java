package com.miiskin.videolibraryproject.content.webapi.client;

import android.content.Context;
import android.support.annotation.StringRes;

import retrofit.RequestInterceptor;

/**
 * Created by ustimov on 01.08.2015.
 */
public class AuthorizationRequestInterceptor implements RequestInterceptor {

    @StringRes
    private int mAuthTokenRes;

    private String mAuthToken;

    public AuthorizationRequestInterceptor(final Context context, @StringRes final int authTokenRes) {
        invalidate(context, authTokenRes);
    }

    public void invalidate(final Context context) {
        invalidate(context, mAuthTokenRes);
    }

    public void invalidate(final Context context, @StringRes final int authTokenRes) {
        mAuthTokenRes = authTokenRes;
        mAuthToken = context.getString(mAuthTokenRes);
    }

    @Override
    public void intercept(final RequestInterceptor.RequestFacade request) {
        request.addHeader("Authorization", mAuthToken);
    }

}
