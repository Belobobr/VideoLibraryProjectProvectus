package com.miiskin.videolibraryproject.content.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.miiskin.videolibraryproject.content.dao.VideoDAO;
import com.miiskin.videolibraryproject.content.data.VideoInfo;

import java.util.List;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoInfoLoader extends AbstractContentProviderLoader<VideoInfo>  {

    private static final String EXTRA_VIDEO_ID = "video_id";

    public static Bundle prepareArgs(final int videoId) {
        final Bundle args = new Bundle();
        args.putInt(EXTRA_VIDEO_ID, videoId);
        return args;
    }

    private VideoDAO mVideoDAO;
    private final int mVideoId;

    public VideoInfoLoader(final Context context, @NonNull final Bundle args) {
        super(context, VideoDAO.CONTENT_URI);
        mVideoDAO = new VideoDAO(context);
        mVideoId = args.getInt(EXTRA_VIDEO_ID);
    }

    @Override
    public VideoInfo loadInBackground() {
        return mVideoDAO.get(mVideoId);
    }
}
