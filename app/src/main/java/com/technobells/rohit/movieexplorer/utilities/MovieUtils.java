package com.technobells.rohit.movieexplorer.utilities;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.technobells.rohit.movieexplorer.model.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
    public static final float GRID_COLUMN_WIDTH = 160;
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
}
