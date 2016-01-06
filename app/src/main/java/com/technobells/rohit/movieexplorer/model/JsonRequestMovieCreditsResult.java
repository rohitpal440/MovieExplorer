package com.technobells.rohit.movieexplorer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohit on 30/12/15.
 */
public class JsonRequestMovieCreditsResult {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = new ArrayList<Cast>();
//    @SerializedName("crew")
//    @Expose
//    private List<Crew> crew = new ArrayList<Crew>();

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
     * The cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     *
     * @param cast
     * The cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    /**
     *
     * @return
     * The crew
     */
//    public List<Crew> getCrew() {
//        return crew;
//    }

    /**
     *
     * @param crew
     * The crew
     */
//    public void setCrew(List<Crew> crew) {
//        this.crew = crew;
//    }
}
