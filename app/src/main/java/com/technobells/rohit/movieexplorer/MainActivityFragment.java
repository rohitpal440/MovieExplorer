package com.technobells.rohit.movieexplorer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.technobells.rohit.movieexplorer.adapter.MovieAdapter;
import com.technobells.rohit.movieexplorer.entity.JsonRequestDiscoverMovieResult;
import com.technobells.rohit.movieexplorer.entity.Movie;
import com.technobells.rohit.movieexplorer.utilities.MovieApiService;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class
MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private Boolean FAVORITE_FLAG = false;
    private String SAVED_MOVIE_LIST = "savedMovieList";
    private String SAVED_SORT_PREF = "savedSortPref";
    private String SAVED_PAGE_NO = "page";
    private int MOVIE_LOADER = 0;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Movie> movieArrayList= new ArrayList<>();
    public static boolean load = true;

    private String sortBy="popularity.desc"; //Default sorting order
    private int minVoteCount = 1000;
    private int page = 1;

    public MainActivityFragment() {
    }

    /*
    Fetch data from internet on onCreate Activity or whenever is required
 */
    private void updateMoviePoster(){
        load = false;
        MovieApiService moviesApiService = MovieUtils.retrofitInstance.create(MovieApiService.class);
        Call<JsonRequestDiscoverMovieResult> call = moviesApiService.getDiscoverMovieFeed(
                                                            sortBy,
                                                            page,
                                                            minVoteCount,
                                                            BuildConfig.MY_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<JsonRequestDiscoverMovieResult>(){
            @Override
            public void onResponse(Response<JsonRequestDiscoverMovieResult> response, Retrofit retrofit1) {
                JsonRequestDiscoverMovieResult jsonRequestDiscoverMovieResult = response.body();
                if (jsonRequestDiscoverMovieResult != null) {
                    ArrayList<Movie> results = (ArrayList<Movie>) jsonRequestDiscoverMovieResult.getResults();

                    movieArrayList.addAll(results);
                    Log.i(LOG_TAG,"Making Request to network with page id " + page
                            +"\nMovie Array list contains :" + movieArrayList.size());

                    //if(page ==1) movieAdapter.clear();
                    movieAdapter.addAll(results);
                    load = true;
                }else{
                    Log.e(LOG_TAG,"Getting null object of JsonRequestDiscoverMovieResult");
                    try {
                        String str =response.errorBody().string();
                        Log.e(LOG_TAG,str);

                    }catch (IOException e){
                        Log.e(LOG_TAG,"IOexception inside the response errorBody");
                    }

                }
            }
            @Override
            public void onFailure(Throwable t){
                Log.i(LOG_TAG,"Retrofit Response failure");
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        /*
        get the dimension of device screen
         */
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(),metrics.widthPixels/300);

        movieAdapter = new MovieAdapter(getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.grid_recycler_view);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        /*
            Creating the listener for infinite scroll.
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                if(dy>0 && load){///vertical scroll check for downward scroll

                    if(gridLayoutManager.getChildCount() + gridLayoutManager.findFirstVisibleItemPosition()
                            >= gridLayoutManager.getItemCount() ){

                        page++;
                        Log.i(LOG_TAG,"At the End of GridList. \nLoading more items with page No. " + page);
                        updateMoviePoster();
                    }
                }
            }
        });

        /*
            lets set the on item click listener
         */



        if(savedInstanceState==null
                || ! savedInstanceState.containsKey(SAVED_MOVIE_LIST)
                || ! savedInstanceState.containsKey(SAVED_SORT_PREF)
                || ! savedInstanceState.containsKey(SAVED_PAGE_NO)){

            updateMoviePoster();
        }else{
            Log.i(LOG_TAG,"Retaining from Saved instances ");
            movieArrayList = savedInstanceState.getParcelableArrayList(SAVED_MOVIE_LIST);
            movieAdapter.addAll( movieArrayList);
            sortBy=savedInstanceState.getString(SAVED_SORT_PREF);
            page = savedInstanceState.getInt(SAVED_PAGE_NO);
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        String sortByPopularity="popularity.desc";
        String sortByRating="vote_average.desc";

        switch(id){
           case R.id.menu_item_favorite:
//                if( !FAVORITE_FLAG ){
//                    FAVORITE_FLAG = true;
//                    showFavoriteMovies();
//                } else{
//                    FAVORITE_FLAG = false;
//                    showAllMovies();
//                }
                return true;

            case R.id.action_sort_by_popularity:
                if(sortBy.equals(sortByRating)){
                    sortBy=sortByPopularity;
                    page =1;
                    movieArrayList.clear();
                    movieAdapter.clear();
                    updateMoviePoster();
                }
                return true;

            case R.id.action_sort_by_rating:
                if(sortBy.equals(sortByPopularity)) {
                    sortBy=sortByRating;
                    page =1;
                    movieArrayList.clear();
                    movieAdapter.clear();
                    updateMoviePoster();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onSaveInstanceState(Bundle saveState){
        super.onSaveInstanceState(saveState);
        Log.i(LOG_TAG,"Saving instances before destroying the activity");
        //check if movieOldClassArrayList is not empty,in this way we don't get null pointer exception when activity is recreated
        if(movieArrayList !=null) saveState.putParcelableArrayList(SAVED_MOVIE_LIST, movieArrayList);
        saveState.putString(SAVED_SORT_PREF,sortBy);
        saveState.putInt(SAVED_PAGE_NO,page);
    }

}
