package com.miiskin.videolibraryproject.content.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.miiskin.videolibraryproject.content.DatabaseUtils;
import com.miiskin.videolibraryproject.content.dao.VideoDAO;

public class VideoLibraryContentProvider extends ContentProvider {

    private static final int MATCH_DIR = 1;
    private static final int MATCH_ID = 2;

    private static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    // Change this to add new tables to content provider
    static {
        addUri(URI_MATCHER, VideoDAO.TABLE, true);
    }

    private static void addUri(final UriMatcher matcher, final String table, final boolean hasChild) {
        matcher.addURI(DatabaseUtils.AUTHORITY, table, MATCH_DIR);
        if (hasChild) {
            matcher.addURI(DatabaseUtils.AUTHORITY, table + "/#", MATCH_ID);
        }
    }

    private DatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return "vnd.android.cursor.dir/vnd." + uri.getAuthority() + "." + getTableFromUri(uri);

            case MATCH_ID:
                return "vnd.android.cursor.dir/item." + uri.getAuthority() + "." + getTableFromUri(uri);

            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private String getTableFromUri(final Uri uri) {
        return uri.getPathSegments().get(0);
    }

    @Override
    public Cursor query(final Uri uri, final String[] projection, final String where,
                        final String[] whereArgs, final String sort) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return withNotificationUri(uri, query(getTableFromUri(uri), projection, where, whereArgs, sort));

            case MATCH_ID:
                return withNotificationUri(uri, queryById(getTableFromUri(uri), projection, uri.getLastPathSegment()));

            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private Cursor query(final String table, final String[] projection, final String where,
                         final String[] whereArgs, final String sort) {
        return mDatabaseHelper.getReadableDatabase().query(table, projection, where, whereArgs, null, null, sort);
    }

    private Cursor queryById(final String table, final String[] projection, final String id) {
        return mDatabaseHelper.getReadableDatabase().query(
                table, projection, BaseColumns._ID + "=?", new String[] { id },
                null, null, null
        );
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return notifyInsert(uri, insert(getTableFromUri(uri), values));

            case MATCH_ID:
                notifyChange(uriWithoutId(uri), updateById(getTableFromUri(uri), uri.getLastPathSegment(), values));
                return uri;

            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private long insert(final String table, final ContentValues values) {
        final long rowId = mDatabaseHelper.getWritableDatabase().insert(table, null, values);
        if (rowId <= 0) {
            throw new SQLiteException("Failed to insert row into table '" + table + "'");
        }
        return rowId;
    }

    @Override
    public int bulkInsert(final Uri uri, @NonNull final ContentValues[] values) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return notifyChange(uri, bulkInsert(getTableFromUri(uri), values));

            case MATCH_ID:
            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private int bulkInsert(final String table, final ContentValues[] bulkValues) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (final ContentValues values : bulkValues) {
                final String id = values.getAsString(BaseColumns._ID);
                if (TextUtils.isEmpty(id) || updateById(table, id, values) <= 0) {
                    insert(table, values);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return bulkValues.length;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String where, final String[] whereArgs) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return notifyChange(uri, update(getTableFromUri(uri), values, where, whereArgs));

            case MATCH_ID:
                return notifyChange(uri, updateById(getTableFromUri(uri), uri.getLastPathSegment(), values));

            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private int update(final String table, final ContentValues values,
                       final String where, final String[] whereArgs) {
        return mDatabaseHelper.getWritableDatabase().update(table, values, where, whereArgs);
    }

    private int updateById(final String table, final String id, final ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int affectedRows = db.update(table, values, BaseColumns._ID + "=?", new String[] { id });
        if (affectedRows < 1) {
            if (db.insert(table, null, values) > 0) {
                ++affectedRows;
            }
        }
        return affectedRows;
    }

    @Override
    public int delete(final Uri uri, final String where, final String[] whereArgs) {
        switch (URI_MATCHER.match(uri)) {
            case MATCH_DIR:
                return notifyChange(uri, delete(getTableFromUri(uri), where, whereArgs));

            case MATCH_ID:
                return notifyChange(uri, deleteById(getTableFromUri(uri), uri.getLastPathSegment()));

            default:
                throw new SQLiteException("Unknown uri: " + uri);
        }
    }

    private int delete(final String table, final String where, final String[] whereArgs) {
        return mDatabaseHelper.getWritableDatabase().delete(table, where, whereArgs);
    }

    private int deleteById(final String table, final String id) {
        return mDatabaseHelper.getWritableDatabase().delete(table, BaseColumns._ID + "=?", new String[] { id });
    }

    private Cursor withNotificationUri(final Uri uri, final Cursor cursor) {
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Uri notifyInsert(final Uri uri, final long rowId) {
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    private int notifyChange(final Uri uri, final int affectedRows) {
        if (affectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

    private Uri uriWithoutId(final Uri uri) {
        return Uri.parse(uri.getScheme() + "://" + uri.getAuthority() + "/" + getTableFromUri(uri));
    }

}

