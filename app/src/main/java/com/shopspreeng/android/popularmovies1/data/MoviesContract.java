package com.shopspreeng.android.popularmovies1.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jayson surface on 29/04/2017.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.shopspreeng.android.popularmovies1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Table name for URI PATH
    public static final String PATH_MOVIES = "movielist";


    public static final class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movielist";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_MOVIE_TITLE = "movietitle";

        public static final String COLUMN_MOVIE_RATING = "rating";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "releasedate";

        public static final String COLUMN_REVIEW = "review";

        public static final String COLUMN_REVIEW_AUTHOR = "author";

        public static final String COLUMN_TRAILER = "trailer";

        //TODO add column for storing image files in db
    }
}
