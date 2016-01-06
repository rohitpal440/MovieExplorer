package com.technobells.rohit.movieexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 30/12/15.
 */
public class JsonRequestMovieReviewResult {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("page")
    @Expose
    private long page;
    @SerializedName("results")
    @Expose
    private List<Review> reviews = new ArrayList<Review>();
    @SerializedName("total_pages")
    @Expose
    private long totalPages;
    @SerializedName("total_results")
    @Expose
    private long totalResults;

    /**
     * @return The id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The page
     */
    public long getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(long page) {
        this.page = page;
    }

    /**
     * @return The reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews The reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return The totalPages
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return The totalResults
     */
    public long getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

}
