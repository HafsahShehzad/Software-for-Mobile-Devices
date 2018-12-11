package com.example.apple.tabssample.Classes;

import java.util.ArrayList;
import java.util.Vector;

public class Picture {

    private static int count=0;
    private String PID;
    private Vector<String>picture_URL;
    private String likes_count;
    private ArrayList<PictureComment> pictureComments;
    private ArrayList<Likes> likes;
    private ArrayList<Share> shares;


    public Picture(Vector<String>picture_URL){
        this.pictureComments =new ArrayList<>();
        this.likes=new ArrayList<>();
        this.shares=new ArrayList<>();
        this.picture_URL=picture_URL;
    }

    public void addComment(PictureComment pictureComment){
        pictureComments.add(pictureComment);
    }

    public void addLike(Likes like){
        likes.add(like);
    }

    public void addShare(Share share){
        shares.add(share);
    }

    public void deleteLike(Likes like){
        likes.remove(like);
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public Vector<String> getPicture_URL() {
        return picture_URL;
    }

    public void setPicture_URL(Vector<String> picture_URL) {
        this.picture_URL = picture_URL;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public ArrayList<PictureComment> getPictureComments() {
        return pictureComments;
    }

    public void setPictureComments(ArrayList<PictureComment> pictureComments) {
        this.pictureComments = pictureComments;
    }

    public ArrayList<Likes> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Likes> likes) {
        this.likes = likes;
    }
}
