package com.example.myapplication;

import java.sql.Date;
import java.util.ArrayList;

public class Trial {
    private String expName;
    private String experimenter;
    private Date time;
    private String value;

    public Trial(String experimenter, String expName, Date time, String value) {
        this.experimenter = experimenter;
        this.expName = expName;
        this.value = value;
        this.time = time;
    }


    public Date getTime() {
        return time;
    }

    public String getExpName() {
        return expName;
    }

    public String getValue() {
        return value;
    }

    public String getExperimenter() {
        return experimenter;
    }
}
