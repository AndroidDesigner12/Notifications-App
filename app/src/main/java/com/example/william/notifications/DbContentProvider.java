package com.example.william.notifications;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.william.notifications.DbContract.CONTENT_AUTHORITY;
import static com.example.william.notifications.DbContract.SCHEDULE_PATH;

/**
 * Created by william on 3/21/18.
 */

public class DbContentProvider extends ContentProvider {

    private static final int DAYS_AND_DATE = 1;


    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY,SCHEDULE_PATH,DAYS_AND_DATE);
    }


    private DbHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public boolean onCreate() {
        helper = new DbHelper(getContext());
        sqLiteDatabase = helper.getReadableDatabase();

        return false;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = uri.getLastPathSegment();

        Cursor cursor = sqLiteDatabase.query(tableName,projection,
                null,null,
                null,null,sortOrder);

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
        String tableName = uri.getLastPathSegment();
        long id = sqLiteDatabase.insert(tableName,null,values);

        Uri tempUri = Uri.parse(" "+id);

        return tempUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table_name = uri.getLastPathSegment();
       int i =  sqLiteDatabase.update(table_name,values,selection,selectionArgs);

        return i;
    }
}
