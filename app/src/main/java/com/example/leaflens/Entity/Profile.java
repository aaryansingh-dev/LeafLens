package com.example.leaflens.Entity;

import java.io.Serializable;

public class Profile implements Serializable{
    String name;
    String DOB;
    String email;
    String profileImageURL;
    String phoneNumber;

    private String deviceID;

    public Profile(){}

    public Profile(String deviceID, String name, String DOB, String email, String phoneNumber, String imageURL)
    {
        this.deviceID = deviceID;
        this.name = name;
        this.DOB = DOB;
        this.email = email;
        this.profileImageURL = imageURL;
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setDOB(String dob)
    {
        this.DOB = dob;
    }

    public String getDOB()
    {
        return this.DOB;
    }

    public void setProfileImageURL(String URL)
    {
        this.profileImageURL = URL;
    }

    public String getProfileImageURL()
    {
        return this.profileImageURL;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addDeviceId(String id)
    {
        this.deviceID = id;
    }

    public String getDeviceID()
    {
        return this.deviceID;
    }
}

