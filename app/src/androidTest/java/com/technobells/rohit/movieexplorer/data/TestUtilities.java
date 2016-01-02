package com.technobells.rohit.movieexplorer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.technobells.rohit.movieexplorer.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by rohit on 30/12/15.
 */
public class TestUtilities extends AndroidTestCase {
    public static final int TEST_MOVIE_ID = 14072;
    public static final String TEST_RANDOM_ID ="bxfsffas122df";
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues){
        assertTrue("Error: Empty cursor returend " + error,valueCursor.moveToFirst());
        validateCurrentRecord(error,valueCursor,expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues){
        Set<Map.Entry<String,Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String,Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx  = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '"+columnName + "' not found. " + error,idx ==-1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '"+entry.getValue().toString() + "' did not match expected value '"
                    +expectedValue+"'. "+ error,expectedValue,valueCursor.getString(idx) );
        }
    }

    static ContentValues createReviewValues (long movieRowId){
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID,"abc");
        reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID,movieRowId);
        reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_AUTHOR,"Rohit");
        reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_CONTENT,"This movie was awesome");
        reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_LINK,"www.facebook.com/rohitpal440");
        return reviewValues;
    }

    static ContentValues createVideoValues (long movieRowId){
        ContentValues videoValues = new ContentValues();
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID,"xyz");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID,movieRowId);
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_LANG,"English");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_KEY,"SUXWAEX2jlg");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_NAME,"Trailer");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_SITE,"Youtube.com");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_SIZE,"720p");
        videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_TYPE, "Trailer");
        return videoValues;
    }

    static ContentValues createInterstellerMovieValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(FavoriteMoviesContract.MovieEntry._ID,1);
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,"14072");
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE,"Intersteller");
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE,"2014-25-12");
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY,7.56);
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,6.23);
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT,3465);
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW,"IT is all about space and time");
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_PATH,"jdkjsddfd.jgp");
        testValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,"ufdfjlfj.jpg");
        return testValues;
    }

    static long insertInterstellerValues(Context context){
        FavoriteMoviesDbHelper dbHelper =new FavoriteMoviesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createInterstellerMovieValues();
        long movieRowId;
        movieRowId = db.insert(FavoriteMoviesContract.MovieEntry.TABLE_NAME,null,testValues);
        assertTrue("Error: failure to insert Intersteller Values ", movieRowId !=-1);
        return movieRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver(){
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht){
            super(new android.os.Handler(ht.getLooper()));
            mHT =ht;
        }

        @Override
        public void onChange(boolean selfChange){
            onChange(selfChange,null);
        }

        @Override
        public void onChange(boolean selfChange,Uri uri){
            mContentChanged = true;
        }

        public void waitForNotificationOrFail(){
            new PollingCheck(5000){
                @Override
                protected boolean check(){
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }
    static TestContentObserver getTestContentObserver(){
        return TestContentObserver.getTestContentObserver();
    }
}
