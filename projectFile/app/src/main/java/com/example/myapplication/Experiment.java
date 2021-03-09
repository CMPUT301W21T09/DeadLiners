package com.example.myapplication;

import android.location.Location;

import java.util.ArrayList;

public class Experiment {
    private String expName;
    private String description;
    private String category;
    //private Lacation region;
    private Integer minimum_trails;
    public boolean published;
    public ArrayList<Location> location;
    public ArrayList<Location> ignores;
    //public barcode;
    public User owner;
    public Array<User> participants;
    

    public String getExpName(){return this.expName;}
    public void setExpName(String name){this.expName = name;}
    public String getDescription(){return this.description;}
    public void setDescription(String des){this.description = des;}
    public String getCategory(){return this.category;}
    public void setCategory(String cat){this.category = cat;}

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
