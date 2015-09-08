package com.miiskin.videolibraryproject.ui.video.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.content.data.VideoInfo;
import com.miiskin.videolibraryproject.content.loader.VideoInfoLoader;
import com.miiskin.videolibraryproject.ui.AbstractFragment;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoPreviewFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<VideoInfo> {

    private static final String EXTRA_VIDEO_ID = "video_id";
    private int mVideoId;
    private VideoInfo mVideoInfo;

    public static VideoPreviewFragment newInstance(final int videoId) {
        VideoPreviewFragment videoPreviewFragment = new VideoPreviewFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_VIDEO_ID, videoId);
        videoPreviewFragment.setArguments(arguments);
        return videoPreviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoId = getArguments().getInt(EXTRA_VIDEO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(R.id.video_preview_loader, VideoInfoLoader.prepareArgs(mVideoId), this);
    }

    @Override
    public Loader<VideoInfo> onCreateLoader(int id, Bundle args) {
        return new VideoInfoLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<VideoInfo> loader, VideoInfo data) {
        mVideoInfo = data;
        updateUi();
    }

    @Override
    public void onLoaderReset(Loader<VideoInfo> loader) {

    }

    private void updateUi() {
        if (getActivity() instanceof VideoLibraryActivity) {
            final VideoLibraryActivity videoLibraryActivity = (VideoLibraryActivity)getActivity();
            videoLibraryActivity.setToolbarTitle(mVideoInfo.getTitle());
        }
    }
}
