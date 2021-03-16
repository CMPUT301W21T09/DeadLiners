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
    public User owner;
    //public Array<User> participants;
    

    public Experiment(String name, String description, String category, String region, String minimum_trails){
        this.expName = name;
        this.description = description;
        this.category = category;
        this.region = region;
        this.minimum_trails = minimum_trails;
        this.published = "True";
    }

    public String isPublished() {
        return published;
    }

    public void setPublishedToTrue() {
        this.published = "True";
    }

    public void setPublishedToFalse(){
        this.published = "False";
    }
    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Location> getIgnores() {
        return ignores;
    }

    public void setIgnores(ArrayList<Location> ignores) {
        this.ignores = ignores;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
