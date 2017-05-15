package com.shopspreeng.android.popularmovies1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static android.R.attr.id;
import static android.content.ContentUris.withAppendedId;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.shopspreeng.android.popularmovies1.data.MoviesContract.MoviesEntry.TABLE_NAME;

/**
 * Created by jayson surface on 06/05/2017.
 */

public class MoviesProvider extends ContentProvider {

    private static final int MOVIES = 100;

    private static final int MOVIES_WITH_ID = 101;

    private MovieDbHelper mMovieDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY,MoviesContract.PATH_MOVIES + "/#",MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase mdb = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        int movieMatch;

        switch (match){
            case MOVIES:
                retCursor = mdb.query(TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

                break;
            case MOVIES_WITH_ID:

        default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase mdb = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case MOVIES:
                long id = mdb.insert(TABLE_NAME,null,contentValues);
                if(id > 0){
                    //success
                    returnUri = withAppendedId(MoviesContract.MoviesEntry.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert data into" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        final SQLiteDatabase mdb = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case MOVIES_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = mdb.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);

        }

        // Return the number of tasks deleted
        return tasksDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
