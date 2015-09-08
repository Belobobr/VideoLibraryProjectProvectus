package com.miiskin.videolibraryproject.content.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.miiskin.videolibraryproject.content.DatabaseUtils;
import com.miiskin.videolibraryproject.content.data.Genre;
import com.miiskin.videolibraryproject.content.data.VideoInfo;

/**
 * Created by Newshka on 08.09.2015.
 */
public class VideoDAO extends  AbstractDAO<VideoInfo>{

    public static final String LOG_TA = "VideoDAO";

    public static final String TABLE = "video";

    public interface Columns extends BaseColumns {
        String ADULT = "adult";
        String GENRES = "genres";
        String ORIGINAL_LANGUAGE = "original_language";
        String ORIGINAL_TITLE = "original_title";
        String OVERVIEW = "overview";
        String RELEASE_DATE = "release_date";
        String POSTER_PATH = "poster_path";
        String POPULARITY = "popularity";
        String TITLE = "title";
        String VIDEO = "video";
        String VOTE_AVERAGE = "vote_average";
        String VOTE_COUNT = "vote_count";
    }

    public static final Uri CONTENT_URI = DatabaseUtils.getUri(TABLE);

    public static final String[] PROJECTION =  {
            Columns._ID,
            Columns.ADULT,
            Columns.GENRES,
            Columns.ORIGINAL_LANGUAGE,
            Columns.ORIGINAL_TITLE,
            Columns.OVERVIEW,
            Columns.RELEASE_DATE,
            Columns.POSTER_PATH,
            Columns.POPULARITY,
            Columns.TITLE,
            Columns.VIDEO,
            Columns.VOTE_AVERAGE,
            Columns.VOTE_COUNT
    };

    public static void onCreate(final SQLiteDatabase db) {
        final DatabaseUtils.TableBuilder tableBuilder = new DatabaseUtils.TableBuilder(TABLE);
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns._ID));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.ADULT));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.GENRES));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.ORIGINAL_LANGUAGE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.ORIGINAL_TITLE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.OVERVIEW));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.RELEASE_DATE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.POSTER_PATH));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().real(Columns.POPULARITY));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.TITLE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().text(Columns.VIDEO));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().real(Columns.VOTE_AVERAGE));
        tableBuilder.addColumn(new DatabaseUtils.ColumnBuilder().integer(Columns.VOTE_COUNT));
        tableBuilder.create(db);

        DatabaseUtils.createIndex(db, TABLE, Columns._ID, new String[] { Columns._ID });
    }

    public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        DatabaseUtils.dropTable(db, TABLE);
        onCreate(db);
    }

    public VideoDAO(Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected Uri getTableUri() {
        return CONTENT_URI;
    }

    @Nullable
    @Override
    protected String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    protected VideoInfo getItemFromCursor(Cursor cursor) {
        final VideoInfo videoInfo = new VideoInfo();
        videoInfo.setId(getId(cursor));
        videoInfo.setAdult(getBoolean(cursor, Columns.ADULT));
        String[] genresStrings = getString(cursor, Columns.GENRES).split(",");
        Genre[] genres = new Genre[genresStrings.length];
        for (int i = 0; i < genres.length; i++) {
            genres[i] = new Genre();
        }
        for (int i = 0; i < genresStrings.length; i++) {
            genres[i].setName(genresStrings[i]);
        }
        videoInfo.setGenres(genres);
        videoInfo.setOriginalLanguage(getString(cursor, Columns.ORIGINAL_LANGUAGE));
        videoInfo.setOriginalTitle(getString(cursor, Columns.ORIGINAL_TITLE));
        videoInfo.setOverview(getString(cursor, Columns.OVERVIEW));
        videoInfo.setReleaseDate(getString(cursor, Columns.RELEASE_DATE));
        videoInfo.setPosterPath(getString(cursor, Columns.POSTER_PATH));
        videoInfo.setPopularity(getDouble(cursor, Columns.POPULARITY));
        videoInfo.setTitle(getString(cursor, Columns.TITLE));
        videoInfo.setVideo(getString(cursor, Columns.VIDEO));
        videoInfo.setVoteAverage(getDouble(cursor, Columns.VOTE_AVERAGE));
        videoInfo.setVoteCount(getLong(cursor, Columns.VOTE_COUNT));
        return videoInfo;
    }

    @NonNull
    @Override
    protected ContentValues toContentValues(@NonNull VideoInfo videoInfo) {
        ContentValues contentValues = new ContentValues();
        final int id = videoInfo.getId();
        if (id != Identify.INVALID_ID) {
            contentValues.put(Columns._ID, id);
        }
        contentValues.put(Columns.ADULT, videoInfo.getAdult());
        contentValues.put(Columns.ORIGINAL_LANGUAGE, videoInfo.getOriginalLanguage());
        contentValues.put(Columns.GENRES, createCSVStringFromGenres(videoInfo.getGenres()));
        contentValues.put(Columns.ORIGINAL_TITLE, videoInfo.getOriginalTitle());
        contentValues.put(Columns.OVERVIEW, videoInfo.getOverview());
        contentValues.put(Columns.RELEASE_DATE, videoInfo.getReleaseDate());
        contentValues.put(Columns.POSTER_PATH, videoInfo.getPosterPath());
        contentValues.put(Columns.POPULARITY, videoInfo.getPopularity());
        contentValues.put(Columns.TITLE, videoInfo.getTitle());
        contentValues.put(Columns.VIDEO, videoInfo.getVideo());
        contentValues.put(Columns.VOTE_AVERAGE, videoInfo.getVoteAverage());
        contentValues.put(Columns.VOTE_COUNT, videoInfo.getVoteCount());
        return contentValues;
    }

    private String createCSVStringFromGenres(Genre[] genres) {
        if (genres.length > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (Genre genre : genres) {
                if (genre != null) {
                    nameBuilder.append("'").append(genre.getName().replace("'", "\\'")).append("',");
                }
                // can also do the following
                // nameBuilder.append("'").append(n.replace("'", "''")).append("',");
            }

            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }
}
