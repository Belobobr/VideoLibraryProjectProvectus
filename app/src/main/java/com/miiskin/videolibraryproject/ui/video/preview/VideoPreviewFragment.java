package com.miiskin.videolibraryproject.ui.video.preview;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.content.data.VideoInfo;
import com.miiskin.videolibraryproject.content.loader.VideoInfoLoader;
import com.miiskin.videolibraryproject.ui.AbstractFragment;
import com.miiskin.videolibraryproject.ui.video.list.VideoLibraryActivity;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoPreviewFragment extends AbstractFragment implements LoaderManager.LoaderCallbacks<VideoInfo>, SurfaceHolder.Callback{

    private static final String EXTRA_VIDEO_ID = "video_id";
    private int mVideoId;
    private VideoInfo mVideoInfo;
    Uri mTargetUri;

    MediaPlayer mMediaPlayer;
    SurfaceHolder mSurfaceHolder;
    boolean mPaus = false;;

    @Bind(R.id.description)
    TextView mTextView;
    @Bind(R.id.play_video_player)
    Button mButtonPlayVideo;
    @Bind(R.id.pause_video_player)
    Button mButtonPauseVideo;
    @Bind(R.id.surface_view)
    SurfaceView mSurfaceView;

    public static VideoPreviewFragment newInstance(final int videoId) {
        VideoPreviewFragment videoPreviewFragment = new VideoPreviewFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_VIDEO_ID, videoId);
        videoPreviewFragment.setArguments(arguments);
        return videoPreviewFragment;
    }

    @OnClick(R.id.play_video_player)
    public void onPlayVideo() {
        mPaus = false;

        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(mSurfaceHolder);

        try {
            mMediaPlayer.setDataSource(getActivity().getApplicationContext(), mTargetUri);
            mMediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.start();
    }

    @OnClick(R.id.pause_video_player)
    public void onPauseVideo() {
        if(mPaus){
            mPaus = false;
            mMediaPlayer.start();
        }
        else{
            mPaus = true;
            mMediaPlayer.pause();
        }
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

        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFixedSize(640, 480);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public Loader<VideoInfo> onCreateLoader(int id, Bundle args) {
        return new VideoInfoLoader(getActivity(), args);
    }

    @Override
    public void onLoadFinished(Loader<VideoInfo> loader, VideoInfo data) {
        mVideoInfo = data;
        mTargetUri = Uri.parse(mVideoInfo.getVideo());
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
        mTextView.setText(mVideoInfo.getOverview());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }
}
