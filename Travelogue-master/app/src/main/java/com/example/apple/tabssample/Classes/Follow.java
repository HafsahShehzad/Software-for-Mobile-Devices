package com.example.apple.tabssample.Classes;

public class Follow {
    private String Uid;  //this is your UID
    private String FollowID; //this is the UID of the person youre following
    private boolean follower; //this is to see if you're a follower
    private boolean following; //this is to see if they're following

    public Follow(String Uid, String FollowID){
        this.Uid=Uid;
        this.FollowID=FollowID;
        this.follower=true;
        this.following=false;

    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFollowID() {
        return FollowID;
    }

    public void setFollowID(String followID) {
        FollowID = followID;
    }

    public boolean isFollower() {
        return follower;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}

