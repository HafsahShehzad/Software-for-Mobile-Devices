package com.example.machine.datastorage_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LogIn extends AppCompatActivity{

    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        Button loginButton=findViewById(R.id.button);




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                EditText email=findViewById(R.id.emailET);
                EditText password=findViewById(R.id.passET);
                String getEmail=email.getText().toString();
                String getPassword=password.getText().toString();
                boolean check=false;
                int id=0;
                List<User>usersList=AppDatabase.getAppDatabase(getApplicationContext()).userDao().getAll(); //get data from list
                for(int i=0;i<usersList.size();i++){
                    if(usersList.get(i).getEmail().toString().equals(getEmail) && usersList.get(i).getPassword().toString().equals(getPassword)){
                        check=true;
                        id=usersList.get(i).getUid();
                        break;
                    }

                }
                Log.d("id","id: "+id);
                if(check==true){
                    Intent i = new Intent (LogIn.this, UserProfile.class);
                    String uid=Integer.toString(id);

                    i.putExtra("key",uid);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Sorry, you must create an account to log in",Toast.LENGTH_SHORT).show();
                }
            }
        });

       /* loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkUser= AppDatabase.getAppDatabase(getApplicationContext()).userDao().findUidByEmailPassword(getEmail,getPassword);
                Log.d("users","check "+checkUser);

                if(checkUser==false){
                    Toast.makeText(getApplicationContext(),"Sorry, you must create an account to log in",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i=new Intent(LogIn.this,UserProfile.class);
                    i.putExtra("Extra_Email",getEmail);
                    i.putExtra("Extra_password",getPassword);
                    startActivity(i);
                }

            }
        });
        */

    }

}
