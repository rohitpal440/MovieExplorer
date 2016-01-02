package com.technobells.rohit.movieexplorer.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by rohit on 30/12/15.
 */
public class TestDb  extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();
    void deleteTheDatabase(){
        mContext.deleteDatabase(FavoriteMoviesDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(FavoriteMoviesContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(FavoriteMoviesContract.ReviewEntry.TABLE_NAME);
        tableNameHashSet.add(FavoriteMoviesContract.VideoEntry.TABLE_NAME);
        mContext.deleteDatabase(FavoriteMoviesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FavoriteMoviesDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'",null);
        assertTrue("Error:Database has not been created correctly",c.moveToFirst());
        do{
            tableNameHashSet.remove(c.getString(0));
        }while (c.moveToNext());
        assertTrue("Error: Database created without movie entry and review entry tables",tableNameHashSet.isEmpty());
        c = db.rawQuery("PRAGMA table_info(" + FavoriteMoviesContract.MovieEntry.TABLE_NAME + ")",null);
        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());
        final HashSet<String> movieColumnSet = new HashSet<>();
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry._ID);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnSet.add(FavoriteMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH);

        int columnNameIndex = c.getColumnIndex("name");
        do{
            String columnName = c.getString(columnNameIndex);
            movieColumnSet.remove(columnName);
        }while(c.moveToNext());

        assertTrue("Error: Database doesn't contains all required columns of movieEntry table",movieColumnSet.isEmpty());
        db.close();

    }

    public void testMovieTable(){

        insertMovie();

    }

    public void testReviewTable(){

        long movieRowId = insertMovie();
        assertFalse("Error: Movie not inserted Correctly",movieRowId==-1L);
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues reviewValues = TestUtilities.createReviewValues(movieRowId);
        long reviewRowId = db.insert(FavoriteMoviesContract.ReviewEntry.TABLE_NAME,null,reviewValues);
        assertTrue(reviewRowId != -1);
        Cursor reviewCursor = db.query(FavoriteMoviesContract.ReviewEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No Record returned from Review Query ", reviewCursor.moveToFirst());
        TestUtilities.validateCurrentRecord("testInsertReadDb review Entry failed to validate ",reviewCursor,reviewValues);
        assertFalse("Error: More than one record returned from review query", reviewCursor.moveToNext());
        reviewCursor.close();
        dbHelper.close();

    }

    public void testVideoTable(){

        long movieRowId = insertMovie();
        assertFalse("Error: Movie not inserted Correctly",movieRowId==-1L);
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues videoValues = TestUtilities.createVideoValues(movieRowId);
        long videoRowId = db.insert(FavoriteMoviesContract.VideoEntry.TABLE_NAME,null,videoValues);
        assertTrue(videoRowId != -1);
        Cursor videoCursor = db.query(FavoriteMoviesContract.VideoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No Record returned from Video Query ", videoCursor.moveToFirst());
        TestUtilities.validateCurrentRecord("testInsertReadDb Video Entry failed to validate ",videoCursor,videoValues);
        assertFalse("Error: More than one record returned from Video query", videoCursor.moveToNext());
        videoCursor.close();
        dbHelper.close();

    }


    long insertMovie(){
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createInterstellerMovieValues();
        long movieRowId;
        movieRowId = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME,null,testValues);
        assertTrue(movieRowId!=-1);
        Cursor cursor = db.query(FavoriteMoviesContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        assertTrue("Error: No Record Returned from Movie Entry Table ",cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Error:Location Query Validation failed",cursor,testValues);
        assertFalse("Error:More than one record returned from Movie query",cursor.moveToNext());
        db.close();
        return movieRowId;
    }

}
