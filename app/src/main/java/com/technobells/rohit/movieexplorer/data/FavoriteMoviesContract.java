package com.technobells.rohit.movieexplorer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rohit on 30/12/15.
 */
public class FavoriteMoviesContract {
    private static final String LOG_TAG = FavoriteMoviesContract.class.getSimpleName();
    public static final String CONTENT_AUTHORITY = "com.technobells.rohit.movieexplorer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "videos";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/"+PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"+ PATH_MOVIE;


        //Table
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TMDB_MOVIE_ID = "tmdb_id";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";



        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        //Table
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_TMDB_REVIEW_ID = "tmdb_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_REVIEW_LINK = "url";

        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildReviewMovieUri(int tmdbMovieId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbMovieId)).build();
        }

        public static Uri buildReviewMovieUriWithReviewId(int tmdbMovieId,String tmdbRevieId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbMovieId))
                    .appendQueryParameter(COLUMN_TMDB_REVIEW_ID,tmdbRevieId)
                    .build();
        }

        public static String getReviewIdFromUri(Uri uri){
//            int pathSegmentSize = uri.getPathSegments().size();
//            Log.i(LOG_TAG, "Size of pathsegment list is :" + uri.getPathSegments().size());
//            return (pathSegmentSize == 3 ? uri.getPathSegments().get(2): null );
            return uri.getQueryParameter(COLUMN_TMDB_REVIEW_ID);
        }

    }

    public static final class VideoEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        //Table
        public static final String TABLE_NAME = "video";
        public static final String COLUMN_TMDB_VIDEO_ID = "tmdb_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_LANG = "language";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";

        public static Uri buildVideoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildVideoMovieUri(int tmdbMovieId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbMovieId)).build();
        }

        public static Uri buildVideoMovieUriWithVideoId(
                int tmdbMovieId,String tmdbVideoId){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(tmdbMovieId))
                    .appendQueryParameter(COLUMN_TMDB_VIDEO_ID,tmdbVideoId)
                    .build();
        }

        public static String getVideoIdFromUri(Uri uri ){
//            int pathSegmentSize = uri.getPathSegments().size();
//            Log.i(LOG_TAG, "Size of pathsegment list is :" + uri.getPathSegments().size());
//            return (pathSegmentSize == 3 ? uri.getPathSegments().get(2): null );
            return uri.getQueryParameter(COLUMN_TMDB_VIDEO_ID);
        }

    }
}
