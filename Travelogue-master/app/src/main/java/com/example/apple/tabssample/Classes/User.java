package com.example.apple.tabssample.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Vector;


public class User {

    private String uid;
    private String location;
    private String email_address;
    private String full_name;
    private String username;
    private String password;
    private String user_description;
    private String profile_picture;
    private String cover_picture;
    private ArrayList<Follow>Followers;
    private ArrayList<Follow>Following;



    public User(String name, String email, String username, String password){
        this.full_name=name;
        this.email_address=email;
        this.username=username;
        this.password=password;
        this.user_description="Tell the world a little about you!";
        this.profile_picture="";
        this.cover_picture="";
        this.location="";
        this.Followers=new ArrayList<>();
        this.Following=new ArrayList<>();


    }

    public void setUID(String uid){
        this.uid=uid;
    }

    public String getUid(){
        return uid;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_description() {
        return user_description;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getCover_picture() {
        return cover_picture;
    }

    public void setCover_picture(String cover_picture) {
        this.cover_picture = cover_picture;
    }

    public ArrayList<Follow> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<Follow> followers) {
        Followers = followers;
    }

    public ArrayList<Follow> getFollowing() {
        return Following;
    }

    public void setFollowing(ArrayList<Follow> following) {
        Following = following;
    }
}
