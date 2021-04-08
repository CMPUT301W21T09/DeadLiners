package com.example.myapplication;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private String uid = "test uid";
    private String username = "test name";
    private String email = "test email";
    private String phoneNumber = "test number";

    private User mockuser(){return new User(uid,username,email,phoneNumber);}


    @Test
    public void TestGetUserInfo(){
        User mockuser = mockuser();
        assertEquals("test uid", mockuser.getUid());
        assertEquals("test name", mockuser.getUsername());
        assertEquals("test email", mockuser.getEmail());
        assertEquals("test number", mockuser.getPhoneNumber());
        mockuser.setEmail("1");
        assertEquals("1",mockuser.getEmail());
        mockuser.setPhone("123");
        assertEquals("123",mockuser.getPhoneNumber());

    }
}
