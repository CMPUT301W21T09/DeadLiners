package com.example.myapplication;

import java.io.Serializable;

public class Experiment implements Serializable {
    private String expName;
    private String description;
    private String category;
    private String region;
    private String minimum_trails;
    public String status;
    public String owner;
    public String ownerName;
    public String geoState;

    public Experiment(String name, String description, String category, String region, String minimum_trails,String uid, String geoState) {
        this.expName = name;
        this.description = description;
        this.category = category;
        this.region = region;
        this.minimum_trails = minimum_trails;
        this.owner = uid;
        if (geoState.equals("required")) {
            this.geoState = "1";
        } else {
            this.geoState = "0";
        }
    }

    public Experiment(String name, String description,String category,String region,String minimumTrails, String uid, String geostate, String status) {
        this.expName = name;
        this.description = description;
        this.category = category;
        this.region = region;
        this.minimum_trails = minimumTrails;
        this.status = status;
        this.geoState = geostate;
        this.owner = uid;
    }

    public Experiment(String name, String description, String category, String region, String minimum_trails,String uid){
        this.expName = name;
        this.description = description;
        this.category = category;
        this.region = region;
        this.minimum_trails = minimum_trails;
        this.status = "open";
        this.owner = uid;
    }



    public void setStatus(String status) {
        this.status = status;
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
        return status;
    }

    public String getGeoState() {
        return geoState;
    }

    public void setGeoState(String geoState) {
        this.geoState = geoState;
    }

    public void setPublishedToFalse() {
        this.status = "end";
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
