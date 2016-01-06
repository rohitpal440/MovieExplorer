package com.technobells.rohit.movieexplorer.utilities;

import com.technobells.rohit.movieexplorer.model.JsonRequestDiscoverMovieResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieCreditsResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieReviewResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieVideoResult;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by rohit on 30/12/15.
 */
public interface MovieApiService {

    @GET("discover/movie")
    Call<JsonRequestDiscoverMovieResult> getDiscoverMovieFeed(@Query("sort_by") String sortBy,
                                                              @Query("page") int page,
                                                              @Query("vote_count.gte") String minVoteCount,
                                                              @Query("api_key") String apiKey
    );

    @GET("movie/{id}/reviews")
    Call<JsonRequestMovieReviewResult> getMovieReviewFeed(@Path("id") String movieId,
                                                          @Query("api_key") String apiKey
    );

    @GET("movie/{id}/videos")
    Call<JsonRequestMovieVideoResult> getMovieVideoFeed(@Path("id") String movieId,
                                                        @Query("api_key") String apiKey
    );

    @GET("movie/{id}/similar")
    Call<JsonRequestDiscoverMovieResult> getSimilarMovieFeed(@Path("id") String movieId,
                                                        @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<JsonRequestMovieCreditsResult> getMovieCreditsFeed(@Path("id") String movieId,
                                                            @Query("api_key") String apiKey);


}
