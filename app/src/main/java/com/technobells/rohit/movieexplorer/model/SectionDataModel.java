package com.technobells.rohit.movieexplorer.model;

import java.util.ArrayList;

/**
 * Created by rohit on 2/1/16.
 */
public class SectionDataModel {
    private final String LOG_TAG = SectionDataModel.class.getSimpleName();
    private String sectionTitle;
    private ArrayList<Object> allItemsInSection = new ArrayList<>();

    public SectionDataModel(){}
    public SectionDataModel(String sectionTitle,ArrayList<Object> allItemsInSection){
        this.sectionTitle = sectionTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public void setSectionTitle(String sectionTitle){
        this.sectionTitle= sectionTitle;
    }
    public String getSectionTitle(){
        return sectionTitle;
    }

    public void setAllItemsInSection(ArrayList<Cast> casts,ArrayList<Movie> similarMovies){
        if(casts != null) this.allItemsInSection.addAll(casts);
        if(similarMovies != null) this.allItemsInSection.addAll(similarMovies);
    }

    public ArrayList<Object> getAllItemsInSection(){
        return allItemsInSection;
    }

}
