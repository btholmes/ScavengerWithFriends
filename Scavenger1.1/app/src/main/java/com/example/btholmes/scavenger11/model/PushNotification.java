package com.example.btholmes.scavenger11.model;

/**
 * Created by btholmes on 4/16/17.
 */

public class PushNotification {

    private String title;
    private String msg;
    private String senderUID;

    public PushNotification(){

    }

    public PushNotification(String none){
        this.title = none;
        this.msg = none;
        this.senderUID = none;
    }

    public PushNotification(String title, String msg, String senderUID){
        this.title = title;
        this.msg = msg;
        this.senderUID = senderUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }
}
