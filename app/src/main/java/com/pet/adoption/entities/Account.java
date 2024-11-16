package com.pet.adoption.entities;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pet.adoption.services.firebase.FirebaseAuthHelper;

public class Account {

    private String userUID;
    private String email;
    private String username;
    private String password;

    public Account(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Account(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Account() {
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

    public Task<AuthResult> signUp(){
        return FirebaseAuthHelper.signUp(email, password);
    }

    public Task<AuthResult> login(){
        return FirebaseAuthHelper.login(email, password);
    }
}
