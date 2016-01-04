package com.technobells.rohit.movieexplorer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technobells.rohit.movieexplorer.adapter.MovieDetailAdapter;
import com.technobells.rohit.movieexplorer.entity.Cast;
import com.technobells.rohit.movieexplorer.entity.JsonRequestDiscoverMovieResult;
import com.technobells.rohit.movieexplorer.entity.JsonRequestMovieCreditsResult;
import com.technobells.rohit.movieexplorer.entity.JsonRequestMovieReviewResult;
import com.technobells.rohit.movieexplorer.entity.JsonRequestMovieVideoResult;
import com.technobells.rohit.movieexplorer.entity.Movie;
import com.technobells.rohit.movieexplorer.entity.Review;
import com.technobells.rohit.movieexplorer.entity.SectionDataModel;
import com.technobells.rohit.movieexplorer.entity.Video;
import com.technobells.rohit.movieexplorer.utilities.MovieApiService;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private MovieDetailAdapter mAdapter;
    private Movie movie;
    private ArrayList<Object> movieItemList = new ArrayList<>();

    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Cast> casts = new ArrayList<>();
    private ArrayList<Movie> similarMovies = new ArrayList<>();

    private final String SAVED_MOVIE_ITEM = "movieItem";
    private final String SAVED_VIDEO_LIST = "videos";
    private final String SAVED_REVIEW_LIST = "reviews";
    @Bind(R.id.fragment_movie_detail_recycler_view)
    RecyclerView recyclerView;

    int progress =0;
    public MovieDetailActivityFragment() {
    }

    private void updateMovieDetailView() {
        Log.i(LOG_TAG, "Fetching the Related data>>>>>>>>>");
        Object temp = movie;
        mAdapter.appendObject(temp,0);
        fetchVideos();
        fetchCast();
        fetchReviews();
        fetchSimilarMovies();
    }

    private void fetchVideos(){
        MovieApiService moviesApiService = MovieUtils.retrofitInstance.create(MovieApiService.class);
        Call<JsonRequestMovieVideoResult> call = moviesApiService.getMovieVideoFeed(
                Long.toString(movie.getId()),BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<JsonRequestMovieVideoResult>(){
            @Override
            public void onResponse(Response<JsonRequestMovieVideoResult> response, Retrofit retrofit1) {
                JsonRequestMovieVideoResult jsonRequestMovieVideoResult = response.body();
                Log.i(LOG_TAG,"Got JsonRequestMovieVideo Result with id : " + jsonRequestMovieVideoResult.getId());
                if (jsonRequestMovieVideoResult != null) {
                    ArrayList<Video> results = (ArrayList<Video>) jsonRequestMovieVideoResult.getVideos();
                    if(results.size()>0){
                        ArrayList<Object> temp = new ArrayList<Object>();
                        temp.add("Related Videos");
                        temp.addAll(results);
                        Log.i(LOG_TAG,"Got "+results.size()+" Videos.\n Inserting Video Section with :"+temp.size()+" values.");
                        mAdapter.appendObjectList(temp,1);
                        videos.clear();
                        videos.addAll(results);
                    }
                }else{
                    Log.e(LOG_TAG,"Getting null object of (VIDEO) JsonRequestMovieVideoResult");
                    try {
                        String str =response.errorBody().string();
                        Log.i(LOG_TAG,"Retrofit Review Response error : "+str);

                    }catch (IOException e){
                        Log.e(LOG_TAG,"IOexception inside the response");
                    }
                }

            }
            @Override
            public void onFailure(Throwable t){
                Log.i(LOG_TAG,"Retrofit Response failure for Video Fetch Request");

            }
        });
    }

    private void fetchCast(){
        MovieApiService moviesApiService = MovieUtils.retrofitInstance.create(MovieApiService.class);
        Call<JsonRequestMovieCreditsResult> call = moviesApiService.getMovieCreditsFeed(
                Long.toString(movie.getId()),BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<JsonRequestMovieCreditsResult>(){
            @Override
            public void onResponse(Response<JsonRequestMovieCreditsResult> response, Retrofit retrofit1) {
                JsonRequestMovieCreditsResult jsonRequestMovieCreditsResult = response.body();
                if (jsonRequestMovieCreditsResult != null) {
                    ArrayList<Cast> results = (ArrayList<Cast>) jsonRequestMovieCreditsResult.getCast();
                    if(results.size()>0){

                        SectionDataModel castSection = new SectionDataModel();
                        castSection.setSectionTitle("Star Cast ");
                        castSection.setAllItemsInSection(results,null);
                        Object temp =  castSection;
                        Log.i(LOG_TAG,"Got "+results.size()+" Cast Members.\n Inserting Cast Section");
                        mAdapter.appendObject(temp,(videos.size()>0?videos.size()+1:0)+1);
                        casts.addAll(results);
                    }
                }else{
                    Log.e(LOG_TAG,"Getting null object of (Cast) JsonRequestMovieReviewResult ");
                    try {
                        String str =response.errorBody().string();
                        Log.i(LOG_TAG,"Retrofit Cast Response error : "+str);

                    }catch (IOException e){
                        Log.e(LOG_TAG,"IOexception inside the response");
                    }

                }

            }
            @Override
            public void onFailure(Throwable t){
                Log.i(LOG_TAG,"Retrofit Response failure for Cast Fetch Request");
//                if(++progress >= 4){
//                    swipeRefreshLayout.setRefreshing(false);
//                }
            }
        });
    }

    private void fetchReviews(){
        MovieApiService moviesApiService = MovieUtils.retrofitInstance.create(MovieApiService.class);
        Call<JsonRequestMovieReviewResult> call = moviesApiService.getMovieReviewFeed(
               Long.toString(movie.getId()),BuildConfig.MY_MOVIE_DB_API_KEY);

        call.enqueue(new Callback<JsonRequestMovieReviewResult>(){
            @Override
            public void onResponse(Response<JsonRequestMovieReviewResult> response, Retrofit retrofit1) {
                JsonRequestMovieReviewResult jsonRequestMovieReviewResult = response.body();
                if (jsonRequestMovieReviewResult != null) {
                    ArrayList<Review> results = (ArrayList<Review>) jsonRequestMovieReviewResult.getReviews();
                    if(results.size() == 0) results.add(MovieUtils.NO_REVIEW);

                        ArrayList<Object> temp = new ArrayList<Object>();
                        temp.add("Reviews");
                        temp.addAll(results);
                        Log.i(LOG_TAG,"Got "+results.size()+" Reviews.\nInserting Review Section with "+temp.size()+" values.");
                        mAdapter.appendObjectList(temp,(videos.size()>0?videos.size()+1:0)+(casts.size() > 0 ?1:0)+1);
                        reviews.clear();
                        reviews.addAll(results);

                }else{
                    Log.e(LOG_TAG,"Getting null object of (REVIEW) JsonRequestMovieReviewResult ");
                    try {
                        String str =response.errorBody().string();
                        Log.i(LOG_TAG,"Retrofit Review Response error : "+str);

                    }catch (IOException e){
                        Log.e(LOG_TAG,"IOexception inside the response");
                    }
                }

            }
            @Override
            public void onFailure(Throwable t){
                Log.i(LOG_TAG,"Retrofit Response failure for Review Fetch Request");
            }
        });
    }

    private void fetchSimilarMovies(){
        MovieApiService moviesApiService = MovieUtils.retrofitInstance.create(MovieApiService.class);
        Call<JsonRequestDiscoverMovieResult> call = moviesApiService.getSimilarMovieFeed(
                Long.toString(movie.getId()),
                BuildConfig.MY_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<JsonRequestDiscoverMovieResult>(){
            @Override
            public void onResponse(Response<JsonRequestDiscoverMovieResult> response, Retrofit retrofit1) {
                JsonRequestDiscoverMovieResult jsonRequestDiscoverMovieResult = response.body();
                if (jsonRequestDiscoverMovieResult != null) {
                    ArrayList<Movie> results = (ArrayList<Movie>) jsonRequestDiscoverMovieResult.getResults();

                    if(results.size()>0){
                        SectionDataModel similarMoviesSection = new SectionDataModel();
                        similarMoviesSection.setSectionTitle("Similar Movies");
                        similarMoviesSection.setAllItemsInSection(null,results);

                        Object temp = similarMoviesSection;
                        Log.i(LOG_TAG,"Got : " + results.size() + " similar movies");
                        mAdapter.appendObject(temp,(videos.size()>0?videos.size()+1:0)+(casts.size()>0?1:0)+(reviews.size()>0?reviews.size()+1:0)+1);
                        similarMovies.addAll(results);
                    }
                }else{
                    Log.e(LOG_TAG,"Getting null object of (Similar Movies) JsonRequestMovieReviewResult ");
                    try {
                        String str =response.errorBody().string();
                        Log.i(LOG_TAG,"Retrofit Similar Movies Response error : "+str);

                    }catch (IOException e){
                        Log.e(LOG_TAG,"IOexception inside the response");
                    }
                }

            }
            @Override
            public void onFailure(Throwable t){
                Log.i(LOG_TAG,"Retrofit Response failure for Similar Movies Request");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movie = getActivity().getIntent().getParcelableExtra("movieTag");

        mAdapter = new MovieDetailAdapter(getActivity());
        ButterKnife.bind(this,rootView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.i(LOG_TAG,"Refreshing");
//                videos.clear();
//                reviews.clear();
//                casts.clear();
//                similarMovies.clear();
//                mAdapter.clear();
//                updateMovieDetailView();
//            }
//        });

        if(savedInstanceState==null
                || ! savedInstanceState.containsKey(SAVED_VIDEO_LIST)
                || ! savedInstanceState.containsKey(SAVED_REVIEW_LIST)  ){
            updateMovieDetailView();
        }else{
            Log.i(LOG_TAG,"Retaining from Saved instances ");
            videos = savedInstanceState.getParcelableArrayList(SAVED_VIDEO_LIST);
            reviews = savedInstanceState.getParcelableArrayList(SAVED_REVIEW_LIST);
            configureMovieItemList();
        }

        return rootView;
    }

    private void configureMovieItemList(){
        Log.i(LOG_TAG,"Configuring with "+videos.size()+" Videos and "+reviews.size() + " reviews ");
        movieItemList.clear();
        movieItemList.add(movie);
        movieItemList.add("Related Videos");
        movieItemList.addAll(videos);
        movieItemList.add("Reviews");
        movieItemList.addAll(reviews);
        mAdapter.clear();
        mAdapter.appendObjectList(movieItemList,0);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG,"Saving instances before destroying the activity");
        //check if movieOldClassArrayList is not empty,in this way we don't get null pointer exception when activity is recreated
        if(videos !=null && reviews != null){
            outState.putParcelableArrayList(SAVED_VIDEO_LIST,videos);
            outState.putParcelableArrayList(SAVED_REVIEW_LIST,reviews);
        }

    }
}
