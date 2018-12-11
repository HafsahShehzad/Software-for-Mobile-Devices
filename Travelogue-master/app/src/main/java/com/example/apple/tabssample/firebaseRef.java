package com.example.apple.tabssample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.apple.tabssample.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class firebaseRef {
    static int count=0;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    DatabaseReference users;
    boolean loggedin;
    String currentUser;
    int isVisited=0;
    boolean newUser;
    User user;
    SharedPreferences sharedPreferences;

    private static final firebaseRef ourInstance = new firebaseRef();

    public static firebaseRef getInstance() {
        return ourInstance;
    }

    private firebaseRef() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        users=myRef.child("User");

    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setMyRef(DatabaseReference myRef) {
        this.myRef = myRef;
    }

    public DatabaseReference getUsers() {
        return users;
    }

    public void setUsers(DatabaseReference users) {
        this.users = users;
    }

    public void setVisited(int visited){
        this.isVisited=visited;
    }
    public int getIsVisited(){

        return isVisited;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public DatabaseReference getUserNode()
    {
        return myRef.child("User").child(currentUser);
    }

    public static SharedPreferences getSp(Activity activity){
        return PreferenceManager.getDefaultSharedPreferences(activity);
    }
}
