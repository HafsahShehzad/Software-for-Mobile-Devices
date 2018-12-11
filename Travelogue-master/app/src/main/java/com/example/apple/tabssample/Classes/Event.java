package com.example.apple.tabssample.Classes;

import android.location.Location;

import java.io.Serializable;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

public class Event implements Serializable {
    String id;
    String startDate;
    String endDate;
    String title,description;
    String location;
    Vector<String> picture;
    Vector<String> interested, going, shares;

    public Event(){}

    public Event(String id, String startDate, String endDate, String title, String description, String location, Vector<String> picture, Vector<String> interested, Vector<String> going, Vector<String> shares) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
        this.location = location;
        this.picture = picture;
        this.interested = interested;
        this.going = going;
        this.shares = shares;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Vector<String> getPicture() {
        return picture;
    }

    public void setPicture(Vector<String> picture) {
        this.picture = picture;
    }

    public Vector<String> getInterested() {
        return interested;
    }

    public void setInterested(Vector<String> interested) {
        this.interested = interested;
    }

    public Vector<String> getGoing() {
        return going;
    }

    public void setGoing(Vector<String> going) {
        this.going = going;
    }

    public Vector<String> getShares() {
        return shares;
    }

    public void setShares(Vector<String> shares) {
        this.shares = shares;
    }
}
