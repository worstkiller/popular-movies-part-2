package com.design.vikas.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vikas kumar on 7/30/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "favorites.db";
    final static int DB_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContractMovie.MovieEntry.TABLE_NAME + " (" +
                    ContractMovie.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    ContractMovie.MovieEntry.COLUMN_MOVIE_ID +  INTEGER_TYPE+ COMMA_SEP +
                    ContractMovie.MovieEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ContractMovie.MovieEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    ContractMovie.MovieEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    ContractMovie.MovieEntry.COLUMN_IMAGE + TEXT_TYPE + COMMA_SEP +
                    ContractMovie.MovieEntry.COLUMN_RATING + REAL_TYPE +
            " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContractMovie.MovieEntry.TABLE_NAME;

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
