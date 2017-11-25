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


    private String displayName;
    @Required
    private String email;
    private String photoUrl = "https://scontent.xx.fbcdn.net/v/t1.0-1/s100x100/1378871_10151890982237420_1347727989_n.jpg?oh=f8522e0ea7aee1411b67ee45988d5456&oe=5A6FA4C9";

    @Required
    @PrimaryKey
    private String uid;

    private int wins = 0;
    private int losses = 0;

    //    private List<Game> games = new ArrayList<>();
    private String userToken;
    //    private List<PushNotification> notifications = new ArrayList<>();
//    private List<Message> messages = new ArrayList<>();
    private String hasGames = "No";


    public RealmUser() {

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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}