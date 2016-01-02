package com.technobells.rohit.movieexplorer.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 30/12/15.
 */
public class JsonRequestMovieVideoResult {


    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = new ArrayList<Video>();

    /**
     *
     * @return
     * The id
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The videos
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     *
     * @param videos
     * The videos
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }


}
