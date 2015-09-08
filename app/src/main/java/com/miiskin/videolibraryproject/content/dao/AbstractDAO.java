package com.miiskin.videolibraryproject.content.dao;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.miiskin.videolibraryproject.content.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ustimov.d on 03.01.2015.
 */
public abstract class AbstractDAO<E extends Identify> {

    public static final String JOIN_DELIMITER = ",";

    public static boolean isCursorValid(final Cursor cursor) {
        return (cursor != null);
    }

    public static String[] prepareArguments(final Object... args) {
        String[] params = { };
        if (args != null) {
            params = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                params[i] = String.valueOf(args[i]);
            }
        }
        return params;
    }

    public static int getId(@Nullable final Uri uri) {
        return (uri == null ? Identify.INVALID_ID : (int) ContentUris.parseId(uri));
    }

    public static int getId(final Cursor c) {
        final Integer id = getNullableInteger(c, BaseColumns._ID);
        return (id == null ? Identify.INVALID_ID : id);
    }

    public boolean getBoolean(final Cursor cursor, final String column) {
        final int columnIndex = cursor.getColumnIndex(column);
        if (cursor.isNull(columnIndex) || cursor.getShort(columnIndex) == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    public static String getString(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        return c.getString(columnIndex);
    }

    public static int getInt(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        return c.getInt(columnIndex);
    }

    @Nullable
    public static Integer getNullableInteger(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        Integer integer = null;
        if (!c.isNull(columnIndex)) {
            integer = c.getInt(columnIndex);
        }
        return integer;
    }

    public static long getLong(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        return c.getLong(columnIndex);
    }

    @Nullable
    public static Long getNullableLong(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        Long l = null;
        if (!c.isNull(columnIndex)) {
            l = c.getLong(columnIndex);
        }
        return l;
    }

    public static double getDouble(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        return c.getDouble(columnIndex);
    }

    @Nullable
    public static Double getNullableDouble(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        Double d = null;
        if (!c.isNull(columnIndex)) {
            d = c.getDouble(columnIndex);
        }
        return d;
    }

    public static float getFloat(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        return c.getFloat(columnIndex);
    }

    @Nullable
    public static Float getNullableFloat(final Cursor c, final String column) {
        final int columnIndex = c.getColumnIndex(column);
        Float f = null;
        if (!c.isNull(columnIndex)) {
            f = c.getFloat(columnIndex);
        }
        return f;
    }

    protected static final String[] COUNT_1_PROJECTION = new String[] { "count(1) as " + BaseColumns._COUNT };
    protected static final String WHERE_BASE_COLUMNS_ID = BaseColumns._ID + "=?";

    private final Context mContext;
    private final ContentResolver mContentResolver;

    protected AbstractDAO(final Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public Context getContext() {
        return mContext;
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    @NonNull
    public List<E> getAll() {
        return get(null, null, null);
    }

    @NonNull
    protected List<E> get(@Nullable final String selection, @Nullable final String[] selectionArgs,
                          @Nullable final String sortOrder) {
        return get(getTableUri(), getProjection(), selection, selectionArgs, sortOrder);
    }

    @NonNull
    protected List<E> get(@NonNull final Uri uri, @Nullable final String[] projection,
                          @Nullable final String selection, @Nullable final String[] selectionArgs,
                          @Nullable final String sortOrder) {
        final Cursor cursor = mContentResolver.query(uri, projection,
                selection, selectionArgs, sortOrder);
        return getItemsFromCursor(cursor);
    }

    @NonNull
    protected abstract Uri getTableUri();

    @Nullable
    protected abstract String[] getProjection();

    @NonNull
    private List<E> getItemsFromCursor(final Cursor cursor) {
        final List<E> items = new ArrayList<E>();
        if (isCursorValid(cursor)) {
            if (cursor.moveToFirst()) {
                do {
                    items.add(getItemFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return items;
    }

    @NonNull
    protected abstract E getItemFromCursor(final Cursor cursor);

    @Nullable
    public Uri insert(@NonNull final E entity) {
        return insert(getTableUri(), entity);
    }

    @Nullable
    protected Uri insert(@NonNull final Uri uri, @NonNull final E entity) {
        return mContentResolver.insert(uri, toContentValues(entity));
    }

    public int update(@NonNull final E entity) {
        final int id = entity.getId();
        return update(getTableUri(), entity, WHERE_BASE_COLUMNS_ID, new String[]{Integer.toString(id)});
    }

    protected int update(@NonNull final Uri uri, @NonNull final E entity,
                         @Nullable final String where,
                         @Nullable final String[] selectionArgs) {
        return mContentResolver.update(uri, toContentValues(entity), where, selectionArgs);
    }

    public boolean insertOrUpdate(@NonNull final E entity) {
        return (update(entity) > 0 || (insert(entity) != null));
    }

    public int bulkInsert(@NonNull final List<E> entities) {
        return bulkInsert(getTableUri(), entities);
    }

    protected int bulkInsert(@NonNull final Uri uri, @NonNull final List<E> entities) {
        final ContentValues[] contentValues = toContentValues(entities);
        return mContentResolver.bulkInsert(uri, contentValues);
    }

    @NonNull
    protected abstract ContentValues toContentValues(@NonNull final E entity);

    @NonNull
    private ContentValues[] toContentValues(@NonNull final List<E> entities) {
        final int size = entities.size();
        final ContentValues[] contentValues = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            contentValues[i] = toContentValues(entities.get(i));
        }
        return contentValues;
    }

    protected int delete(@NonNull final Uri uri,
                         @Nullable final String where,
                         @Nullable final String[] selectionArgs) {
        return mContentResolver.delete(uri, where, selectionArgs);
    }

    public int delete(@NonNull final E entity) {
        return delete(entity.getId());
    }

    public int delete(final int id) {
        int deletedRecords = 0;
        if (id != Identify.INVALID_ID) {
            deletedRecords = delete(getTableUri(), WHERE_BASE_COLUMNS_ID,
                    new String[] { Integer.toString(id) });
        }
        return deletedRecords;
    }

    public int deleteAll(@NonNull final int[] ids) {
        int deletedRecords = 0;
        final List<String> idsList = new ArrayList<String>();
        for (final int id : ids) {
            if (id != Identify.INVALID_ID) {
                idsList.add(Integer.toString(id));
            }
        }
        if (!idsList.isEmpty()) {
            final String where = DatabaseUtils.in(BaseColumns._ID, ids.length, false);
            final String[] whereArgs = idsList.toArray(new String[idsList.size()]);
            deletedRecords = delete(getTableUri(), where, whereArgs);
        }
        return deletedRecords;
    }

    public int deleteAll(@NonNull final List<E> entities) {
        final int[] ids = new int[entities.size()];
        int deletedRecords = 0;
        if (!entities.isEmpty()) {
            for (int i = 0; i < entities.size(); i++) {
                ids[i] = entities.get(i).getId();
            }
            deletedRecords = deleteAll(ids);
        }
        return deletedRecords;
    }

    @Nullable
    public E get(final int id) {
        final List<E> entities = get(WHERE_BASE_COLUMNS_ID,
                new String[] { Integer.toString(id) }, null);
        return (entities.isEmpty() ? null : entities.get(0));
    }

    public boolean existsById(final int id) {
        return existsByCustomField(id, WHERE_BASE_COLUMNS_ID);
    }

    public boolean existsByCustomField(final Object object, final String whereColumnCondition) {
        final Cursor cursor = mContentResolver.query(getTableUri(), COUNT_1_PROJECTION,
                whereColumnCondition, new String[] { object.toString() }, null);

        boolean exists = false;
        if (isCursorValid(cursor)) {
            if (cursor.moveToFirst()) {
                exists = (getInt(cursor, BaseColumns._COUNT) > 0);
            }
            cursor.close();
        }

        return exists;
    }

}