package com.example.myapplication;

import android.location.Location;

public class User {
    private  String uid;
    private String username;
    private String email;
    private Location address;

    public User(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Location getAddress() {
        return address;
    }
    public void setAddress(Location address) {
        this.address = address;
    }
}
