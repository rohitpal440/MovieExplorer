package com.technobells.rohit.movieexplorer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rohit on 30/12/15.
 */
public class FavoriteMoviesDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";
    public FavoriteMoviesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE= "CREATE TABLE " + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                +"("
                + FavoriteMoviesContract.MovieEntry._ID +" INTEGER PRIMARY KEY, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID +" INTEGER UNIQUE NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE+" REAL NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT+" INTEGER NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW+" TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_PATH+" TEXT NOT NULL, "
                + FavoriteMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH+ " TEXT NOT NULL );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + FavoriteMoviesContract.ReviewEntry.TABLE_NAME
                +" ( "
                + FavoriteMoviesContract.ReviewEntry._ID+" INTEGER PRIMARY KEY, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID +" TEXT NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID +" INTEGER NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_AUTHOR+" TEXT NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_CONTENT+" TEXT NOT NULL, "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_LINK+" TEXT NOT NULL, "
                + "FOREIGN KEY (" + FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                + " (" + FavoriteMoviesContract.MovieEntry._ID+ "), "
                + "UNIQUE ( "
                + FavoriteMoviesContract.ReviewEntry._ID + ", "
                + FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID +") "
                + "ON CONFLICT REPLACE);";


        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + FavoriteMoviesContract.VideoEntry.TABLE_NAME
                +" ( "
                + FavoriteMoviesContract.VideoEntry._ID + " INTEGER PRIMARY KEY, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_LANG + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_SITE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_SIZE + " TEXT NOT NULL, "
                + FavoriteMoviesContract.VideoEntry.COLUMN_TYPE + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                + " (" + FavoriteMoviesContract.MovieEntry._ID+ "), "
                + "UNIQUE ( "
                + FavoriteMoviesContract.VideoEntry._ID + ", "
                + FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID +") "
                + "ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + FavoriteMoviesContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
