package com.technobells.rohit.movieexplorer.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 30/12/15.
 */
public class Video implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private long size;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The iso6391
     */
    public String getIso6391() {
        return iso6391;
    }

    /**
     *
     * @param iso6391
     * The iso_639_1
     */
    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    /**
     *
     * @return
     * The key
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The site
     */
    public String getSite() {
        return site;
    }

    /**
     *
     * @param site
     * The site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     *
     * @return
     * The size
     */
    public long getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }


    @Override
    public void writeToParcel(Parcel dest, int flag){
        dest.writeString(id);
        dest.writeString(iso6391);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeLong(size);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public Video(){}
    private Video(Parcel in){
        id = in.readString();
        iso6391 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readLong();
        type = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>(){
        @Override
        public Video createFromParcel(Parcel in){
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size){
            return new Video[size];
        }
    };


}
