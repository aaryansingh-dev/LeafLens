package com.example.leaflens.entity;

public class Profile {
    String name;
    String DOB;
    String email;
    String profileImageURL;
    String phoneNumber;

    public Profile(){}

    public Profile(String name, String DOB, String email, String phoneNumber)
    {
        this.name = name;
        this.DOB = DOB;
        this.email = email;
        this.profileImageURL = null;
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
}

