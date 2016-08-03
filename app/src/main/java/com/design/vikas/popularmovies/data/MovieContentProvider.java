package com.design.vikas.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.design.vikas.popularmovies.R;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by vikas kumar on 7/30/2016.
 */
public class MovieContentProvider extends ContentProvider {

    private SQLiteDatabase sqLiteDatabase;
    private MyDatabaseHelper databaseHelper;

    private static final int CODE_ID = 100;
    private static final int CODE_DATA = 101;

    private static final UriMatcher URI_MATCHER = buildMatcher();

    public static boolean isSuccess = false;

    private static HashMap<String,String> stringHashMap ;

    private static UriMatcher buildMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContractMovie.AUTHORITY;
        uriMatcher.addURI(authority,ContractMovie.PATH+ "/*",CODE_ID);
        uriMatcher.addURI(authority,ContractMovie.PATH,CODE_DATA);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MyDatabaseHelper(getContext());
        return (databaseHelper==null)?false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractMovie.MovieEntry.TABLE_NAME);
        switch (URI_MATCHER.match(uri)){
            case CODE_ID:
                queryBuilder.appendWhere(ContractMovie.MovieEntry._ID+"="+uri.getPathSegments().get(1));
                break;
            case CODE_DATA:
                queryBuilder.setProjectionMap(stringHashMap);
                break;
            default:
                throw new IllegalArgumentException(getContext().getResources().getString(R.string.exception_Illegal));
        }
        Cursor cursor = queryBuilder.query(sqLiteDatabase,strings,s,strings1,null,null,s1);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Long rowId;
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        {
            sqLiteDatabase.beginTransaction();
            rowId = sqLiteDatabase.insert(ContractMovie.MovieEntry.TABLE_NAME, null, contentValues);
            sqLiteDatabase.yieldIfContendedSafely();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }

        if (rowId>0){
            getContext().getContentResolver().notifyChange(uri, null);
            isSuccess = true;
            return uri;
        }
        try {
            throw new SQLException(getContext().getResources().getString(R.string.exception_insert) + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int rowId = 0;
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {

            case CODE_DATA:
                sqLiteDatabase.beginTransaction();
                rowId = sqLiteDatabase.delete(ContractMovie.MovieEntry.TABLE_NAME, s, strings);
                sqLiteDatabase.yieldIfContendedSafely();
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                if (rowId > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    isSuccess = true;
                    return rowId;
                }
                try {
                    throw new SQLException(getContext().getResources().getString(R.string.exception_delete) + uri);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case CODE_ID:
                sqLiteDatabase.beginTransaction();
                rowId = sqLiteDatabase.delete(ContractMovie.MovieEntry.TABLE_NAME, s, strings);
                sqLiteDatabase.yieldIfContendedSafely();
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();

                if (rowId>0){
                    getContext().getContentResolver().notifyChange(uri, null);
                    isSuccess = true;
                    return rowId;
                }
                try {
                    throw new SQLException(getContext().getResources().getString(R.string.exception_delete)+uri);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return rowId;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int rowId;
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        {
            sqLiteDatabase.beginTransaction();
            rowId = sqLiteDatabase.update(ContractMovie.MovieEntry.TABLE_NAME,contentValues,s , strings);
            sqLiteDatabase.yieldIfContendedSafely();
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }

        if (rowId>0){
            getContext().getContentResolver().notifyChange(uri, null);
            isSuccess = true;
        }
        try {
            throw new SQLException(getContext().getResources().getString(R.string.exception_delete) + uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowId;
    }
}
