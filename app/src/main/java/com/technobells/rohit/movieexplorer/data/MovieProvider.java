package com.technobells.rohit.movieexplorer.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by rohit on 30/12/15.
 */
public class MovieProvider extends ContentProvider{

    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int REVIEW = 200;
    static final int REVIEW_WITH_MOVIE = 201;
    static final int REVIEW_WITH_MOVIE_AND_REVIEW_ID = 202;
    static final int VIDEO = 300;
    static final int VIDEO_WITH_MOVIE = 301;
    static final int VIDEO_WITH_MOVIE_AND_VIDEO_ID = 302;

    private static final SQLiteQueryBuilder sReviewByMovieIdQueryBuilder;

    static {
        sReviewByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        // review INNER JOIN movie ON review.movie_id = movie._id
        sReviewByMovieIdQueryBuilder.setTables(
                FavoriteMoviesContract.ReviewEntry.TABLE_NAME
                        + " INNER JOIN "
                        + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                        + " ON "
                        + FavoriteMoviesContract.ReviewEntry.TABLE_NAME
                        + "." + FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID
                        + " = "
                        + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                        + "." + FavoriteMoviesContract.MovieEntry._ID );
    }

    private static final SQLiteQueryBuilder sVideoByMovieIdQueryBuilder;
    static {
        sVideoByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        // video INNER JOIN movie ON video.movie_id = movie._id
        sVideoByMovieIdQueryBuilder.setTables(
                FavoriteMoviesContract.VideoEntry.TABLE_NAME
                        + " INNER JOIN "
                        + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                        + " ON "
                        + FavoriteMoviesContract.VideoEntry.TABLE_NAME
                        + "." + FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID
                        + " = "
                        + FavoriteMoviesContract.MovieEntry.TABLE_NAME
                        + "." + FavoriteMoviesContract.MovieEntry._ID );
    }

    //movie.tmdb_id = ?
    private static final String sMovieIdSelection =
            FavoriteMoviesContract.MovieEntry.TABLE_NAME
                    + "." + FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ? ";


    //movie.tmdb_id = ? AND review.tmdb_id = ?
    private static final String sMovieIdAndReviewSelection =
            FavoriteMoviesContract.MovieEntry.TABLE_NAME
                    + "." + FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID
                    + " = ? AND "
                    + FavoriteMoviesContract.ReviewEntry.TABLE_NAME
                    + "." + FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID
                    + " = ? ";

    //movie.tmdb_id = ? AND video.tmdb_id = ?
    private static final String sMovieIDAndVideoSelection =
            FavoriteMoviesContract.MovieEntry.TABLE_NAME
                    + "." + FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID
                    + " = ? AND "
                    + FavoriteMoviesContract.VideoEntry.TABLE_NAME
                    + "." + FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID
                    + " = ? ";


    private Cursor getReviewByMovieId(Uri uri, String [] projection , String sortOrder){
        String movieId = FavoriteMoviesContract.MovieEntry.getMovieIdFromUri(uri);
        String reviewId = FavoriteMoviesContract.ReviewEntry.getReviewIdFromUri(uri);
        String selection;
        Log.i(LOG_TAG,"Performing Read Operation inside getReviewByMovieId() method with ReviewId " + reviewId);
        String[] selectionArgs;
        if (reviewId == null ){
            selection = sMovieIdSelection;
            selectionArgs = new String[]{movieId};
        } else {
            selection = sMovieIdAndReviewSelection;
            selectionArgs = new String[]{movieId,reviewId};
        }

        Log.i(LOG_TAG,"Value of selection is : " + selection + "\nWith selection argument : "+ selectionArgs[0]);



        return sReviewByMovieIdQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

    }

    private Cursor getVideoByMovieId(Uri uri,String[] projection,String sortOrder){
        String movieId = FavoriteMoviesContract.MovieEntry.getMovieIdFromUri(uri);
        String videoId = FavoriteMoviesContract.VideoEntry.getVideoIdFromUri(uri);
        String selection;
        String[] selectionArgs;
        if(videoId == null){
            selection = sMovieIdSelection;
            selectionArgs = new String[]{movieId};
        } else {
            selection = sMovieIDAndVideoSelection;
            selectionArgs = new String[]{movieId,videoId};
        }

        return sVideoByMovieIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }


    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,FavoriteMoviesContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority,FavoriteMoviesContract.PATH_REVIEW,REVIEW);
        matcher.addURI(authority,FavoriteMoviesContract.PATH_REVIEW + "/#",REVIEW_WITH_MOVIE);
        //matcher.addURI(authority,FavoriteMoviesContract.PATH_REVIEW + "/#/*",REVIEW_WITH_MOVIE_AND_REVIEW_ID);
        matcher.addURI(authority,FavoriteMoviesContract.PATH_VIDEO,VIDEO);
        matcher.addURI(authority,FavoriteMoviesContract.PATH_VIDEO + "/#",VIDEO_WITH_MOVIE);
        //matcher.addURI(authority,FavoriteMoviesContract.PATH_VIDEO + "/#/*",VIDEO_WITH_MOVIE_AND_VIDEO_ID);
        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                return FavoriteMoviesContract.MovieEntry.CONTENT_TYPE;

            case REVIEW:
                return FavoriteMoviesContract.ReviewEntry.CONTENT_TYPE;

            case REVIEW_WITH_MOVIE:
                return FavoriteMoviesContract.ReviewEntry.CONTENT_TYPE;

//            case REVIEW_WITH_MOVIE_AND_REVIEW_ID:
//                return FavoriteMoviesContract.ReviewEntry.CONTENT_ITEM_TYPE;

            case VIDEO:
                return FavoriteMoviesContract.VideoEntry.CONTENT_TYPE;

            case VIDEO_WITH_MOVIE:
                return FavoriteMoviesContract.VideoEntry.CONTENT_TYPE;

//            case VIDEO_WITH_MOVIE_AND_VIDEO_ID:
//                return FavoriteMoviesContract.VideoEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri,String[] projection, String selection,String[] selectionArgs,String sortOrder){
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case REVIEW:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case REVIEW_WITH_MOVIE:{
                retCursor = getReviewByMovieId(uri,projection,sortOrder);
                Log.i(LOG_TAG,"Read Query REVIEW WITH MOVIE performed");
                break;
            }



            case VIDEO:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.VideoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case VIDEO_WITH_MOVIE:{
                retCursor = getVideoByMovieId(uri,projection,sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final  int match  = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIE: {
                long _id = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoriteMoviesContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into uri " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(FavoriteMoviesContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoriteMoviesContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into uri " + uri);
                break;
            }
            case VIDEO: {
                long _id = db.insert(FavoriteMoviesContract.VideoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FavoriteMoviesContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into uri " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match  = sUriMatcher.match(uri);
        int rowsDeleted;
        if(null == selection ) selection = "1";
        switch (match){
            case MOVIE:
                rowsDeleted = db.delete(FavoriteMoviesContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;

            case REVIEW:
                rowsDeleted = db.delete(FavoriteMoviesContract.ReviewEntry.TABLE_NAME,selection,selectionArgs);
                break;

            case VIDEO:
                rowsDeleted = db.delete(FavoriteMoviesContract.VideoEntry.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri :"+uri);
        }
        if (rowsDeleted !=0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri,ContentValues values, String selection, String[] selectionArgs){
        final SQLiteDatabase db =mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch(match){
            case MOVIE:
                rowsUpdated = db.update(FavoriteMoviesContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(FavoriteMoviesContract.ReviewEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case VIDEO:
                rowsUpdated = db.update(FavoriteMoviesContract.VideoEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri : "+ uri);
        }
        if (rowsUpdated != 0 ){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REVIEW: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // normalizeDate(value);
                        long _id = db.insert(FavoriteMoviesContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case VIDEO:{
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // normalizeDate(value);
                        long _id = db.insert(FavoriteMoviesContract.VideoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }


            default:
                return super.bulkInsert(uri, values);
        }

        //return super.bulkInsert(uri, values);
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
