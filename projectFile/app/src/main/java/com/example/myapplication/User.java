package com.example.myapplication;

import android.location.Location;

public class User {
    private String uid;
    private String username;
    private String email;
    private String phoneNumber;
    private Location address;

    public User(String uid) {
        this.uid = uid;
    }

    public void setPhone(String number){this.phoneNumber=number;}

    public  void setUid(String uid){ this.uid = uid; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(Location address) {
        this.address = address;
    }
}
