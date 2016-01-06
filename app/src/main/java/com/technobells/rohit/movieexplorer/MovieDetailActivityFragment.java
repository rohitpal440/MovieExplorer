package com.technobells.rohit.movieexplorer;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.technobells.rohit.movieexplorer.adapter.MovieDetailAdapter;
import com.technobells.rohit.movieexplorer.data.FavoriteMoviesContract;
import com.technobells.rohit.movieexplorer.model.Cast;
import com.technobells.rohit.movieexplorer.model.JsonRequestDiscoverMovieResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieCreditsResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieReviewResult;
import com.technobells.rohit.movieexplorer.model.JsonRequestMovieVideoResult;
import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.model.Review;
import com.technobells.rohit.movieexplorer.model.SectionDataModel;
import com.technobells.rohit.movieexplorer.model.Video;
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
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private MovieDetailAdapter mAdapter;
    private Movie movie;
    private ArrayList<Object> movieItemList = new ArrayList<>();

    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private ArrayList<Cast> casts = new ArrayList<>();
    private ArrayList<Movie> similarMovies = new ArrayList<>();
    private long movieId;
    private final int MOVIE_LOADER =0;
    private final int VIDEO_LOADER =1;
    private final int REVIEW_LOADER = 2;


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
    public Loader<Cursor> onCreateLoader(int id, Bundle arg){

        switch (id){
            case MOVIE_LOADER:
                return new CursorLoader(
                        getActivity(),
                        FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                        MovieUtils.MOVIE_COLUMN,
                        FavoriteMoviesContract.MovieEntry.COLUMN_TMDB_MOVIE_ID+" = ? ",
                        new String[]{Long.toString(movie.getId())},
                        null
                );


            case VIDEO_LOADER:
                return new CursorLoader(
                        getActivity(),
                        FavoriteMoviesContract.VideoEntry.buildVideoMovieUri(movie.getId()),
                        MovieUtils.VIDEO_COLUMN,
                        null,
                        null,
                        null
                );
            case REVIEW_LOADER:
                return new CursorLoader(
                        getActivity(),
                        FavoriteMoviesContract.ReviewEntry.buildReviewMovieUri(movie.getId()),
                        MovieUtils.REVIEW_COLUMN,
                        null,
                        null,
                        null
                );
        }
        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case MOVIE_LOADER:
                //mAdapter.setExtra(data,MovieUtils.MOVIE_DETAIL);
                mAdapter.changeCursor(data);
                break;
            case VIDEO_LOADER:
                //mAdapter.setExtra(data,MovieUtils.VIDEO);
                mAdapter.changeCursor(data);
                break;
            case REVIEW_LOADER:
                //mAdapter.setExtra(data,MovieUtils.REVIEW);
                mAdapter.changeCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(MovieUtils.FAVORITE_FLAG){
            Log.i(LOG_TAG,"Inside onActivityCreated(), Setting up the loaders");

            getLoaderManager().initLoader(MOVIE_LOADER, null, this);
            getLoaderManager().initLoader(VIDEO_LOADER, null, this);
            getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.menu_movie_detail,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_item_favorite:{
                addMovieDetailToDatabase();
                break;
            }
            case R.id.action_settings:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,rootView);
        mAdapter = new MovieDetailAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        movie = getActivity().getIntent().getParcelableExtra("movieTag");
        if(!MovieUtils.FAVORITE_FLAG){
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

    /**
     * Insert Movie and its related Information into the database.
     */
    public void addMovieDetailToDatabase(){
        long movieRowId = MovieUtils.addMovieToFavorite(getContext(),movie);
        int videoRowInserted = MovieUtils.addVideosRelatedToMovie(getContext(),movieRowId,videos);
        int reviewRowInserted = MovieUtils.addReviewsRelatedToMovie(getContext(),movieRowId,reviews);
        Log.i(LOG_TAG,"Movie Inserted with rowId : " + movieRowId +"\n"+videoRowInserted +" Videos Inserted \n"+ reviewRowInserted + " Reviews Inserted");
    }

    public void deleteMovieDetailsFromDatabase(){
        long movieRowId = MovieUtils.getMovieRowFromDatabase(getContext(),movie.getId());
        int movieRowsDeleted = MovieUtils.deleteMovieFromFavorite(getContext(),movieRowId);
        int videoRowsDeleted = MovieUtils.deleteVideosFromDatabase(getContext(),movieRowId);
        int reviewRowsDeleted = MovieUtils.deleteReviewsFromDatabase(getContext(),movieRowId);
        Log.i(LOG_TAG,movieRowsDeleted +" Movie rows deleted\n"+videoRowsDeleted+ " Video rows deleted\n"+reviewRowsDeleted+ " Review rows Deleted");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG,"Saving instances before destroying the activity");
        if(videos !=null && reviews != null){
            outState.putParcelableArrayList(SAVED_VIDEO_LIST,videos);
            outState.putParcelableArrayList(SAVED_REVIEW_LIST,reviews);
        }
    }
}
