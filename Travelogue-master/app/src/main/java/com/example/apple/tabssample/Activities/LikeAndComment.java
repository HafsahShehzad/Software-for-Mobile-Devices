package com.example.apple.tabssample.Activities;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.apple.tabssample.Classes.PictureComment;
import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.ArrayList;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class LikeAndComment extends AppCompatActivity {

    ListView simpleList;
    ArrayList<String> commentList = new ArrayList<>();

    String Picref = "abc";

    Toolbar toolbar;

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_and_comment);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.imageView);

        Intent i = getIntent();
        Picref = i.getStringExtra("uri");
        Picasso.get().load(Picref).into(img);

        final DatabaseReference commentsRef = firebaseRef.getInstance().getMyRef().child("Comments");
        Query picturesQuery = commentsRef.orderByChild("pictureID").equalTo(Picref);
        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    for (DataSnapshot c : postSnapshot.child("Comment").getChildren()){
                        String comment = c.getValue(String.class);
                        commentList.add(comment);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button comment_button = (Button) findViewById(R.id.commentButton);
        comment_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent (getApplicationContext(), PictureCommentActivity.class);
                i.putExtra("PictureID", Picref);
                startActivityForResult(i, 22);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 22 && resultCode == RESULT_OK)
        {

            String comment = data.getStringExtra("comment");

            commentList.add(comment);

            simpleList = (ListView)findViewById(R.id.listviewComments);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, commentList);
            simpleList.setAdapter(arrayAdapter);

        }
    };

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
