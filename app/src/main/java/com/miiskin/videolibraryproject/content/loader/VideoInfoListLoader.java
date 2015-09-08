package com.miiskin.videolibraryproject.content.loader;

import android.content.Context;

import com.miiskin.videolibraryproject.content.dao.VideoDAO;
import com.miiskin.videolibraryproject.content.data.VideoInfo;

import java.util.List;


/**
 * Created by Sednev Michail on 15.08.2015.
 */
public class VideoInfoListLoader extends AbstractContentProviderLoader<List<VideoInfo>> {

    private VideoDAO mVideoDAO;

    public VideoInfoListLoader(final Context context) {
        super(context, VideoDAO.CONTENT_URI);
        mVideoDAO = new VideoDAO(context);
    }

    @Override
    public List<VideoInfo> loadInBackground() {
        return mVideoDAO.getAll();
    }
}
