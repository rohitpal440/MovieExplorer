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

import com.squareup.picasso.Picasso;
import com.technobells.rohit.movieexplorer.MainActivityFragment;
import com.technobells.rohit.movieexplorer.MovieDetailActivity;
import com.technobells.rohit.movieexplorer.MovieDetailActivityFragment;
import com.technobells.rohit.movieexplorer.R;
import com.technobells.rohit.movieexplorer.model.Cast;
import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rohit on 1/1/16.
 */
public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String LOG_TAG = HorizontalRecyclerAdapter.class.getSimpleName();
    private ArrayList<Object> itemsConnectedtoMovie = new ArrayList<>();
    private Context mContext;

    public HorizontalRecyclerAdapter(Context mContext){
        this.mContext=mContext;
    }

    public HorizontalRecyclerAdapter(Context mContext,ArrayList<Object> list){
        this.mContext = mContext;
        itemsConnectedtoMovie.addAll(list);
        notifyItemRangeInserted(itemsConnectedtoMovie.size() - list.size(),itemsConnectedtoMovie.size());
    }

    public void addAll(ArrayList<Object> objects){
        itemsConnectedtoMovie.addAll(objects);
        notifyDataSetChanged();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.cast_card_poster)
        ImageView poster;
        @Bind(R.id.cast_card_name)
        TextView name;
        @Bind(R.id.cast_card_character)
        TextView character;
        int pos;
        Context context;
        public CastViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public static class SimilarMovieViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.movie_card_item_image)
        ImageView poster;
        @Bind(R.id.movie_card_item_rating_text)
        TextView rating;
        @Bind(R.id.movie_card_item_release_date)
        TextView releaseDate;
        Context context;
        int pos;
        Movie movie;
        public SimilarMovieViewHolder(View view){
            super(view);

            ButterKnife.bind(this,view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do something
                    //Toast.makeText(context,"You clicked the similar Movies",Toast.LENGTH_SHORT).show();
                    if(context.getResources().getBoolean(R.bool.twoPane)){
                        ((MainActivityFragment.CallBack) context).onItemSelected(movie,MovieDetailActivityFragment.FAVORITE);
                    }else {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra("movieTag", movie);
                        intent.putExtra("FavFlag", MovieDetailActivityFragment.FAVORITE);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.header_item_title)
        TextView title;

        public HeaderViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    @Override
    public int getItemCount() {
        return itemsConnectedtoMovie.size();
    }

    @Override
    public int getItemViewType(int position){
        Object object = itemsConnectedtoMovie.get(position);
        if(object instanceof Movie){

            return MovieUtils.SIMILAR;
        }else if(object instanceof Cast){

            return MovieUtils.CAST;
        }
        Log.e(LOG_TAG,"Unable to determine the object type");
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){

            case MovieUtils.CAST:{
                View view = inflater.inflate(R.layout.cast_card,parent,false);
                viewHolder = new CastViewHolder(view);
                break;
            }
            case MovieUtils.SIMILAR:{
                View view = inflater.inflate(R.layout.movie_card,parent,false);
                viewHolder = new SimilarMovieViewHolder(view);
                break;
            }

            default:{
                View view = inflater.inflate(R.layout.header_item,parent,false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int pos){
        switch (holder.getItemViewType()){
            case MovieUtils.CAST:
                CastViewHolder castViewHolder = (CastViewHolder) holder;
                configureCastViewHolder(castViewHolder,pos);
                break;

            case MovieUtils.SIMILAR:
                SimilarMovieViewHolder similarMovieViewHolder = (SimilarMovieViewHolder) holder;
                configureSimilarMovieViewHolder(similarMovieViewHolder,pos);
                break;
        }

    }

    public void configureCastViewHolder(CastViewHolder holder,int pos){
        Cast cast = (Cast) itemsConnectedtoMovie.get(pos);
        holder.name.setText(cast.getName());
        holder.character.setText(cast.getCharacter());

        holder.pos = pos;
        holder.context = mContext;
        if(MovieUtils.SCREEN_DENSITY > 1.0) {
            holder.poster.setAdjustViewBounds(true);
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE+"w184/"+ cast.getProfilePath()).
                    placeholder(R.drawable.placeholder)
                    .fit().into(holder.poster);
        }else {
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE+"w154/"+ cast.getProfilePath()).
                    placeholder(R.drawable.placeholder)
                    .fit().into(holder.poster);
        }
    }

    public void configureSimilarMovieViewHolder(SimilarMovieViewHolder holder,int pos){
        Movie movie = (Movie) itemsConnectedtoMovie.get(pos);
        holder.rating.setText(String.format("%.1f",movie.getVoteAverage()));
        holder.releaseDate.setText(MovieUtils.formatDate(movie.getReleaseDate(),"yyyy-MM-dd","yyyy"));

        holder.context = mContext;
        holder.pos = pos;
        holder.movie = movie;
        if(MovieUtils.SCREEN_DENSITY > 1.0){
           // holder.poster.setAdjustViewBounds(true);
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE +"w185/"+movie.getPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .into(holder.poster);
        }else {
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE +"w154/"+movie.getPosterPath())
                    .placeholder(R.drawable.placeholder).into(holder.poster);
        }
    }

}
