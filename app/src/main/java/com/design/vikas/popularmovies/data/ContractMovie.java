package com.design.vikas.popularmovies.data;

import android.content.ContentProvider;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vikas kumar on 7/30/2016.
 */
public class ContractMovie {

    public final static String AUTHORITY = "com.design.vikas.popularmovies";
    public final static Uri BASE_URI = Uri.parse("content://"+AUTHORITY);
    public final static String PATH = "movieData";

    public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

    public ContractMovie() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "movie_name";
        public static final String COLUMN_DATE = "movie_date";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_IMAGE = "movie_image";
        public static final String COLUMN_DESCRIPTION = "movie_description";
    }
}
