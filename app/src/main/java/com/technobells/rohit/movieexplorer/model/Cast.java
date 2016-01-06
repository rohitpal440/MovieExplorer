package com.technobells.rohit.movieexplorer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rohit on 30/12/15.
 */
public class Cast implements Parcelable{

    @SerializedName("cast_id")
    @Expose
    private long castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private long order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;



    /**
     *
     * @return
     * The castId
     */
    public long getCastId() {
        return castId;
    }

    /**
     *
     * @param castId
     * The cast_id
     */
    public void setCastId(long castId) {
        this.castId = castId;
    }

    /**
     *
     * @return
     * The character
     */
    public String getCharacter() {
        return character;
    }

    /**
     *
     * @param character
     * The character
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     *
     * @return
     * The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     *
     * @param creditId
     * The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

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
     * The order
     */
    public long getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(long order) {
        this.order = order;
    }

    /**
     *
     * @return
     * The profilePath
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     *
     * @param profilePath
     * The profile_path
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag){
        dest.writeLong(castId);
        dest.writeString(character);
        dest.writeString(creditId);
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(order);
        dest.writeString(profilePath);
    }

    public Cast(){}
    private Cast(Parcel in){
        castId = in.readLong();
        character = in.readString();
        creditId = in.readString();
        id = in.readLong();
        name = in.readString();
        order = in.readLong();
        profilePath = in.readString();
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>(){
        @Override
        public Cast createFromParcel(Parcel in){
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size){
            return new Cast[size];
        }
    };



}
