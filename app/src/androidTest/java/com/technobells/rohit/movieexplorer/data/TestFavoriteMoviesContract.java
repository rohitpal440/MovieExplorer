package com.technobells.rohit.movieexplorer.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by rohit on 30/12/15.
 */
public class TestFavoriteMoviesContract extends AndroidTestCase {



    private static final int TEST_MOVIE_ID = 23424;
    private static final String TEST_REVIEW_ID = "xjljdhalj";  // December 20th, 2014

    public void testBuildReviewMovie() {
        Uri reviewMovieUri = FavoriteMoviesContract.ReviewEntry.buildReviewMovieUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildReviewMovie in " +
                        "FavoriteMovieContract.",
                reviewMovieUri);
        System.out.println(reviewMovieUri);
        String movieId = reviewMovieUri.getLastPathSegment();
        assertEquals("Error: Movie id is not properly appended to the end of the Uri",
                TEST_MOVIE_ID,Integer.parseInt(movieId));
        assertEquals("Error: ReviewMovie Uri doesn't match our expected result",
                reviewMovieUri.toString(),
                "content://com.technobells.rohit.movieexplorer/review/23424");
    }

}
