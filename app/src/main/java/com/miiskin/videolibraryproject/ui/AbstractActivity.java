package com.miiskin.videolibraryproject.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Newshka on 08.09.2015.
 */
public class AbstractActivity extends AppCompatActivity{

    public void setContentView(@LayoutRes final int layoutResId) {
        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

    public void setContentView(final View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }
}
