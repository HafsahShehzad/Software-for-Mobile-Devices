package com.example.apple.tabssample.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.apple.tabssample.Classes.FollowingUsers;
import com.example.apple.tabssample.FollowingUsersAdapter;
import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;

import java.util.ArrayList;


public class FollowersAndFollowing extends AppCompatActivity {


    private ListView listView;
    private FollowingUsersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_and_following);

        listView = (ListView) findViewById(R.id.FollowingFollowersList);
        ArrayList<FollowingUsers> UserList = new ArrayList<>();
        UserList.add(new FollowingUsers(R.drawable.final_image, "User1" , "Follower"));
        UserList.add(new FollowingUsers(R.drawable.images, "User2" , "Following"));
        UserList.add(new FollowingUsers(R.drawable.images, "User3" , "Following"));
        UserList.add(new FollowingUsers(R.drawable.final_image, "User4" , "Follower"));
        UserList.add(new FollowingUsers(R.drawable.final_image, "User5" , "Follower"));
        UserList.add(new FollowingUsers(R.drawable.images, "User6" , "Following"));
        UserList.add(new FollowingUsers(R.drawable.images, "User7" , "Following"));
        UserList.add(new FollowingUsers(R.drawable.images, "User8" , "Following"));

        mAdapter = new FollowingUsersAdapter(this,UserList);
        listView.setAdapter(mAdapter);


    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_profile:
                startActivity(new Intent(this, profile.class));
                return true;
            case R.id.edit_profile:
                startActivity(new Intent(this, editProfile.class));
                return true;
            case R.id.view_events:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.logout:
                startActivity(new Intent(this, homepage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
