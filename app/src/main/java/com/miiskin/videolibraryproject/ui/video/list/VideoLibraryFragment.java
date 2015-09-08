package com.miiskin.videolibraryproject.ui.video.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.content.data.VideoInfo;
import com.miiskin.videolibraryproject.content.loader.VideoInfoListLoader;
import com.miiskin.videolibraryproject.service.VideoLibraryClientService;
import com.miiskin.videolibraryproject.ui.AbstractFragment;
import com.miiskin.videolibraryproject.ui.OnItemClickListener;
import com.miiskin.videolibraryproject.ui.video.preview.VideoPreviewFragment;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoLibraryFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<List<VideoInfo>>, OnItemClickListener{

    @Bind(R.id.recyrcle_view)
    RecyclerView mRecyclerView;

    VideoListAdapter mVideoListAdapter;

    public static VideoLibraryFragment newInstance() {
        VideoLibraryFragment videoLibraryFragment = new VideoLibraryFragment();
        Bundle arguments = new Bundle();
        videoLibraryFragment.setArguments(arguments);
        return videoLibraryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            requestVideoList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_info_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(R.id.video_info_list_loader, null, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mVideoListAdapter = new VideoListAdapter(this);
        mRecyclerView.setAdapter(mVideoListAdapter);
        setToolbarTitle();
    }

    private void setToolbarTitle() {
        if (getActivity() instanceof VideoLibraryActivity) {
            final VideoLibraryActivity videoLibraryActivity = (VideoLibraryActivity)getActivity();
            videoLibraryActivity.setToolbarTitle(getString(R.string.video_library));
        }
    }

    private void requestVideoList() {
        VideoLibraryClientService.queryVideoList(getActivity());
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new VideoInfoListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<VideoInfo>> loader, List<VideoInfo> data) {
        mVideoListAdapter.setItems(data);
    }

    @Override
    public void onLoaderReset(Loader<List<VideoInfo>> loader) {

    }

    @Override
    public void onItemClickListener(int position) {
        if (getActivity() instanceof VideoLibraryActivity) {
            final VideoLibraryActivity videoLibraryActivity = (VideoLibraryActivity)getActivity();
            videoLibraryActivity.replaceFragment(VideoPreviewFragment.newInstance(mVideoListAdapter.getItem(position).getId()), true);
        }
    }
}
