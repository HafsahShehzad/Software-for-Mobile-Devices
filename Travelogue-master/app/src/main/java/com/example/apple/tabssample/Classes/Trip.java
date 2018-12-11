package com.example.apple.tabssample.Classes;

import java.util.ArrayList;
import java.util.Vector;

public class Trip {

    String TripID;
    String TripTitle;
    String description;
    String startDate,endDate;
    String startocation, endLocation;
    ArrayList<Picture>pictures;
    ArrayList<String> p;

    public Trip( String tripTitle, String description, String startDate, String endDate, String startocation, String endLocation) {
        //TripID = tripID;
        TripTitle = tripTitle;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startocation = startocation;
        this.endLocation = endLocation;
        //this.pictures = new ArrayList<>();
        // this.p = p;

    }

    public void addPictures(Vector<String>picture){
        Picture new_picture=new Picture(picture);
        pictures.add(new_picture);
    }

    public String getTripID() {
        return TripID;
    }

    public void setTripID(String tripID) {
        TripID = tripID;
    }

    public String getTripTitle() {
        return TripTitle;
    }

    public void setTripTitle(String tripTitle) {
        TripTitle = tripTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStartocation() {
        return startocation;
    }

    public void setStartocation(String startocation) {
        this.startocation = startocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public ArrayList<String> getP() {
        return p;
    }

    public void setP(ArrayList<String> p) {
        this.p = p;
    }
}
