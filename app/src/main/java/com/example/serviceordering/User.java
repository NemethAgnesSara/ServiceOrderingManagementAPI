package com.example.serviceordering;

public class User {

    private String name;
    private String email;
    private String phoneNumber;
    private String phneType;
    private String interest;

    public User(String name, String email, String phoneNumber, String phneType, String interest) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.phneType = phneType;
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhneType() {
        return phneType;
    }

    public void setPhneType(String phneType) {
        this.phneType = phneType;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
