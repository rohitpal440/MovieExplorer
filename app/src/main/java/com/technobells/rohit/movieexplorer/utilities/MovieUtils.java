package com.technobells.rohit.movieexplorer.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.technobells.rohit.movieexplorer.data.FavoriteMoviesContract;
import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.model.Review;
import com.technobells.rohit.movieexplorer.model.Video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by rohit on 30/12/15.
 */
public class MovieUtils {
    private static final String LOG_TAG= MovieUtils.class.getSimpleName();
    public static final String BASE_URL="http://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGE= "http://image.tmdb.org/t/p/";
    public static final String BASE_URL_VIDEO_THUMBNAIL = "http://img.youtube.com/vi/";
    public static final String MIN_VOTE_COUNT = "1000";

    public static boolean FAVORITE_FLAG = false;

    public static final int MOVIE_DETAIL = 10 ;//Don't change the values as they represent the no. of column in cursor
    public static final int VIDEO = 9; //Don't change the values as they represent the no. of column in cursor
    public static final int REVIEW = 6;//Don't change the values as they represent the no. of column in cursor
    public static final int HEADER = 5 ;
    public static final int RECYCLER_VIEW = 7;
    public static final int CAST = 2;
    public static final int SIMILAR = 4;

    public static final Retrofit retrofitInstance;
    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);
        retrofitInstance = new Retrofit.Builder()
                .baseUrl(MovieUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
    }

    public static final HashMap<Integer,String> genre;
    static {
        genre = new HashMap<>();
        genre.put(28,"Action");
        genre.put(12,"Adventure");
        genre.put(16,"Animation");
        genre.put(35,"Comedy");
        genre.put(80,"Crime");
        genre.put(99,"Documentary");
        genre.put(18,"Drama");
        genre.put(10751,"Family");
        genre.put(14,"Fantasy");
        genre.put(10769,"Foreign");
        genre.put(36,"History");
        genre.put(27,"Horror");
        genre.put(10402,"Music");
        genre.put(9648,"Mystery");
        genre.put(10749,"Romance");
        genre.put(878,"Science Fiction");
        genre.put(10770,"TV Movie");
        genre.put(53,"Thriller");
        genre.put(10752,"War");
        genre.put(37,"Western");
    }
    public static final Review NO_REVIEW = new Review();
    static {
        NO_REVIEW.setAuthor("There is No Review For this Movie");
        NO_REVIEW.setContent(" ");
    }

    public static String formateDate(String dateString,String givenFormat,String requiredFormat){
        Date date;
        SimpleDateFormat fromSrc = new SimpleDateFormat(givenFormat);
        SimpleDateFormat myFormat = new SimpleDateFormat(requiredFormat);

        try {
            dateString = myFormat.format(fromSrc.parse(dateString));
            return dateString;
        } catch (ParseException e) {
            Log.e(LOG_TAG,"Error in Parsing the date");
        }
        return dateString;
    }

    public static Movie getMovieFromCursor(Cursor cursor,int pos){
        Movie movie = new Movie();
        Log.i(LOG_TAG,"Inside getMovieFromCursor(), with cursor position : "+ pos);

        if(cursor.moveToPosition(pos)) {


            movie.setId(cursor.getLong(MovieUtils.COL_MOVIE_TMDB_MOVIE_ID));
            movie.setTitle(cursor.getString(MovieUtils.COL_MOVIE_TITLE));
            movie.setReleaseDate(cursor.getString(MovieUtils.COL_MOVIE_RELEASE_DATE));
            movie.setVoteAverage(cursor.getDouble(MovieUtils.COL_MOVIE_VOTE_AVERAGE));
            movie.setVoteCount(cursor.getLong(MovieUtils.COL_MOVIE_VOTE_COUNT));
            movie.setOverview(cursor.getString(MovieUtils.COL_MOVIE_OVERVIEW));
            movie.setPosterPath(cursor.getString(MovieUtils.COL_MOVIE_POSTER_PATH));
            movie.setBackdropPath(cursor.getString(MovieUtils.COL_MOVIE_BACKDROP_PATH));
            movie.setPopularity(cursor.getDouble(MovieUtils.COL_MOVIE_POPULARITY));
            movie.setAdult(false);
            movie.setOriginalLanguage("en");
        }else{
            Log.i(LOG_TAG,"Inside getMovieFromCursor(), invalid Row at given position");
        }
        return movie;
    }
    public static Video getVideoFromCursor(Cursor cursor,int pos){
        Video video = new Video();
        if(cursor.moveToPosition(pos)) {
            video.setId(cursor.getString(MovieUtils.COL_VIDEO_TMDB_VIDEO_ID));
            video.setType(cursor.getString(MovieUtils.COL_VIDEO_TYPE));
            video.setIso6391(cursor.getString(MovieUtils.COL_VIDEO_LANG));
            video.setName(cursor.getString(MovieUtils.COL_VIDEO_NAME));
            video.setKey(cursor.getString(MovieUtils.COL_VIDEO_KEY));
            video.setSite(cursor.getString(MovieUtils.COL_VIDEO_SITE));
            video.setSize(cursor.getLong(MovieUtils.COL_VIDEO_SIZE));
        }else{
            Log.i(LOG_TAG,"Inside getVideoFromCursor(),invalid Row at given position");
        }
        return video;
    }

    public static Review getReviewFromCursor(Cursor cursor,int pos){
        Review review = new Review();
        if(cursor.moveToPosition(pos)){
            review.setId(cursor.getString(MovieUtils.COL_REVIEW_TMDB_REVIEW_ID));
            review.setAuthor(cursor.getString(MovieUtils.COL_REVIEW_AUTHOR));
            review.setContent(cursor.getString(MovieUtils.COL_REVIEW_CONTENT));
            review.setUrl(cursor.getString(MovieUtils.COL_REVIEW_REVIEW_LINK));
        }else {
            Log.i(LOG_TAG,"Inside getReviewFromCursor, invalid Row at given position");
        }

        return review;
    }


    public static final String[] MOVIE_COLUMN ={
            FavoriteMoviesContract.MovieEntry.TABLE_NAME + "."+ FavoriteMoviesContract.MovieEntry._ID,
            FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,
            FavoriteMoviesContract.MovieEntry.COLUMN_TITLE,
            FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY,
            FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
            FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW,
            FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            FavoriteMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TMDB_MOVIE_ID =1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_MOVIE_RELEASE_DATE =3;
    public static final int COL_MOVIE_POPULARITY = 4;
    public static final int COL_MOVIE_VOTE_AVERAGE = 5;
    public static final int COL_MOVIE_VOTE_COUNT = 6;
    public static final int COL_MOVIE_OVERVIEW = 7;
    public static final int COL_MOVIE_POSTER_PATH = 8;
    public static final int COL_MOVIE_BACKDROP_PATH = 9;

    public static final String[] REVIEW_COLUMN ={
            FavoriteMoviesContract.ReviewEntry.TABLE_NAME + "." + FavoriteMoviesContract.ReviewEntry._ID,
            FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID,
            FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID,
            FavoriteMoviesContract.ReviewEntry.COLUMN_AUTHOR,
            FavoriteMoviesContract.ReviewEntry.COLUMN_CONTENT,
            FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_LINK
            //,
            //FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID

    };

    public static final int COL_REVIEW_ID =0;
    public static final int COL_REVIEW_TMDB_REVIEW_ID = 1;
    public static final int COL_REVIEW_MOVIE_ID = 2;
    public static final int COL_REVIEW_AUTHOR = 3;
    public static final int COL_REVIEW_CONTENT = 4;
    public static final int COL_REVIEW_REVIEW_LINK = 5;

    public static final String[] VIDEO_COLUMN ={
            FavoriteMoviesContract.VideoEntry.TABLE_NAME+"."+FavoriteMoviesContract.VideoEntry._ID,
            FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID,
            FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID,
            FavoriteMoviesContract.VideoEntry.COLUMN_LANG,
            FavoriteMoviesContract.VideoEntry.COLUMN_KEY,
            FavoriteMoviesContract.VideoEntry.COLUMN_NAME,
            FavoriteMoviesContract.VideoEntry.COLUMN_SITE,
            FavoriteMoviesContract.VideoEntry.COLUMN_SIZE,
            FavoriteMoviesContract.VideoEntry.COLUMN_TYPE
            //,
           // FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID
    };

    public static final int COL_VIDEO_ID = 0;
    public static final int COL_VIDEO_TMDB_VIDEO_ID = 1;
    public static final int COL_VIDEO_MOVIE_ID = 2;
    public static final int COL_VIDEO_LANG = 3;
    public static final int COL_VIDEO_KEY = 4;
    public static final int COL_VIDEO_NAME = 5;
    public static final int COL_VIDEO_SITE =6;
    public static final int COL_VIDEO_SIZE = 7;
    public static final int COL_VIDEO_TYPE = 8;

    public static long getMovieRowFromDatabase(Context mContext,long tmdbMovieId){
        Cursor movieCursor = mContext.getContentResolver().query(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                new String[]{FavoriteMoviesContract.MovieEntry._ID},
                FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID + " = ? ",
                new String[]{Long.toString(tmdbMovieId)},
                null
        );
        if(movieCursor.moveToFirst()){
            Log.i(LOG_TAG,"Inside getMovieRowIdFromDatabase(). \nMovie exist? " +movieCursor.moveToFirst());

            return movieCursor.getLong(MovieUtils.COL_MOVIE_ID);
        }
        return -1L;
    }

    public static long addMovieToFavorite(Context mContext, Movie movie){
        long movieRowId = MovieUtils.getMovieRowFromDatabase(mContext,movie.getId());
        if(movieRowId == -1){
            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID,movie.getId());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE,movie.getTitle());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getReleaseDate());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_VOTE_COUNT,movie.getVoteCount());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POPULARITY,movie.getPopularity());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_OVERVIEW,movie.getOverview());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER_PATH,movie.getPosterPath());
            movieValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,movie.getBackdropPath());

            Uri insertedUri = mContext.getContentResolver().insert(
                    FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            movieRowId = ContentUris.parseId(insertedUri);
        }
        return movieRowId;
    }

    public static int addVideosRelatedToMovie(Context mContext,long movieRowId,ArrayList<Video> videos){
        Vector<ContentValues> contentValuesVector = new Vector<>(videos.size());

        for (int i =0 ;i<videos.size();i++){
            ContentValues videoValue = new ContentValues();

            String vidTmdbId = videos.get(i).getId();
            long vidMovieId= movieRowId;
            String vidLang = videos.get(i).getIso6391();
            String vidKey = videos.get(i).getKey();
            String vidName = videos.get(i).getName();
            String vidSite = videos.get(i).getSite();
            long vidSize = videos.get(i).getSize();
            String vidType = videos.get(i).getType();

            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_TMDB_VIDEO_ID,vidTmdbId);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID,vidMovieId);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_LANG,vidLang);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_KEY,vidKey);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_NAME,vidName);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_SITE,vidSite);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_SIZE,vidSize);
            videoValue.put(FavoriteMoviesContract.VideoEntry.COLUMN_TYPE,vidType);

            contentValuesVector.add(videoValue);
        }
        int videoInserted = 0;
        if(contentValuesVector.size()>0){
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            videoInserted = mContext.getContentResolver().bulkInsert(
                    FavoriteMoviesContract.VideoEntry.CONTENT_URI, cvArray);
        }
        return videoInserted;
    }

    public static int addReviewsRelatedToMovie(Context mContext,long movieRowId,ArrayList<Review> reviews){
        Vector<ContentValues> contentValuesVector = new Vector<>(reviews.size());

        for (int i =0 ;i<reviews.size();i++){
            ContentValues reviewsValue = new ContentValues();
            String revTmdbId = reviews.get(i).getId();
            long revMovieId = movieRowId;
            String revAuthor = reviews.get(i).getAuthor();
            String revContent = reviews.get(i).getContent();
            String revLink = reviews.get(i).getUrl();

            reviewsValue.put(FavoriteMoviesContract.ReviewEntry.COLUMN_TMDB_REVIEW_ID,revTmdbId);
            reviewsValue.put(FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID,revMovieId);
            reviewsValue.put(FavoriteMoviesContract.ReviewEntry.COLUMN_AUTHOR,revAuthor);
            reviewsValue.put(FavoriteMoviesContract.ReviewEntry.COLUMN_CONTENT,revContent);
            reviewsValue.put(FavoriteMoviesContract.ReviewEntry.COLUMN_REVIEW_LINK,revLink);
            contentValuesVector.add(reviewsValue);
        }
        int reviewsInserted = 0;
        if(contentValuesVector.size()>0){
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            reviewsInserted = mContext.getContentResolver().bulkInsert(
                    FavoriteMoviesContract.ReviewEntry.CONTENT_URI, cvArray);
        }
        return reviewsInserted;
    }

    public static int  deleteMovieFromFavorite(Context mContext, long movieRowId){

        return mContext.getContentResolver().delete(
                FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID +" = ? ",
                new String[]{Long.toString(movieRowId)}
        );
    }

    public static int deleteVideosFromDatabase(Context mContext, long movieRowId){
        return mContext.getContentResolver().delete(
                FavoriteMoviesContract.VideoEntry.CONTENT_URI,
                FavoriteMoviesContract.VideoEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{Long.toString(movieRowId)}
        );
    }

    public static int deleteReviewsFromDatabase(Context mContext, long movieRowId){
        return mContext.getContentResolver().delete(
                FavoriteMoviesContract.ReviewEntry.CONTENT_URI,
                FavoriteMoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{Long.toString(movieRowId)}
        );
    }

}
