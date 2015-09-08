package com.miiskin.videolibraryproject.content.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miiskin.videolibraryproject.content.dao.VideoDAO;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "miiskin.video.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        VideoDAO.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        VideoDAO.onUpgrade(db, oldVersion, newVersion);
    }

}