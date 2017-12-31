package com.example.omri.battleShip.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Mark on 28/12/2017.
 */

public class DataProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.omri.provider";
    static final String URL = "content://" + PROVIDER_NAME + "/string/Key";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String KEY = "Key";

    static final UriMatcher uriMatcher;
    private static final int URI_MATCH_KEY_VALUE = 1;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, KEY, URI_MATCH_KEY_VALUE);
    }

    private shipsOpenHelper db_sqLiteHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        this.db_sqLiteHelper = new shipsOpenHelper(this.getContext());

        // Create a write able database which will trigger its creation if it doesn't already exist.
        this.db = db_sqLiteHelper.getWritableDatabase();
        return this.db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;

        switch (DataProvider.uriMatcher.match(uri)) {
            case URI_MATCH_KEY_VALUE:
                cursor = this.db_sqLiteHelper.getCursor(selection);
                break;
            default: // Unknown URI
                cursor = this.db_sqLiteHelper.getCursor(selection);
                //throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db_sqLiteHelper.put(values);

        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
