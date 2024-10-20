package com.pet.adoption.entities;


public class Account {
    private String email;
    private String username;
    private String userUID;

    public Account(String email, String username, String userUID) {
        this.email = email;
        this.username = username;
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
