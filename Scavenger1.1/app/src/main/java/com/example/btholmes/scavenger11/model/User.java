package com.example.btholmes.scavenger11.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by btholmes on 11/4/17.
 */

/**
 * This is user class used to retrieve data from Firebase. All info is parsed and stored in Realm as user-specific
 */
public class User {


    private String userToken = null;
    private List<Game> games = new ArrayList<>();
    private List<PushNotification> notifications = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private String displayName = null;
    private String photoUrl = "https://scontent.xx.fbcdn.net/v/t1.0-1/s100x100/1378871_10151890982237420_1347727989_n.jpg?oh=f8522e0ea7aee1411b67ee45988d5456&oe=5A6FA4C9";
    private String hasGames = "No";

    @Required
    private String email = null;

    @Required
    @PrimaryKey
    private String uid = null;


    public User() {

    }

    public static User create(String uid, String email){
        User user = new User();
        user.uid = uid;
        user.email = email;
        return user;
    }

    public User(String uid, String email){
        this.uid = uid;
        this.email = email;
        this.displayName = email;

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

    public void setEmail(String email){
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

    public List<Game> getGames() {
        if(games.size() >=1){
            hasGames = "yes";
        }
        return games;
    }

    public void setGames(List<Game> games) {
        if(games!= null && games.size() >= 1){
            this.games = games;
            hasGames = "no";
        }

    }
}