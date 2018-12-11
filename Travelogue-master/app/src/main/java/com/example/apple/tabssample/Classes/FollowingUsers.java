package com.example.apple.tabssample.Classes;

public class FollowingUsers {

    private int mImageDrawable;
    private String mName;
    private String FollowingOrFollwer;

    public FollowingUsers(int mImageDrawable, String mName, String followingOrFollwer) {
        this.mImageDrawable = mImageDrawable;
        this.mName = mName;
        FollowingOrFollwer = followingOrFollwer;
    }

    public int getmImageDrawable() {
        return mImageDrawable;
    }

    public String getmName() {
        return mName;
    }

    public String getFollowingOrFollwer() {
        return FollowingOrFollwer;
    }

    public void setmImageDrawable(int mImageDrawable) {
        this.mImageDrawable = mImageDrawable;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setFollowingOrFollwer(String followingOrFollwer) {
        FollowingOrFollwer = followingOrFollwer;
    }
}
