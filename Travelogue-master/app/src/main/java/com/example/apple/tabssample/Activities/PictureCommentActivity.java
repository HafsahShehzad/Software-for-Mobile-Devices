package com.example.apple.tabssample.Activities;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apple.tabssample.Classes.PictureComment;
import com.example.apple.tabssample.Classes.Trip;
import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.firebase.database.DatabaseReference;

public class PictureCommentActivity extends AppCompatActivity {

    Toolbar toolbar;

    String picID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_comment);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button publish_comment_button = (Button) findViewById(R.id.writeCommentButton);

        Intent i = new Intent();
        picID =i.getStringExtra("PictureID");


        publish_comment_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                final EditText comment = (EditText) findViewById(R.id.writeComment);
                final String Get_comment = comment.getText().toString();


                PictureComment new_picture_comment = new PictureComment("", picID, Get_comment);
                final DatabaseReference commentsRef = firebaseRef.getInstance().getMyRef().child("Comments");
                final String newKey = commentsRef.push().getKey();
                //DatabaseReference key = firebaseRef.getInstance().getMyRef().child("Events").push();

                new_picture_comment.setID(newKey);
                commentsRef.child(newKey).setValue(new_picture_comment);

                String ref_to_cmnt = commentsRef.child(newKey).child("Comment").getKey();
                commentsRef.child(newKey).child("Comment").push().setValue(Get_comment);

                Intent intent=new Intent();
                intent.putExtra("comment",Get_comment);
                setResult(RESULT_OK,intent);

                finish();

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
}
