package com.example.apple.tabssample.Activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.firebaseRef;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    Button followersButton, followingButton, editProfileButton, createTripButton, LogoutButton;
    TextView name, desc;
    CircleImageView image;
    ProgressDialog progressDialog;
    String userName, descripton, profile, cover, email, password;
    LinearLayout background;

    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        followersButton = findViewById(R.id.viewFollowersButton);
        followingButton = findViewById(R.id.viewFollowingButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        createTripButton = findViewById(R.id.createTripButton);
        LogoutButton = findViewById(R.id.LogoutButton);
        name = findViewById(R.id.nameTV);
        desc = findViewById(R.id.descTV);
        image = findViewById(R.id.profile_image);
        background = findViewById(R.id.layout);

        // Intent i=getIntent();
        // String userID=i.getStringExtra("UID");

        if (!firebaseRef.getInstance().isNewUser() && firebaseRef.getInstance().getIsVisited() == 0) {
            setProfile();

        } else {
            getUserInfo();
        }


    }


    public void setProfile() {
        String uid = firebaseRef.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting your information");
        progressDialog.show();


        Query getUserInfo = firebaseRef.getInstance().getMyRef().child("User").orderByChild("uid").equalTo(uid);
        getUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    userName = postSnapshot.child("full_name").getValue(String.class);
                    descripton = postSnapshot.child("user_description").getValue(String.class);
                    profile = postSnapshot.child("profile_picture").getValue(String.class);
                    cover = postSnapshot.child("cover_picture").getValue(String.class);
                    email = postSnapshot.child("email_address").getValue(String.class);
                    password = postSnapshot.child("password").getValue(String.class);
                    String username = postSnapshot.child("username").getValue(String.class);

                    User tempUser = new User(userName, email, username, password);
                    tempUser.setProfile_picture(profile);
                    tempUser.setUser_description(descripton);
                    tempUser.setCover_picture(cover);
                    firebaseRef.getInstance().setUser(tempUser);
                    firebaseRef.getInstance().setVisited(1);


                    flag = true;
                }

                if (flag) {
                    name.setText(userName);
                    desc.setText(descripton);
                    if (profile.equals("")) {
                        final StorageReference mUserProfilePic = FirebaseStorage.getInstance().getReference()
                                .child("icon.jpg");
                        String pictureUrl = mUserProfilePic.getDownloadUrl().toString();
                        Picasso.get().load(pictureUrl).into(image);
                    } else {
                        //decode string
                        progressDialog.setTitle("loading profile picture");
                        String uid = firebaseRef.getInstance().getCurrentUser();
                        final StorageReference mUserProfilePic = FirebaseStorage.getInstance().getReference()
                                .child("profilePics").child(uid).child("profilePic.jpg");
                        String pictureUrl = mUserProfilePic.getDownloadUrl().toString();
                        Picasso.get().load(pictureUrl).into(image);

                    }
                    if (!cover.equals("")) {
                        progressDialog.setTitle("loading cover picture");
                        String uid = firebaseRef.getInstance().getCurrentUser();
                        final StorageReference mUserCoverPicture = FirebaseStorage.getInstance().getReference().child("coverPictures")
                                .child(uid).child("coverPic.jpg");
                        String pictureUrl = mUserCoverPicture.getDownloadUrl().toString();
                        Target target = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                BitmapDrawable bitDraw = new BitmapDrawable(bitmap);
                                background.setBackground(bitDraw);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        };

                        Picasso.get().load(pictureUrl).into(target);


                    }

                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

    public void logout(View v) {
        firebaseRef.getInstance().setLoggedin(false);
        firebaseRef.getInstance().setCurrentUser(null);
        firebaseRef.getInstance().setUser(null);
        firebaseRef.getInstance().setVisited(1);
        Intent i = new Intent(this, homepage.class);
        startActivity(i);
    }

    public void createEvent(View v) {
        Intent i = new Intent(this, CreateEvent.class);
        startActivity(i);
    }

    public void editProfile(View v) {
        Intent i = new Intent(this, editProfile.class);
        startActivity(i);
    }

    public void createTrip(View v) {
        Intent i = new Intent(this, createTrip.class);
        startActivity(i);
    }

    public void createPost(View v) {
        Intent i = new Intent(this, createPost.class);
        startActivity(i);
    }

    public void getUserInfo() {
        name.setText(firebaseRef.getInstance().getUser().getFull_name());
        desc.setText(firebaseRef.getInstance().getUser().getUser_description());
        profile = firebaseRef.getInstance().getUser().getProfile_picture();
        cover = firebaseRef.getInstance().getUser().getCover_picture();

        if (profile.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.usericon);
            image.setImageBitmap(bitmap);
        } else {
            String pictureUrl = firebaseRef.getInstance().getUser().getProfile_picture();
            Picasso.get().load(pictureUrl).into(image);
        }
        if (!cover.isEmpty()) {
            String pictureUrl = firebaseRef.getInstance().getUser().getCover_picture();
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    BitmapDrawable bitDraw = new BitmapDrawable(bitmap);
                    background.setBackground(bitDraw);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.get().load(pictureUrl).into(target);

        }

    }


    public void followers(View v) {
        Intent i = new Intent(this, FollowersAndFollowing.class);
        startActivity(i);
    }

}