package com.example.leaflens.entity;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Date;

public class SearchResult {

    private String name;
    private Date dateSearched;
    private Bitmap imageClicked;
    private String resultShown;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDateSearched(Date date)
    {
        this.dateSearched = date;
    }

    public Date getDateSearched()
    {
        return this.dateSearched;
    }

    public void setImageClicked(Bitmap img)
    {
        this.imageClicked = img;
    }

    public Bitmap getImageClicked(){
        return this.imageClicked;
    }

    public void setResultShown(String diagnosis){
        this.resultShown = diagnosis;
    }

    public String getResultShown(){
        return this.resultShown;
    }
}
