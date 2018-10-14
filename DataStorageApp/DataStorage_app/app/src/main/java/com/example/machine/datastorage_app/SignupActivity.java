package com.example.machine.datastorage_app;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SignupActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       final EditText email=findViewById(R.id.editEmail);
       final EditText fname=findViewById(R.id.editFName);
       final EditText lname=findViewById(R.id.editLName);

        Button register_button = (Button) findViewById(R.id.Register);
        register_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(fname.getText().toString()) || TextUtils.isEmpty(lname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), " ERROR! Enter the fields above to register", Toast.LENGTH_SHORT).show();
                }
                else{

                    DatabaseInitializer databaseInit = new DatabaseInitializer();
                    databaseInit.populateAsync(AppDatabase.getAppDatabase(getApplicationContext()));
                    Toast.makeText(getApplicationContext(), "You are registered successfully", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onDestroy()
    {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

    public class DatabaseInitializer
    {
        public void populateAsync(@NonNull final AppDatabase db) {
            PopulateDbAsync task = new PopulateDbAsync(db);
            task.execute();
        }

        public void populateSync(@NonNull final AppDatabase db) {
            populateWithTestData(db);
        }

        private User addUser(final AppDatabase db, User user) {
            db.userDao().insertAll(user);
            return user;
        }

        private void populateWithTestData(AppDatabase db) {

            final EditText email = (EditText) findViewById(R.id.editEmail);
            final EditText password = (EditText) findViewById(R.id.editPassword);
            final EditText firstName = (EditText) findViewById(R.id.editFName);
            final EditText lastName = (EditText) findViewById(R.id.editLName);

            final String Get_firstName = firstName.getText().toString();
            final String Get_lastName = lastName.getText().toString();
            final String Get_email = email.getText().toString();
            final String Get_password = password.getText().toString();

            User user = new User();
            user.setFirstName(Get_firstName);
            user.setLastName(Get_lastName);
            user.setEmail(Get_email);
            user.setPassword(Get_password);

            addUser(db, user);

            //Toast.makeText(getApplicationContext(), "Email: "+ Get_email + "\n" + "Password: " + Get_password + "\n" +"firstname: " + Get_firstName + "\n" + "lastName: " + Get_lastName ,  Toast.LENGTH_LONG).show();
            List<User> userList = db.userDao().getAll();
              Log.d( "users:  ","Rows Count: " + userList.size());
              for(int i=0;i<userList.size();i++){
                  System.out.println(userList.get(i).getUid()+userList.get(i).getEmail()+" "+userList.get(i).getPassword());
            }

        }

        private class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
            private final AppDatabase mDb;

            PopulateDbAsync(AppDatabase db) {
                mDb = db;
            }

            @Override
            protected Void doInBackground(final Void... params) {
                populateWithTestData(mDb);
                return null;
            }

        }
    }
}




