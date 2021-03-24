package com.example.myapplication;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class Experiment implements Serializable {
    private String expName;
    private String description;
    private String category;
    private String region;
    private String minimum_trails;
    public String published;
    public ArrayList<Location> locations;
    public ArrayList<Location> ignores;
    //public barcode;
    public String owner;
    public String ownerName;
    //public Array<User> participants;
    public int geoState = 0;

    public Experiment(String name, String description, String category, String region, String minimum_trails,String uid){
        this.expName = name;
        this.description = description;
        this.category = category;
        this.region = region;
        this.minimum_trails = minimum_trails;
        this.published = "published";
        this.owner = uid;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwnerName(String userName){
        this.ownerName = userName;
    }
    public String getPublished() {
        return published;
    }

    public void setPublishedToTrue() {
        this.published = "published";
    }

    public int getGeoState() {
        return geoState;
    }

    public void setGeoState(int geoState) {
        this.geoState = geoState;
    }

    public void setPublishedToFalse() {
        this.published = "unpublished";
    }
    public Experiment(String name) {
        this.expName = name;
    }
    public String getExpName(){return this.expName;}
    public void setExpName(String name){this.expName = name;}
    public String getDescription(){return this.description;}
    public void setDescription(String des){this.description = des;}
    public String getCategory(){return this.category;}
    public void setCategory(String cat){this.category = cat;}

    public String getRegion() {
        return region;
    }

    public String getMinimum_trails() {
        return minimum_trails;
    }

    public void setMinimum_trails(String minimum_trails) {
        this.minimum_trails = minimum_trails;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void generateQR(){}

    public void setBarcode(){}

    public int calculateSTAT(){
        return 0;
    }

    public void generateHistograms(){}

    public void generatePlots(){}

    public void addQuestion(){}

    public void replyQuestion(){}

    public void upload(){}

    public void addLocation(){}

    public void addIgnoreLocation(){}
}
