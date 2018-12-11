package com.example.apple.tabssample.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.firebaseRef;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.util.List;

public class SplashScreen extends ActivityManagePermission {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSharedPreferences();
        /*askCompactPermissions(new String[]{PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
                        PermissionUtils.Manifest_READ_EXTERNAL_STORAGE},
                new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        //permission granted
                        //replace with your action
                        SplashScreenActivate();
                    }

                    @Override
                    public void permissionDenied() {
                        //permission denied
                        //replace with your action
                        SplashScreenActivate();
                    }
                    @Override
                    public void permissionForeverDenied() {
                        // user has check 'never ask again'
                        // you need to open setting manually
                        openSettingsApp(SplashScreen.this);
                    }
                });*/


    }

    public void SplashScreenActivate() {

        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, homepage.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void checkSharedPreferences() {
        SharedPreferences pref = firebaseRef.getSp(SplashScreen.this);
        String uid = firebaseRef.getSp(SplashScreen.this).getString("UID", "");
        if (!uid.isEmpty()) {
            String name = firebaseRef.getSp(SplashScreen.this).getString("name", "");
            String email = firebaseRef.getSp(SplashScreen.this).getString("email", "");
            String password = firebaseRef.getSp(SplashScreen.this).getString("password", "");
            String username = firebaseRef.getSp(SplashScreen.this).getString("username", "");
            String profile = firebaseRef.getSp(SplashScreen.this).getString("profile", "");
            String location = firebaseRef.getSp(SplashScreen.this).getString("location", "");
            String cover = firebaseRef.getSp(SplashScreen.this).getString("cover", "");

            User tempUser = new User(name, email, username, password);
            tempUser.setLocation(location);
            tempUser.setCover_picture(cover);
            tempUser.setProfile_picture(profile);

            firebaseRef.getInstance().setUser(tempUser);
            firebaseRef.getInstance().setCurrentUser(uid);

            Intent i = new Intent(SplashScreen.this, PostActivity.class);
            startActivity(i);
        } else {
            askCompactPermissions(new String[]{PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
                            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE},
                    new PermissionResult() {
                        @Override
                        public void permissionGranted() {
                            //permission granted
                            //replace with your action
                            SplashScreenActivate();
                        }

                        @Override
                        public void permissionDenied() {
                            //permission denied
                            //replace with your action
                            SplashScreenActivate();
                        }

                        @Override
                        public void permissionForeverDenied() {
                            // user has check 'never ask again'
                            // you need to open setting manually
                            openSettingsApp(SplashScreen.this);
                        }
                    });

        }
    }
}
