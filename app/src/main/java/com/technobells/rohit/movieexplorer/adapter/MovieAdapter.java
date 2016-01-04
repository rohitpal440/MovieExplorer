package com.technobells.rohit.movieexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.technobells.rohit.movieexplorer.utilities.ItemClickListener;
import com.technobells.rohit.movieexplorer.MovieDetailActivity;
import com.technobells.rohit.movieexplorer.R;
import com.technobells.rohit.movieexplorer.entity.Movie;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rohit on 30/12/15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> movies;
    private Context mContext;

    public MovieAdapter(Context context){
        this.mContext = context;
        movies = new ArrayList<Movie>();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        private ItemClickListener clickListener;
        @Bind(R.id.movie_card_item_image)
        ImageView posterImage;
        @Bind(R.id.movie_card_item_rating_text)
        TextView ratingTv;
        @Bind(R.id.movie_card_item_release_date)
        TextView releaseDateTv;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view){
            this.clickListener.onClick(view,getPosition(),false);
        }

        @Override
        public boolean onLongClick(View view){
            clickListener.onClick(view,getPosition(),true);
            return true;
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);
        return new ViewHolder(v);

    }

    public void addAll(ArrayList<Movie> list){
        movies.addAll(list);
        Log.i(LOG_TAG,"Adding all "+list.size() + " movies to adapter.\n Now Adapter Contains "+movies.size()+" movies");
        notifyDataSetChanged();
    }

    /*
    clear data in MovieAdapter
     */
    public void clear(){
        movies.clear();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Movie movie = movies.get(position);
        holder.ratingTv.setText(String.format("%.1f",movie.getVoteAverage()));
        String releaseDate = MovieUtils.formateDate(movie.getReleaseDate(),"yyyy-MM-dd","yyyy");
        holder.releaseDateTv.setText(releaseDate);

        /* Possible Image size are "w92", "w154", "w185", "w342", "w500", "w780", or "original" */
        final String SIZE="w185/";
        //Glide.with(mContext).load( MovieUtils.BASE_URL_IMAGE+ SIZE + movie.getPosterPath()).into(holder.posterImage);
        holder.posterImage.setAdjustViewBounds(true);
        Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + SIZE + movie.getPosterPath()).placeholder(R.drawable.placeholder).into(holder.posterImage);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    Toast.makeText(mContext,movie.getTitle()+" is Long pressed at position "+ position,
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(mContext,MovieDetailActivity.class);
                    intent.putExtra("movieTag",movie);
                    view.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}
