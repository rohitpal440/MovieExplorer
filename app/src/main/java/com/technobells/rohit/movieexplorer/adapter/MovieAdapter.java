package com.technobells.rohit.movieexplorer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technobells.rohit.movieexplorer.MainActivityFragment;
import com.technobells.rohit.movieexplorer.utilities.ItemClickListener;
import com.technobells.rohit.movieexplorer.MovieDetailActivity;
import com.technobells.rohit.movieexplorer.R;
import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.io.File;
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
    private boolean mDataValid;
    private int mRowIdColumn = MovieUtils.COL_MOVIE_ID;
    private Cursor mCursor;
    private DataSetObserver mDataSetObserver;
    private Activity mActivity;
    public MovieAdapter(Activity activity){
        this.mContext = activity;
        mActivity= activity;
        movies = new ArrayList<>();

        mCursor = null;
        mDataValid = false;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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


    public Cursor getCursor(){
        return mCursor;
    }

    public void setCursor(Cursor cursor){
        mCursor = cursor;
        if(mCursor!=null){
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        notifyDataSetChanged();
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

    public void clear(){
        movies.clear();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie;
        if (MovieUtils.FAVORITE_FLAG){
            mCursor.moveToPosition(position);
            movie = MovieUtils.getMovieFromCursor(mCursor, position);
        }else{
            movie = movies.get(position);
        }
        holder.ratingTv.setText(String.format("%.1f",movie.getVoteAverage()));
        String releaseDate = MovieUtils.formateDate(movie.getReleaseDate(),"yyyy-MM-dd","yyyy");
        holder.releaseDateTv.setText(releaseDate);

        /* Possible Image size are "w92", "w154", "w185", "w342", "w500", "w780", or "original" */
        final String SIZE="w185/";
        //Glide.with(mContext).load( MovieUtils.BASE_URL_IMAGE+ SIZE + movie.getPosterPath()).into(holder.posterImage);
        holder.posterImage.setAdjustViewBounds(true);
        if(MovieUtils.FAVORITE_FLAG) {
            Picasso.with(mContext).load(new File( mContext.getFilesDir().getPath() + "/moviePoster/"+movie.getPosterPath())).placeholder(R.drawable.placeholder).into(holder.posterImage);
        }else {
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + SIZE + movie.getPosterPath()).placeholder(R.drawable.placeholder).into(holder.posterImage);
        }
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    Toast.makeText(mContext,movie.getTitle()+" is Long pressed at position "+ position,
                            Toast.LENGTH_SHORT).show();
                }else{

                    ((MainActivityFragment.CallBack)mActivity).onItemSelected(movie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if(!MovieUtils.FAVORITE_FLAG) return movies.size();
        if(mDataValid && mCursor !=null){
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds){
        if(MovieUtils.FAVORITE_FLAG){
            super.setHasStableIds(true);
        }else {
            super.setHasStableIds(false);
        }
    }

    @Override
    public long getItemId(int position){
        if(!MovieUtils.FAVORITE_FLAG) return -1;
        if(mDataValid && mCursor != null && mCursor.moveToPosition(position)){
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

}
