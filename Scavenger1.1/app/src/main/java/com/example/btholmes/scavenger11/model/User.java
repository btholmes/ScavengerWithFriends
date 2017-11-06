package com.example.btholmes.scavenger11.model;

import java.io.Serializable;

/**
 * Created by btholmes on 11/4/17.
 */

public class User implements Serializable{

    private String uid;
    private String email;
    private String displayName;
    private String photoUrl;

    public User() {

    }

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public String getUid(){
        return uid;
    }

    public String getEmail(){
        return email;
    }

    public String getDisplayName(){
        return displayName;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public   void setEmail(String email){
        this.email = email;
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}