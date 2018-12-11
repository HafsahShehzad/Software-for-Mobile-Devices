package com.example.apple.tabssample.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    Button login,forgot_password,signup;
    EditText email,password;

    ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.LoginButton);
        forgot_password=findViewById(R.id.forgot_passwordButton);
        signup=findViewById(R.id.redirectSignup);
        email=findViewById(R.id.emailET);
        password=findViewById(R.id.usernameET);

    }

    public void login(View v){
        if(TextUtils.isEmpty(email.getText())){
            Toast.makeText(getApplicationContext(),"Please enter email or username to log in",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password.getText())){
            Toast.makeText(getApplicationContext(),"Please enter valid password to log in",Toast.LENGTH_LONG).show();
        }
        else{
            //check valid password and email
            final String email_add=email.getText().toString();
            progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Verifying your details");
            progressDialog.show();

            final Query loginCheck = firebaseRef.getInstance().getMyRef().child("User")
                    .orderByChild("email_address").equalTo(email_add);
            loginCheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    boolean flag = false;
                    progressDialog.dismiss();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        String pass = password.getText().toString();
                        String dbPassword = postSnapshot.child("password").getValue(String.class);
                        String dbEmail = postSnapshot.child("email_address").getValue(String.class);
                        String userId= postSnapshot.child("uid").getValue(String.class);
                        String profile=postSnapshot.child("profile_picture").getValue(String.class);
                        String cover=postSnapshot.child("cover_picture").getValue(String.class);
                        String name=postSnapshot.child("full_name").getValue(String.class);
                        String username=postSnapshot.child("cover_picture").getValue(String.class);
                        String location=postSnapshot.child("location").getValue(String.class);

                        if(pass.equals(dbPassword)) {
                            Intent i=new Intent(login.this, PostActivity.class);


                            firebaseRef.getInstance().setCurrentUser(userId);
                            firebaseRef.getInstance().setLoggedin(true);
                            firebaseRef.getInstance().setNewUser(false);
                            SharedPreferences.Editor editor=firebaseRef.getSp(login.this).edit();
                            editor.putString("email",dbEmail);
                            editor.putString("password",dbPassword);
                            editor.putString("UID",userId);
                            editor.putString("name",name);
                            editor.putString("username",username);
                            editor.putString("profile",profile);
                            editor.putString("cover",cover);
                            editor.putString("location",location);
                            editor.apply();

                            User tempUser=new User(name, dbEmail,username,dbPassword);
                            tempUser.setProfile_picture(profile);
                            tempUser.setCover_picture(cover);
                            tempUser.setLocation(location);
                            firebaseRef.getInstance().setUser(tempUser);


                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid password! please try again",
                                    Toast.LENGTH_LONG).show();
                            password.setText("");
                            Animation shake= AnimationUtils.loadAnimation(login.this,R.anim.shake);
                            password.startAnimation(shake);
                        }
                        return;
                    }

                    Toast.makeText(getApplicationContext(),"Invalid email! Please try again",
                            Toast.LENGTH_LONG).show();
                    //password.setText("");
                    Animation shake= AnimationUtils.loadAnimation(login.this,R.anim.shake);
                    email.startAnimation(shake);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
    }

    public void forgotPassword(View v){
        Intent i=new Intent(this,forgotPassword.class);
        startActivity(i);
        finish();
    }

    public void createAccount(View v){
        Intent i=new Intent(this,signup.class);
        startActivity(i);
        finish();
    }

}
