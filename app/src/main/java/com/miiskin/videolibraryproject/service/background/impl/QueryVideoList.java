package com.miiskin.videolibraryproject.service.background.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.miiskin.videolibraryproject.content.dao.VideoDAO;
import com.miiskin.videolibraryproject.content.data.VideoInfoList;
import com.miiskin.videolibraryproject.content.webapi.VideoLibraryRestClient;
import com.miiskin.videolibraryproject.content.webapi.client.VideoErrorHandler;
import com.miiskin.videolibraryproject.service.background.BackgroundJob;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ustimov on 01.08.2015.
 */
public class QueryVideoList extends BackgroundJob<VideoInfoList> {

    private static final String LOG_TAG = "QuerySuggestsJob";

    private final Context mContext;
    private final VideoDAO mVideoDAO;
    private final VideoLibraryRestClient mVideoLibraryRestClient;

    public QueryVideoList(final Context context, final VideoLibraryRestClient videoLibraryRestClient,
                          @NonNull final VideoDAO videoDAO) {
        super();
        mContext = context;
        mVideoLibraryRestClient = videoLibraryRestClient;
        mVideoDAO = videoDAO;
    }

    @Nullable
    @Override
    protected VideoInfoList doInBackground() {
        VideoInfoList videoInfoList = null;
        int errorCode;
        try {
            videoInfoList = mVideoLibraryRestClient.getVideoApi().getVideoInfoList();
            mVideoDAO.bulkInsert(Arrays.asList(videoInfoList.mVideoInfo));
            errorCode = VideoErrorHandler.ERROR_CODE_OK;
        } catch (final VideoErrorHandler.RequestException re) {
            Log.e(LOG_TAG, re.getMessage());
            errorCode = re.getCode();
        }
        return videoInfoList;
    }

    @Override
    public void onCompleted(@Nullable final VideoInfoList suggests) {
        super.onCompleted(suggests);
        if (suggests != null) {
            //SuggestsReceiver.onSuggestsLoaded(mContext, suggests);
        }
    }

}
