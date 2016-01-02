package com.technobells.rohit.movieexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technobells.rohit.movieexplorer.R;
import com.technobells.rohit.movieexplorer.entity.SectionDataModel;
import com.technobells.rohit.movieexplorer.entity.Movie;
import com.technobells.rohit.movieexplorer.entity.Review;
import com.technobells.rohit.movieexplorer.entity.Video;
import com.technobells.rohit.movieexplorer.utilities.MovieUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rohit on 31/12/15.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static final int MOVIE_DETAIL = 0 ;
    private static final int VIDEO = 1;
    private static final int REVIEW = 3;
    private static final int HEADER = 5 ;
    private static final int RECYCLER_VIEW = 6;

    private ArrayList<Object> movieItems;
    private Context mContext;

    public MovieDetailAdapter(Context context){
        this.mContext = context;
        movieItems = new ArrayList<>();
    }

    public void appendObject(Object object){
        movieItems.add(object);
        notifyItemRangeInserted(movieItems.size(),1);
    }

    public void appendObjectList(ArrayList<Object> list){
        movieItems.addAll(list);
        Log.i(LOG_TAG,"Appending all "+list.size() + " Objects to adapter.\n Now Adapter Contains "+movieItems.size()+" object");
        notifyItemRangeInserted(movieItems.size(),list.size());
    }

//    public void addAll(ArrayList<Object> list){
//        movieItems.addAll(list);
//        Log.i(LOG_TAG,"Adding all "+list.size() + " Objects to adapter.\n Now Adapter Contains "+movieItems.size()+" Objects");
//        notifyDataSetChanged();
//    }

    /*
    clear data in MovieAdapter
     */
    public void clear(){
        movieItems.clear();
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
        @Bind(R.id.video_item_share_buttor)
        ImageView videoShareAction;

        int position;

        public VideoViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            poster.setOnClickListener(this);
            poster.setOnLongClickListener(this);
            videoShareAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("You have Just Clicked on Share button");

                    Toast.makeText(v.getContext(),
                            "You have just clicked on Share Action at pos"+ position,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void setPosition(int pos){
            position = pos;
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

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    @Override
    public int getItemViewType(int position){
        Object object = movieItems.get(position);
        if(object instanceof Movie){
            return MOVIE_DETAIL;
        }else if(object instanceof Video){
            return VIDEO;
        }else if(object instanceof Review){
            return REVIEW;
        }else if (object instanceof String){
            return HEADER;
        }else if (object instanceof SectionDataModel){
            Log.i(LOG_TAG,"Recycler view Position is "+ position);
            return RECYCLER_VIEW;
        }

        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case MOVIE_DETAIL:{
                View view = inflater.inflate(R.layout.movie_item,parent,false);
                viewHolder = new MovieDetailViewHolder(view);
                break;
            }
            case VIDEO:{
                View view = inflater.inflate(R.layout.video_item,parent,false);
                viewHolder = new VideoViewHolder(view);
                break;
            }
            case REVIEW:{
                View view = inflater.inflate(R.layout.review_item,parent,false);
                viewHolder = new ReviewViewHolder(view);
                break;
            }
            case RECYCLER_VIEW:{
                View view = inflater.inflate(R.layout.inner_recycler,parent,false);
                viewHolder = new InnerRecyclerViewHolder(view);
                break;
            }

            case HEADER:{
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
            case MOVIE_DETAIL:
                MovieDetailViewHolder movieDetailViewHolder = (MovieDetailViewHolder) holder;
                configureMovieDetailViewHolder(movieDetailViewHolder,position);
                break;
            case VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                configureVideoViewHolder(videoViewHolder,position);
                break;
            case REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                configureReviewViewHolder(reviewViewHolder,position);
                break;
            case HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                configureHeaderViewHolder(headerViewHolder,position);
                break;
            case RECYCLER_VIEW:
                InnerRecyclerViewHolder innerRecyclerViewHolder = (InnerRecyclerViewHolder) holder;
                configureInnerRecyclerViewHolder(innerRecyclerViewHolder,position);
                break;

        }

    }

    public void configureMovieDetailViewHolder(MovieDetailViewHolder holder,int pos){
        Movie movie = (Movie) movieItems.get(pos);

        /* Possible Image size are "w92", "w154", "w185", "w342", "w500", "w780", or "original" */
        //final String SIZE="w185/";
        Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + "w185/" + movie.getPosterPath()).placeholder(R.drawable.placeholder).into(holder.posterImage);
        Picasso.with(mContext).load(MovieUtils.BASE_URL_IMAGE + "w500/"+ movie.getBackdropPath()).placeholder(R.drawable.loading_placeholder).into(holder.backDropPoster);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(MovieUtils.formateDate(movie.getReleaseDate(),"yyyy-MM-dd","MMM, yyyy"));
        holder.rating.setText(String.format("%.1f",movie.getVoteAverage()));
        holder.voteCount.setText(Long.toString(movie.getVoteCount()));
        holder.plotSummary.setText(movie.getOverview());

    }

    public void configureVideoViewHolder(VideoViewHolder holder,int pos){
        Video video = (Video) movieItems.get(pos);
        String QUALITY = "/hqdefault.jpg";
        Picasso.with(mContext).load(MovieUtils.BASE_URL_VIDEO + video.getKey() + QUALITY).placeholder(R.drawable.grey_placeholder)
                .into(holder.poster);
        holder.name.setText(video.getName());
        holder.type.setText(video.getType());
        holder.lang.setText(video.getIso6391());
        holder.setPosition(pos);
//        holder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//
//                if(isLongClick){
//                    Toast.makeText(mContext,"Hello",
//                            Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(mContext,"hi",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

    }

    public void configureReviewViewHolder(ReviewViewHolder holder,int pos){
        Review review = (Review) movieItems.get(pos);
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
        HorizontalRecyclerAdapter adapter = new HorizontalRecyclerAdapter(mContext,sectionDataModel.getAllItemsInSection());
        holder.innerRecyclerView.setAdapter(adapter);
        holder.innerRecyclerView.setLayoutManager(layoutManager);

    }

}
