package com.example.btholmes.scavenger11.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by btholmes on 11/4/17.
 */


/**
 * This is the User class for in app storage
 */
public class RealmUser extends RealmObject{


    private String userToken = null;
    private String displayName = null;
    private String photoUrl = "https://scontent.xx.fbcdn.net/v/t1.0-1/s100x100/1378871_10151890982237420_1347727989_n.jpg?oh=f8522e0ea7aee1411b67ee45988d5456&oe=5A6FA4C9";

    @Required
    private String email = null;

    @Required
    @PrimaryKey
    private String uid = null;


    public RealmUser() {

    }

    public static RealmUser create(String uid, String email){
        RealmUser user = new RealmUser();
        user.uid = uid;
        user.email = email;
        return user;
    }

    public RealmUser(String uid, String email){
        this.uid = uid;
        this.email = email;
        this.displayName = email;
    }

    public RealmUser(String uid, String email, String displayName) {
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