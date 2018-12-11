package com.example.apple.tabssample.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.firebaseRef;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.apple.tabssample.R;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class signup extends ActivityManagePermission {

    EditText fullname, username, email, password;
    Button register, login;
    String getname, getmail, getpass, getusername, userLocation;

    ProgressDialog progressDialog;
    FusedLocationProviderClient client;


    protected void onCreate(Bundle savedInstancestate) {

        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_signup);
        fullname = findViewById(R.id.fullnameET);
        email = findViewById(R.id.EmailET);
        password = findViewById(R.id.PasswordET);
        username = findViewById(R.id.usernameET);
        login = findViewById(R.id.redirectLogin);
        register = findViewById(R.id.SignupButton);


    }

    public void redirectLogin(View v) {
        Intent i = new Intent(this, login.class);
        startActivity(i);
    }

    public boolean checkValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(signup.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    userLocation = location.toString();
                }
            }
        });

    }

    public void signup(View v) {

        getname = fullname.getText().toString();
        getmail = email.getText().toString();
        getpass = password.getText().toString();
        getusername = username.getText().toString();

        if (TextUtils.isEmpty(fullname.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter full name to create account", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter email to create account", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter username to create account", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter password to create account", Toast.LENGTH_SHORT).show();
        } else {
            //validate email
            if (!checkValidEmail(getmail)) {
                email.setText("");
                password.setText("");
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                email.startAnimation(shake);
                email.setHighlightColor(getResources().getColor(R.color.red));
                email.setError("Please enter a valid email");

            } else {
                //validate password
                if (getpass.length() < 7) {
                    password.setText("");
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    password.startAnimation(shake);
                    password.setHighlightColor(getResources().getColor(R.color.red));
                    password.setError("Password should be 7 characters");

                } else {
                    //check if they exist
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Please wait while your details are processed");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Query signUpCheck = firebaseRef.getInstance().getMyRef().child("User")
                            .orderByChild("email_address").equalTo(getmail);
                    signUpCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean flag = false;

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                // this is how you can read data
                                // String email = postSnapshot.child("email_address").getValue(String.class);
                                // String full_name = postSnapshot.child("full_name").getValue(String.class);
                                flag = true;
                            }
                            if (flag) {

                                password.setText("");
                                email.setText("");
                                fullname.setText("");
                                username.setText("");
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Email already in use! Try again", Toast.LENGTH_LONG).show();
                            } else {
                                User new_user = new User(getname, getmail, getusername, getpass);
                                //getLocation();
                                //new_user.setLocation(userLocation);
                                DatabaseReference key = firebaseRef.getInstance().getMyRef().child("User").push();
                                //firebaseRef.getInstance().getMyRef().child("User").child(getmail).setValue(new_user);
                                new_user.setUID(key.getKey());
                                key.setValue(new_user);
                                firebaseRef.getInstance().setCurrentUser(key.getKey());
                                firebaseRef.getInstance().setUser(new_user);
                                firebaseRef.getInstance().setNewUser(true);
                                firebaseRef.getInstance().setLoggedin(true);
                                firebaseRef.getInstance().setVisited(1);
                                //setting shared preference
                                SharedPreferences.Editor editor = firebaseRef.getSp(signup.this).edit();
                                editor.putString("UID", key.getKey());
                                editor.putString("name", getname);
                                editor.putString("email", getmail);
                                editor.putString("password", getpass);
                                editor.putString("username", getusername);
                                editor.apply();


                                Log.d("Check", "I SIGNED UP");
                                progressDialog.dismiss();
                                Intent i = new Intent(signup.this, setPicture.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }


        }
    }
}
