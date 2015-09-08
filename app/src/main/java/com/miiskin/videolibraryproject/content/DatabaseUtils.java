package com.miiskin.videolibraryproject.content;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    private static final String LOG_TAG = "DatabaseUtils";

    public static final String AUTHORITY = "ru.miiskin.videolibrary.contentprovider";
    public static final String SCHEME = "content";

    private static final String COLUMN_TYPE_REAL = "REAL";
    private static final String COLUMN_TYPE_INTEGER = "INTEGER";
    private static final String COLUMN_TYPE_BLOB = "BLOB";
    private static final String COLUMN_TYPE_TEXT = "TEXT";

    private static final String INDEX_SFX = "_idx";

    public static Uri getUri(final String table) {
        return Uri.parse(SCHEME + "://" + AUTHORITY + "/" + table);
    }

    public static class TableBuilder {

        private final String mName;
        private List<ColumnBuilder> mColumns;

        public TableBuilder(final String name) {
            mName = name;
            mColumns = new ArrayList<ColumnBuilder>();
        }

        public TableBuilder addColumn(final ColumnBuilder columnBuilder) {
            mColumns.add(columnBuilder);
            return this;
        }

        public void create(final SQLiteDatabase db) {
            final List<String> columnDefinitions = new ArrayList<String>();
            for (final ColumnBuilder column : mColumns) {
                columnDefinitions.add(column.build());
            }

            final StringBuilder tableSql = new StringBuilder();
            tableSql.append("CREATE TABLE ").append(mName);
            tableSql.append("(").append(TextUtils.join(", ", columnDefinitions));
            tableSql.append(");");

            db.execSQL(tableSql.toString());
        }

    }

    public static class ColumnBuilder {

        private String mName;
        private String mType;
        private boolean mIsPrimary = false;
        private boolean mNotNull = false;

        public ColumnBuilder() {
        }

        public ColumnBuilder text(final String name) {
            mName = name;
            mType = COLUMN_TYPE_TEXT;
            return this;
        }

        public ColumnBuilder integer(final String name) {
            mName = name;
            mType = COLUMN_TYPE_INTEGER;
            return this;
        }

        public ColumnBuilder real(final String name) {
            mName = name;
            mType = COLUMN_TYPE_REAL;
            return this;
        }

        public ColumnBuilder blob(final String name) {
            mName = name;
            mType = COLUMN_TYPE_BLOB;
            return this;
        }

        public ColumnBuilder primaryKey() {
            mIsPrimary = true;
            mNotNull = false;
            return this;
        }

        public ColumnBuilder notNull() {
            if (!mIsPrimary) {
                mNotNull = true;
            }
            return this;
        }

        public String build() {
            final StringBuilder sb = new StringBuilder();
            sb.append(mName).append(" ");
            sb.append(mType);
            if (mIsPrimary) {
                sb.append(" PRIMARY KEY");
            }
            if (mNotNull) {
                sb.append(" NOT NULL");
            }
            return sb.toString();
        }

    }

    public static void createIndex(final SQLiteDatabase db, final String table,
                                   final String name, final String[] onColumns) {
        final StringBuilder indexSql = new StringBuilder();
        indexSql.append("CREATE INDEX IF NOT EXISTS ").append(name);
        indexSql.append(INDEX_SFX).append(" ON ").append(table);
        indexSql.append("(").append(TextUtils.join(", ", onColumns));
        indexSql.append(");");

        db.execSQL(indexSql.toString());
    }

    public static void dropTable(final SQLiteDatabase db, final String table) {
        final StringBuilder dropSql = new StringBuilder();
        dropSql.append("DROP TABLE IF EXISTS ").append(table).append(";");

        db.execSQL(dropSql.toString());
    }

    public static String in(final String column, final int argsNumber, final boolean notIn) {
        final StringBuilder inClause = new StringBuilder();

        if (argsNumber > 0) {
            inClause.append(column)
                    .append(notIn ? " not " : " ")
                    .append("in (");
            for (int i = 0; i < argsNumber; i++) {
                if (i > 0) {
                    inClause.append(", ");
                }
                inClause.append("?");
            }
            inClause.append(")");
        }

        return inClause.toString();
    }

    private DatabaseUtils() {
    }

}