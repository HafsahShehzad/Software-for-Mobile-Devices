package com.example.machine.datastorage_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class UserProfile extends Activity {


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        TextView uid=findViewById(R.id.idTV);
        TextView firstName=findViewById(R.id.fNameTv);
        TextView lastName=findViewById(R.id.lNameTv);
        TextView email=findViewById(R.id.emailTV);

        //String LoginEmail=getIntent().getStringExtra("Extra_Email");
        //String LoginPassword=getIntent().getStringExtra("Extra_password");
        Intent i=getIntent();

        List<User>userList=AppDatabase.getAppDatabase(this).userDao().getAll();
        String id=i.getStringExtra("key");
        Bundle extras = getIntent().getExtras();
        int age = extras.getInt("key");
        uid.setText("User ID: "+id.toString());
        int x=Integer.parseInt(id);
        String fname=userList.get(x).getFirstName();
        String lname=userList.get(x).getLastName().toString();
        String emailaddress=userList.get(x).getEmail().toString();
        firstName.setText("First Name: "+fname);
        lastName.setText("Last Name: "+lname);
        email.setText("Email: "+emailaddress);

        Button update=findViewById(R.id.updateButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserProfile.this,UpdatePassword.class);
                startActivity(i);
            }
        });

      /*  int Userid=AppDatabase.getAppDatabase(this).userDao().findUID(LoginEmail,LoginPassword);
        String id=Integer.toString(Userid);
        uid.setText("User ID : "+id.toString());
        email.setText(LoginEmail);
        String fname=AppDatabase.getAppDatabase(this).userDao().findFirstName(LoginEmail,LoginPassword);
        String lname=AppDatabase.getAppDatabase(this).userDao().findLastName(LoginEmail,LoginPassword);

        firstName.setText("First Name: "+fname.toString());
        lastName.setText("Last Name: "+ lname.toString());*/

    }
}
