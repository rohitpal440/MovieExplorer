package com.technobells.rohit.movieexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.CallBack{
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getResources().getBoolean(R.bool.twoPane)){
            mTwoPane = true;
            MovieUtils.TWO_PANE = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment,new MovieDetailActivityFragment(),"DFTAG")
                        .commit();
            }

        }else {
            mTwoPane = false;

        }
        Log.i(LOG_TAG,"Tablet Detected:>>>>>>\n>>>\n>>>\n>>>\n>>>\n>>>\n>>\n"+mTwoPane);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemSelected(Movie movie,boolean FavState){

        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable("movieTag",movie);
            args.putBoolean("FavFlag", FavState);
            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment,fragment,"DFTAG")
                    .addToBackStack(null)
                    .commit();
            Log.i(LOG_TAG,"Inside onItemSelected Method : sending data to Detail Frag");
        }else {
            Intent intent = new Intent(this,MovieDetailActivity.class);
            intent.putExtra("movieTag",movie);
            intent.putExtra("FavFlag",FavState);
            startActivity(intent);
        }
    }
}
