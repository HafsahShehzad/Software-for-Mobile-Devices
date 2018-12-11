package com.example.apple.tabssample.Classes;

import java.io.Serializable;
import java.util.Vector;

public class Post implements Serializable {

    String id,date;
    String location;
    String title;
    String description;
    String publisher;
    Vector<String> pictures;
    Vector<Likes>  likes;
    Vector<PictureComment> pictureComments;
    Vector<Share>  shares;

    public Post(){

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Post(String id, String date, String location, String title, String description, String publisher, Vector<String> pictures, Vector<Likes> likes, Vector<PictureComment> pictureComments, Vector<Share> shares) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.title = title;
        this.description = description;
        this.publisher=publisher;
        this.pictures = pictures;
        this.likes = likes;
        this.pictureComments = pictureComments;
        this.shares = shares;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vector<String> getPicture() {
        return pictures;
    }

    public void setPicture(Vector<String> pictures) {
        this.pictures = pictures;
    }

    public Vector<Likes> getLikes() {
        return likes;
    }

    public void setLikes(Vector<Likes> likes) {
        this.likes = likes;
    }

    public Vector<PictureComment> getPictureComments() {
        return pictureComments;
    }

    public void setPictureComments(Vector<PictureComment> pictureComments) {
        this.pictureComments = pictureComments;
    }

    public Vector<Share> getShares() {
        return shares;
    }

    public void setShares(Vector<Share> shares) {
        this.shares = shares;
    }



}
