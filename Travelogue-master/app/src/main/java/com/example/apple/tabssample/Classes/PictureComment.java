package com.example.apple.tabssample.Classes;

public class PictureComment {

    private String PictureID;
    private String Comment;
    private String ID;

    public PictureComment(String pictureID, String comment, String ID) {
        PictureID = pictureID;
        Comment = comment;
        this.ID = ID;
    }


    public String getPictureID() {
        return PictureID;
    }

    public void setPictureID(String pictureID) {
        PictureID = pictureID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
