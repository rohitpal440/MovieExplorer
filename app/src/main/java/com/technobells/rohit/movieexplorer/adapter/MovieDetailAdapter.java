package com.technobells.rohit.movieexplorer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;
import com.technobells.rohit.movieexplorer.BuildConfig;
import com.technobells.rohit.movieexplorer.R;
import com.technobells.rohit.movieexplorer.model.SectionDataModel;
import com.technobells.rohit.movieexplorer.model.Movie;
import com.technobells.rohit.movieexplorer.model.Review;
import com.technobells.rohit.movieexplorer.model.Video;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rohit on 31/12/15.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Object> movieItems;

    private MergeCursor mergeCursor;
    private Cursor movieCursor;
    private Cursor videoCursor;
    private Cursor reviewCursor;
    private boolean mDataValid;
    private int mRowIdColumn =0;
    private DataSetObserver movieDataSetObserver;
    private DataSetObserver videoDataSetObserver;
    private DataSetObserver reviewDataSetObserver;

    private Context mContext;
    private Activity mActivity;
    private static boolean IS_YOUTUBE_INSTALLED;
    public MovieDetailAdapter(Activity activity){
        this.mContext = activity;
        this.mActivity = activity;
        movieItems = new ArrayList<>();
        videoCursor = null;
        movieCursor = null;
        reviewCursor = null;
        IS_YOUTUBE_INSTALLED = (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(mContext)== YouTubeInitializationResult.SUCCESS);
    }
//
//    public void setMovieCursor(Cursor movieCursor){
//        this.movieCursor = movieCursor;
//        setMovieDataSetObserver();
//        if(this.movieCursor != null && videoCursor != null) mDataValid =true;
//        notifyDataSetChanged();
//    }
//    public void setVideoCursor(Cursor videoCursor){
//        this.videoCursor = videoCursor;
//        setVideoDataSetObserver();
//        if(this.movieCursor != null && videoCursor != null) mDataValid =true;
//        notifyDataSetChanged();
//    }
//    public void setReviewCursor(Cursor reviewCursor){
//        this.reviewCursor = reviewCursor;
//        setReviewDataSetObserver();
//        notifyDataSetChanged();
//    }
    public void setMovieDataSetObserver(){
        movieDataSetObserver = new NotifyingDataSetObserver();
        if (movieCursor != null) {
            movieCursor.registerDataSetObserver(movieDataSetObserver);
        }
    }

    public void setVideoDataSetObserver(){
        videoDataSetObserver = new NotifyingDataSetObserver();
        if (videoCursor != null) {
            videoCursor.registerDataSetObserver(videoDataSetObserver);
        }
    }

    public void setReviewDataSetObserver(){
        reviewDataSetObserver = new NotifyingDataSetObserver();
        if (reviewCursor != null) {
            reviewCursor.registerDataSetObserver(reviewDataSetObserver);
        }
    }

    public int getMovieItemCusorCount(){
        if(movieCursor == null) return 0;
        return movieCursor.getCount() + ( videoCursor==null? 0:videoCursor.getCount()) + (reviewCursor==null?0:reviewCursor.getCount());
    }

    @Override
    public int getItemCount(){
        if(MovieUtils.FAVORITE_FLAG){
            return getMovieItemCusorCount();

        }else {
            return movieItems.size();
        }
    }

    public int getActualPositionInCursor(int pos){
        if(pos==0) return 0;
        else if(pos<= videoCursor.getCount()) return pos -1;
        else if(pos<= videoCursor.getCount()+ reviewCursor.getCount()) return pos - (videoCursor.getCount() +1);
        else throw new ArrayIndexOutOfBoundsException(pos);
    }
    public void changeCursor(Cursor cursor){
        Cursor old;
        //switch (getExtra(cursor)){
        switch (cursor.getColumnCount()){
            case MovieUtils.MOVIE_DETAIL:
                old = swapMovieCursor(cursor);
                break;
            case MovieUtils.VIDEO:
                old = swapVideoCursor(cursor);
                break;
            case MovieUtils.REVIEW:
                old = swapReviewCursor(cursor);
                break;
            default:
                old =null;
                break;
        }
        if(old != null){
            old.close();
        }
    }

    public Cursor swapMovieCursor(Cursor newCursor){
        if (newCursor == movieCursor) {
            return null;
        }
        final Cursor oldCursor = movieCursor;
        if (oldCursor != null && movieDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(movieDataSetObserver);
        }
        movieCursor = newCursor;
        if (movieCursor != null) {
            if (movieDataSetObserver != null) {
                movieCursor.registerDataSetObserver(movieDataSetObserver);
            }else {
                setMovieDataSetObserver();
            }
           // mRowIdColumn = newCursor.getColumnIndexOrThrow(MovieUtils.MOVIE_COLUMN[MovieUtils.COL_MOVIE_ID]);
            mRowIdColumn =0;
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

    public Cursor swapVideoCursor(Cursor newCursor){
        if (newCursor == videoCursor) {
            return null;
        }
        final Cursor oldCursor = videoCursor;
        if (oldCursor != null && videoDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(videoDataSetObserver);
        }
        videoCursor = newCursor;
        if (videoCursor != null) {
            if (videoDataSetObserver != null) {
                videoCursor.registerDataSetObserver(videoDataSetObserver);
            }else {
                setVideoDataSetObserver();
            }
            //mRowIdColumn = newCursor.getColumnIndexOrThrow(MovieUtils.VIDEO_COLUMN[MovieUtils.COL_VIDEO_ID]);
            mRowIdColumn =0;
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

    public Cursor swapReviewCursor(Cursor newCursor){
        if (newCursor == reviewCursor) {
            return null;
        }
        final Cursor oldCursor = reviewCursor;
        if (oldCursor != null && reviewDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(reviewDataSetObserver);
        }
        reviewCursor = newCursor;
        if (reviewCursor != null) {
            if (reviewDataSetObserver != null) {
                reviewCursor.registerDataSetObserver(reviewDataSetObserver);
            }else {
                setReviewDataSetObserver();
            }
            //mRowIdColumn = newCursor.getColumnIndexOrThrow(MovieUtils.REVIEW_COLUMN[MovieUtils.COL_REVIEW_ID]);
            mRowIdColumn = 0;
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

    public void appendObject(Object object,int pos){
        movieItems.add(pos,object);
        Log.i(LOG_TAG,"Appending Object a pos:" + pos);
        notifyItemRangeInserted(pos,1);

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


    public void appendObjectList(ArrayList<Object> list,int pos){
        movieItems.addAll(pos,list);
        Log.i(LOG_TAG,"Appending all "+list.size() + " Objects  at postion ;"+pos+" to adapter.\n Now Adapter Contains "+movieItems.size()+" object");
        notifyItemRangeInserted(pos,list.size());
    }

    /*
    clear data in MovieAdapter
     */
    public void clear(){
        movieItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        if(MovieUtils.FAVORITE_FLAG)super.setHasStableIds(true);
        else super.setHasStableIds(false);
    }

    //Applicable only on API level 23
//    public void setExtra(Cursor cursor,int type){
//        Bundle bundle = new Bundle();
//        bundle.putInt("ITEM_TYPE",type);
//        cursor.setExtras(bundle);
//        //cursor.respond(bundle);
//       // ((AbstractCursor) cursor).setExtras(bundle);
//    }

//    public int getExtra(Cursor cursor){
//        Bundle bundle = cursor.getExtras();
//        return bundle.getInt("ITEM_TYPE");
//    }

    public Cursor getCursorAtPosition(int pos){
        if(pos == 0){
            //setExtra(movieCursor,MovieUtils.MOVIE_DETAIL);
            return movieCursor;
        }else if(pos <= videoCursor.getCount()){
            videoCursor.moveToPosition(pos - 1);
            //setExtra(videoCursor,MovieUtils.VIDEO);
            return videoCursor;
        }else if(pos <= videoCursor.getCount() + reviewCursor.getCount()){
            reviewCursor.moveToPosition(pos - (videoCursor.getCount() +1));
            //setExtra(reviewCursor,MovieUtils.REVIEW);
            return reviewCursor;
        }else {
            Log.e(LOG_TAG,"No cursor at pos " + pos);
            throw new ArrayIndexOutOfBoundsException(pos);
        }
    }

    @Override
    public long getItemId(int position) {
        if(!MovieUtils.FAVORITE_FLAG) return -1;
        Cursor cursor = getCursorAtPosition(position);
        if (mDataValid && getItemCount() != 0 && cursor != null) {
            //switch (getExtra(cursor)){
            switch (cursor.getColumnCount()){
                case MovieUtils.MOVIE_DETAIL:
                    return cursor.getLong(MovieUtils.COL_MOVIE_ID);
                case MovieUtils.VIDEO:
                    return cursor.getLong(MovieUtils.COL_VIDEO_ID);
                case MovieUtils.REVIEW:
                    return cursor.getLong(MovieUtils.COL_REVIEW_ID);
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position){
        if(MovieUtils.FAVORITE_FLAG){
           // return getExtra(getCursorAtPosition(position));
            return getCursorAtPosition(position).getColumnCount();
        }else {
            Object object = movieItems.get(position);
            if(object instanceof Movie){
                return MovieUtils.MOVIE_DETAIL;
            }else if(object instanceof Video){
                return MovieUtils.VIDEO;
            }else if(object instanceof Review){
                return MovieUtils.REVIEW;
            }else if (object instanceof String){
                return MovieUtils.HEADER;
            }else if (object instanceof SectionDataModel){
                Log.i(LOG_TAG,"Recycler view Position is "+ position);
                return MovieUtils.RECYCLER_VIEW;
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case MovieUtils.MOVIE_DETAIL:{
                View view = inflater.inflate(R.layout.movie_item,parent,false);
                viewHolder = new MovieDetailViewHolder(view);
                break;
            }
            case MovieUtils.VIDEO:{
                View view = inflater.inflate(R.layout.video_item,parent,false);
                viewHolder = new VideoViewHolder(view);
                break;
            }
            case MovieUtils.REVIEW:{
                View view = inflater.inflate(R.layout.review_item,parent,false);
                viewHolder = new ReviewViewHolder(view);
                break;
            }
            case MovieUtils.RECYCLER_VIEW:{
                View view = inflater.inflate(R.layout.inner_recycler,parent,false);
                viewHolder = new InnerRecyclerViewHolder(view);
                break;
            }

            case MovieUtils.HEADER:{
                View view = inflater.inflate(R.layout.header_item,parent,false);
                viewHolder = new HeaderViewHolder(view);
                break;
            }

            default:{
//                View view = inflater.inflate(R.layout.header_item,parent,false);
//                viewHolder = new HeaderViewHolder(view);
                viewHolder = null;
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case MovieUtils.MOVIE_DETAIL:
                MovieDetailViewHolder movieDetailViewHolder = (MovieDetailViewHolder) holder;
                configureMovieDetailViewHolder(movieDetailViewHolder,position);
                break;
            case MovieUtils.VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                configureVideoViewHolder(videoViewHolder,position);
                break;
            case MovieUtils.REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                configureReviewViewHolder(reviewViewHolder,position);
                break;
            case MovieUtils.HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                configureHeaderViewHolder(headerViewHolder,position);
                break;
            case MovieUtils.RECYCLER_VIEW:
                InnerRecyclerViewHolder innerRecyclerViewHolder = (InnerRecyclerViewHolder) holder;
                configureInnerRecyclerViewHolder(innerRecyclerViewHolder,position);
                break;

        }

    }

    /*
   Create all type of View Holders
    */
    public static class MovieDetailViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movie_item_backdrop_poster)
        ImageView backDropPoster;
        @Bind(R.id.movie_item_poster)
        ImageView posterImage;
        @Bind(R.id.movie_item_title)
        TextView title;
        @Bind(R.id.movie_item_release_date)
        TextView releaseDate;
        @Bind(R.id.movie_item_average_rating)
        TextView rating;
        @Bind(R.id.movie_item_vote_count)
        TextView voteCount;
        @Bind(R.id.movie_item_plot_summary)
        TextView plotSummary;


        public MovieDetailViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        //private ItemClickListener clickListener;
        @Bind(R.id.video_item_poster)
        ImageView poster;
        @Bind(R.id.video_item_name)
        TextView name;
        @Bind(R.id.video_item_lang)
        TextView lang;
        @Bind(R.id.video_item_type)
        TextView type;
        @Bind(R.id.video_item_share_button)
        ImageView videoShareAction;
        Activity activity;
        String videoId;
        String movieName;
        public VideoViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            poster.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(IS_YOUTUBE_INSTALLED){
                        showVideoInLightBox();
                    }else {
                        Log.i("VideoHolder","Youtube is not installed in the phone");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                        v.getContext().startActivity(Intent.createChooser(intent, "Open Video using"));
                    }
                }
            });
            poster.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    if(IS_YOUTUBE_INSTALLED){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                        v.getContext().startActivity(Intent.createChooser(intent, "Open Video using"));
                    }else{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                        v.getContext().startActivity(Intent.createChooser(intent, "Open Video using"));
                    }

                    return true;
                }
            });
            videoShareAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,"Hi, Check this new "+type.getText().toString()+" of "+ movieName +" \n "+ Uri.parse("http://www.youtube.com/watch?v="+videoId));
                    intent.setType("text/plain");
                    v.getContext().startActivity(Intent.createChooser(intent,"Share Video using"));
                }
            });
        }
        public void setActivity(Activity activity){
            this.activity = activity;
        }
        public void showVideoInLightBox(){
            activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity,
                    BuildConfig.My_GOOGLE_ANDROID_API, videoId, 0, true, true));
        }
        public void setVideoId(String videoId){
            this.videoId=videoId;
        }
        public void setMovieName(String movieName){
            this.movieName = movieName;
        }
//        public void setClickListener(ItemClickListener itemClickListener){
//            this.clickListener = itemClickListener;
//        }

        @Override
        public void onClick(View view){

            //this.clickListener.onClick(view,getPosition(),false);
        }

        @Override
        public boolean onLongClick(View view){
            //clickListener.onClick(view,getPosition(),true);
            return true;
        }

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.review_item_title)
        TextView name;
        @Bind(R.id.review_item_content)
        TextView content;

        public ReviewViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
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

    public static class InnerRecyclerViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.section_title)
        TextView sectionTitle;
        @Bind(R.id.inner_recycler_view)
        RecyclerView innerRecyclerView;

        HorizontalRecyclerAdapter innerRecyclerAdapter;
        public InnerRecyclerViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }

    }

    public void configureMovieDetailViewHolder(MovieDetailViewHolder holder,int pos){

        Movie movie;
        if (MovieUtils.FAVORITE_FLAG){
            movie = MovieUtils.getMovieFromCursor(getCursorAtPosition(pos),getActualPositionInCursor(pos));
        }else movie= (Movie) movieItems.get(pos);

        /* Possible Image size are "w92", "w154", "w185", "w342", "w500", "w780", or "original" */
        //final String SIZE="w185/";
        holder.posterImage.setAdjustViewBounds(true);
        if(MovieUtils.FAVORITE_FLAG){
            Picasso.with(mContext).load(new File( mContext.getFilesDir().getPath() + "/moviePoster/"+movie.getPosterPath())).fit().placeholder(R.drawable.placeholder).into(holder.posterImage);
            Picasso.with(mContext).load(new File( mContext.getFilesDir().getPath() + "/moviePoster/"+movie.getBackdropPath())).placeholder(R.drawable.loading_placeholder).into(holder.backDropPoster);

        }else {
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + "w185/" + movie.getPosterPath()).placeholder(R.drawable.placeholder).fit().into(holder.posterImage);
            Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + "w500/"+ movie.getBackdropPath()).placeholder(R.drawable.loading_placeholder).fit().into(holder.backDropPoster);
        }

        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(MovieUtils.formateDate(movie.getReleaseDate(),"yyyy-MM-dd","MMM, yyyy"));
        holder.rating.setText(String.format("%.1f",movie.getVoteAverage()));
        holder.voteCount.setText(Long.toString(movie.getVoteCount()));
        holder.plotSummary.setText(movie.getOverview());

    }

    public void configureVideoViewHolder(VideoViewHolder holder,int pos){
        Video video;
        if(MovieUtils.FAVORITE_FLAG){
            video = MovieUtils.getVideoFromCursor(getCursorAtPosition(pos),getActualPositionInCursor(pos));
        }else {
            video = (Video) movieItems.get(pos);
        }
        final String QUALITY = "/hqdefault.jpg";
        if(MovieUtils.FAVORITE_FLAG){
            Picasso.with(mContext).load(new File( mContext.getFilesDir().getPath() + "/videoPoster/"+video.getKey()+".jpg")).placeholder(R.drawable.grey_placeholder)
                    .into(holder.poster);
        }else {
            Picasso.with(mContext).load(MovieUtils.BASE_URL_VIDEO_THUMBNAIL + video.getKey() + QUALITY).placeholder(R.drawable.grey_placeholder)
                    .into(holder.poster);
        }
        holder.setActivity(mActivity);
        holder.name.setText(video.getName());
        holder.type.setText(video.getType());
        holder.lang.setText(video.getIso6391());
        holder.setVideoId(video.getKey());
        String movieName;
        if(MovieUtils.FAVORITE_FLAG){
            movieName = movieCursor.getString(MovieUtils.COL_MOVIE_TITLE);
        }else {
            Movie movie = (Movie) movieItems.get(0);
            movieName = movie.getTitle();
        }
        holder.setMovieName(movieName);
    }

    public void configureReviewViewHolder(ReviewViewHolder holder,int pos){
        Review review;
        if(MovieUtils.FAVORITE_FLAG){
            review = MovieUtils.getReviewFromCursor(getCursorAtPosition(pos),getActualPositionInCursor(pos));
        }else {
            review = (Review) movieItems.get(pos);
        }
        holder.name.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    public void configureHeaderViewHolder(HeaderViewHolder holder,int pos){
        String headerTitle = (String) movieItems.get(pos);
        holder.title.setText(headerTitle);
    }

    public void configureInnerRecyclerViewHolder(InnerRecyclerViewHolder holder,int pos){
        SectionDataModel sectionDataModel = (SectionDataModel) movieItems.get(pos);
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.sectionTitle.setText(sectionDataModel.getSectionTitle());
        HorizontalRecyclerAdapter adapter = new HorizontalRecyclerAdapter(mActivity,sectionDataModel.getAllItemsInSection());
        holder.innerRecyclerView.setLayoutManager(layoutManager);
        holder.innerRecyclerView.setAdapter(adapter);
    }

}
