package com.example.apple.tabssample.Classes;

public class Share {

    private static int count=0;
    private String UID;
    private String PID;
    private String SID;

    public Share(String UID, String PID){
        this.UID=UID;
        this.PID=PID;
        this.SID=String.valueOf(++count);
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }
}
