package com.example.apple.tabssample.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apple.tabssample.R;

public class homepage extends AppCompatActivity {
    Button login,signup,facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        login=findViewById(R.id.loginButton);
        facebook=findViewById(R.id.fbButton);
        signup=findViewById(R.id.emailButton);


    }

    public void loginclick(View v){
        if(v==login){
            Intent i=new Intent(this,login.class);
            startActivity(i);
        }

    }

    public void signupClick(View v){
        if(v==signup){
            Intent i=new Intent(this,signup.class);
            startActivity(i);
        }
    }


}
