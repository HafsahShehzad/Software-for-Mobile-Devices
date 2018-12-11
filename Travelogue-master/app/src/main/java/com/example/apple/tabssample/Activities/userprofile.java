package com.example.apple.tabssample.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;

public class userprofile extends AppCompatActivity {

    Toolbar toolbar;

    CheckBox follow;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        follow=findViewById(R.id.checkBox);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
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
                firebaseRef.getInstance().setLoggedin(false);
                firebaseRef.getInstance().setCurrentUser(null);
                firebaseRef.getInstance().setUser(null);
                firebaseRef.getInstance().setVisited(0);

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().commit();

                startActivity(new Intent(this, homepage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkFollowing(View v){
        if(follow.isChecked()){
            follow.setText("Following");

        }
        else{
            follow.setText("Follow");
            //remove from following list
        }
    }
}
