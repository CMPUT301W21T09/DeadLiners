package com.example.myapplication;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Trial {
    private String expName;
    private String experimenter;
    private String time;
    private Boolean ignore;
    private String value;

    public Trial(String experimenter, String expName, String time, String value, Boolean ignore) {
        this.experimenter = experimenter;
        this.expName = expName;
        this.value = value;
        this.ignore = ignore;
        this.time = time;
    }


    public String getTime() {
        return time;
    }

    public String getExpName() {
        return expName;
    }

    public String getValue() {
        return value;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    public String getExperimenter() {
        return experimenter;
    }
}
