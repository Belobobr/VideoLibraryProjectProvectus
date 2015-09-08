package com.miiskin.videolibraryproject.ui.video.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.miiskin.videolibraryproject.R;
import com.miiskin.videolibraryproject.ui.AbstractActivity;

import butterknife.Bind;


public class VideoLibraryActivity extends AbstractActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        if (savedInstanceState == null) {
            replaceFragment(VideoLibraryFragment.newInstance(), false);
        }
    }

    public void replaceFragment(final Fragment fragment, boolean addToBackStack) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        if (addToBackStack) {
            ft.addToBackStack(fragment.toString());
        }
        ft.replace(R.id.content, fragment);
        ft.commit();
    }

    public void setToolbarTitle(final String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }
}
