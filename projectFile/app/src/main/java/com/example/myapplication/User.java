package com.example.myapplication;

public class User {
    private String uid;
    private String username;
    private String email;
    private String phoneNumber;


    User(String uid, String username, String email, String phoneNumber){
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setPhone(String number){this.phoneNumber=number;}
    public String getPhoneNumber(){return this.phoneNumber;}

    public  void setUid(String uid){ this.uid = uid; }


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

}
