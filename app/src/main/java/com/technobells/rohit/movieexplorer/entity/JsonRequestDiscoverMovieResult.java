package com.technobells.rohit.movieexplorer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 30/12/15.
 */
public class JsonRequestDiscoverMovieResult implements Parcelable {

    @SerializedName("page")
    @Expose
    private long page;
    @SerializedName("results")
    @Expose
    private List<Movie> results = new ArrayList<>();
    @SerializedName("total_results")
    @Expose
    private long totalResults;
    @SerializedName("total_pages")
    @Expose
    private long totalPages;

    /**
     * No args constructor for use in serialization
     *
     */
    public JsonRequestDiscoverMovieResult() {
    }

    /**
     *
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public JsonRequestDiscoverMovieResult(long page, List<Movie> results, long totalResults, long totalPages) {
        this.page = page;
        this.results = results;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The page
     */
    public long getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(long page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<Movie> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Movie> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public long getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag){
        dest.writeLong(page);
        dest.writeList(results);
        dest.writeLong(totalResults);
        dest.writeLong(totalPages);

    }

    @Override
    public int describeContents(){
        return 0;
    }

    private JsonRequestDiscoverMovieResult(Parcel in){
        page = in.readInt();
        ArrayList<Movie> l = new ArrayList<>();
        in.readList(l,null);
        results =l;
        totalResults=in.readInt();
        totalPages=in.readInt();

    }


    public static final Parcelable.Creator<JsonRequestDiscoverMovieResult> CREATOR = new Parcelable.Creator<JsonRequestDiscoverMovieResult>(){
        @Override
        public JsonRequestDiscoverMovieResult createFromParcel(Parcel in){
            return new JsonRequestDiscoverMovieResult(in);
        }

        @Override
        public JsonRequestDiscoverMovieResult[] newArray(int size){
            return new JsonRequestDiscoverMovieResult[size];
        }
    };


}
