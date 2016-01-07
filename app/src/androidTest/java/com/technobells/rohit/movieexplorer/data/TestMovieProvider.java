package com.technobells.rohit.movieexplorer.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by rohit on 30/12/15.
 */
public class TestMovieProvider  extends AndroidTestCase {
    public static final String LOG_TAG = TestMovieProvider.class.getSimpleName();


    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

       Note : Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */


    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                FavoriteMoviesContract.VideoEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Video table during delete", 0, cursor.getCount());
        cursor.close();

    }

    /*
       This helper function deletes all records from both database tables using the database
       functions only.  This is designed to be used to reset the state of the database until the
       delete functionality is available in the ContentProvider.
     */

    public void deleteAllRecordsFromDB() {
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(FavoriteMoviesContract.MovieEntry.TABLE_NAME,null,null);
        db.delete(FavoriteMoviesContract.ReviewEntry.TABLE_NAME, null, null);
        db.delete(FavoriteMoviesContract.VideoEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
        Note : Refactor this function to use the deleteAllRecordsFromProvider functionality once
        you have implemented delete functionality there.
     */

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    /*
     *   This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FavoriteMoviesContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FavoriteMoviesContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: Movie Provider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    /*
           This test doesn't touch the database.  It verifies that the ContentProvider returns
           the correct type for each type of URI that it can handle.
        */
    public void testGetType() {
        // content://com.technobells.rohit.movieexplorer/review/
        String type = mContext.getContentResolver().getType(FavoriteMoviesContract.ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.technobells.rohit.movieexplorer/review/
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                FavoriteMoviesContract.ReviewEntry.CONTENT_TYPE, type);

        int testMovieId = 14072;
        // endpoint: /review/14072
        type = mContext.getContentResolver().getType(
                FavoriteMoviesContract.ReviewEntry.buildReviewMovieUri(testMovieId));
        // vnd.android.cursor.dir/com.technobells.rohit.movieexplorer/review
        assertEquals("Error: the ReviewEntry CONTENT_URI with movie should return ReviewEntry.CONTENT_TYPE",
                FavoriteMoviesContract.ReviewEntry.CONTENT_TYPE, type);

//        String testReviewId = "dfdxv";
//        // content://com.weather/94074/20140612
//        type = mContext.getContentResolver().getType(
//                FavoriteMoviesContract.ReviewEntry.buildReviewMovieUriWithReviewId(testMovieId, testReviewId));
//        // vnd.android.cursor.item/com.technobells.rohit.movieexplorer/review/14072/fjadfjlajf
//        assertEquals("Error: the ReviewEntry CONTENT_URI with movie and reviewId should return WeatherEntry.CONTENT_ITEM_TYPE",
//                FavoriteMoviesContract.ReviewEntry.CONTENT_ITEM_TYPE, type);


        // content://com.technobells.rohit.movieexplorer/video/
        type = mContext.getContentResolver().getType(FavoriteMoviesContract.VideoEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.technobells.rohit.movieexplorer/video
        assertEquals("Error: the ReviewEntry CONTENT_URI should return VideoEntry.CONTENT_TYPE",
                FavoriteMoviesContract.VideoEntry.CONTENT_TYPE, type);

        // endpoint: /video/14072
        type = mContext.getContentResolver().getType(
                FavoriteMoviesContract.VideoEntry.buildVideoMovieUri(testMovieId));
        // vnd.android.cursor.dir/com.technobells.rohit.movieexplorer/review
        assertEquals("Error: the ReviewEntry CONTENT_URI with movie should return VideoEntry.CONTENT_TYPE",
                FavoriteMoviesContract.VideoEntry.CONTENT_TYPE, type);


        // endpoint: /movie
        type = mContext.getContentResolver().getType(FavoriteMoviesContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.technobells.rohit.movieexplorer/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                FavoriteMoviesContract.MovieEntry.CONTENT_TYPE, type);
    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */

    public void testBasicMovieDataQuery() {
        // insert our test records into the database
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createInterstellerMovieValues();
        long movieRowId = TestUtilities.insertInterstellerValues(mContext);


        ContentValues reviewValues = TestUtilities.createReviewValues(movieRowId);

        long reviewRowId = db.insert(FavoriteMoviesContract.ReviewEntry.TABLE_NAME, null, reviewValues);
        assertTrue("Unable to Insert ReviewEntry into the Database", reviewRowId != -1);

        ContentValues videoValues  = TestUtilities.createVideoValues(movieRowId);

        long videoRowId = db.insert(FavoriteMoviesContract.VideoEntry.TABLE_NAME,null,videoValues);
        assertTrue("Unable to Insert VideoEntry into the Database", videoRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("Review Cursor is not matching with Content values inserted inside testBasicMovieQuery", cursor, reviewValues);

        cursor = mContext.getContentResolver().query(FavoriteMoviesContract.VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("Video Cursor is not matching with Content values inserted inside testBasicMovieQuery",cursor,videoValues);

        cursor.close();

    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.
     */
    public void testBasicMovieQueries() {
        // insert our test records into the database
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createInterstellerMovieValues();
        long movieRowId = TestUtilities.insertInterstellerValues(mContext);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQueries, Movie query", movieCursor, testValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if ( Build.VERSION.SDK_INT >= 19 ) {
            assertEquals("Error: Movie Query did not properly set NotificationUri",
                    movieCursor.getNotificationUri(), FavoriteMoviesContract.MovieEntry.CONTENT_URI);
        }
    }


    /*
        This test uses the provider to insert and then update the data.
     */
    public void testUpdateMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createInterstellerMovieValues();

        Uri movieUri = mContext.getContentResolver().
                insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE, "I am a SuperMan");
        updatedValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW, "This is a great movie");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor movieCursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                updatedValues,
                FavoriteMoviesContract.MovieEntry._ID + "= ?",
                new String[] { Long.toString(movieRowId)}
        );
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // note: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,   // projection
                FavoriteMoviesContract.MovieEntry._ID + " = " + movieRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateMovie.  Error validating movie entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Note: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.

    public void testInsertReadProvider() {

        //Testing insert in Movie Table
        ContentValues testValues = TestUtilities.createInterstellerMovieValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, testValues);

        /*
        Did our content observer get called?  Note:  If this fails, your insert movie
        isn't calling getContext().getContentResolver().notifyChange(uri, null);
        */
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, testValues);

        Log.i(LOG_TAG,"Movie in MovieEntry is :"+movieRowId);

        ///Testing insert in Review Table

        ContentValues reviewValues = TestUtilities.createReviewValues(movieRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, true, tco);

        Uri reviewInsertUri = mContext.getContentResolver()
                .insert(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, reviewValues);

        assertTrue(reviewInsertUri != null);

        // Did our content observer get called?  note:  If this fails, your insert review
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ReviewEntry insert.",
                cursor, reviewValues);

        Log.i(LOG_TAG,"Successfully inserted the values into the Review Table");
        // Add the movie values in with the review data so that we can make
        // sure that the join worked and we actually get all the values back

        reviewValues.putAll(testValues);

        // Get the joined Review and Movie data
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.buildReviewMovieUri(TestUtilities.TEST_MOVIE_ID),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Review and Movie Data.",
                cursor, reviewValues);



        // Get the joined Movie data for a specific reviewid
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.buildReviewMovieUriWithReviewId(TestUtilities.TEST_MOVIE_ID, "abc"),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Review and Movie data for a specific reviewId.",
                cursor, reviewValues);



        /// Test insert in Video Table

        ContentValues videoValues = TestUtilities.createVideoValues(movieRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.VideoEntry.CONTENT_URI, true, tco);

        Uri videoInsertUri = mContext.getContentResolver()
                .insert(FavoriteMoviesContract.VideoEntry.CONTENT_URI, videoValues);
        assertTrue(videoInsertUri != null);

        // Did our content observer get called?  note:  If this fails, your insert video
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.VideoEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating VideoEntry insert.",
                cursor, videoValues);

        // Add the movie values in with the video data so that we can make
        // sure that the join worked and we actually get all the values back

        videoValues.putAll(testValues);

        // Get the joined Review and Movie data
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.VideoEntry.buildVideoMovieUri(TestUtilities.TEST_MOVIE_ID),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Video and Movie Data.",
                cursor, videoValues);



        // Get the joined Movie data for a specific videoId

        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.VideoEntry.buildVideoMovieUriWithVideoId(TestUtilities.TEST_MOVIE_ID,"xyz"),
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Video and Movie data for a specific videoId.",
                cursor, videoValues);

    }

    // Make sure we can still delete after adding/updating stuff
    //
    // Note: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our Movie delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.MovieEntry.CONTENT_URI, true, movieObserver);

        // Register a content observer for our Review delete.
        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, true, reviewObserver);

        // Register a content observer for our video delete.
        TestUtilities.TestContentObserver videoObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.VideoEntry.CONTENT_URI, true, videoObserver);


        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieObserver.waitForNotificationOrFail();
        reviewObserver.waitForNotificationOrFail();
        videoObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(reviewObserver);
        mContext.getContentResolver().unregisterContentObserver(videoObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertReviewValues(long movieRowId) {

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID,"dafxfsfaf"+i);
            reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID,movieRowId);
            reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_AUTHOR,"Rohit" + i );
            reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_CONTENT,"this is review no . " + i);
            reviewValues.put(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_LINK,"www.youtube.com/" + i);

            returnContentValues[i] = reviewValues;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertVideoValues(long movieRowId) {

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues videoValues = new ContentValues();
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID,"dafxfsfaf"+i);
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID,movieRowId);
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_LANG,"English");
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_KEY,"dafjufafnfnfxy"+i);
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_NAME,"Official Trailer"+i);
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_SITE,"Youtube");
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_SIZE,"720p");
            videoValues.put(FavoriteMoviesContract.VideoEntry.COLUMN_TYPE,"Trailer");
            returnContentValues[i] = videoValues;
        }
        return returnContentValues;
    }

    // Note: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    public void testBulkInsert() {
        // first, let's create a movie value
        ContentValues testValues = TestUtilities.createInterstellerMovieValues();
        Uri movieUri = mContext.getContentResolver().insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating MovieEntry.",
                cursor, testValues);

        // Now we can bulkInsert some Movie Data.  In fact, we only implement BulkInsert for review
        // entries and Video entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.

        /// Bulk insert on Review Table

        ContentValues[] bulkInsertContentValues = createBulkInsertReviewValues(movieRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver reviewObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, true, reviewObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(FavoriteMoviesContract.ReviewEntry.CONTENT_URI, bulkInsertContentValues);

        // Note:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        reviewObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(reviewObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  //FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID + " ASC"  // sort order == by TMDB_ID ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating ReviewEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }


        // testing bulk insert for Video Entry table

        bulkInsertContentValues = createBulkInsertVideoValues(movieRowId);

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver videoObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(FavoriteMoviesContract.VideoEntry.CONTENT_URI, true, videoObserver);

        insertCount = mContext.getContentResolver().bulkInsert(FavoriteMoviesContract.VideoEntry.CONTENT_URI, bulkInsertContentValues);

        // Note:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        videoObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(videoObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  //FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_REVIEW_ID + " ASC"  // sort order == by TMDB_ID ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating VideoEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }

        cursor.close();
    }


}
