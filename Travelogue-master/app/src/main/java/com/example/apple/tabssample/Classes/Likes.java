package com.example.apple.tabssample.Classes;

public class Likes {

    private String UID;
    private String like_status;

    public Likes(String UID, String like_status) {
        this.UID = UID;
        this.like_status = like_status;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }
}
