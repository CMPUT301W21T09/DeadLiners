package com.example.myapplication;

import android.location.Location;

public class User {
    private String username;
    private String email;
    private Location address;

    public User(String username) {
        this.username = username;
    }

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
