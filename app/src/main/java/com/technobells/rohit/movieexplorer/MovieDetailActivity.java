package com.technobells.rohit.movieexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.technobells.rohit.movieexplorer.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null){
            Bundle args = new Bundle();
            Intent intent = getIntent();
            Movie movie = intent.getParcelableExtra("movieTag");
            Boolean Fav = intent.getBooleanExtra("FavFlag",false);
            args.putParcelable("movieTag",movie);
            args.putBoolean("FavFlag",Fav);
            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_fragment,fragment)
                    .commit();
        }
    }

}
