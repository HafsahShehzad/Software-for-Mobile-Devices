package com.example.apple.tabssample.Activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.firebaseRef;

import com.example.apple.tabssample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class forgotPassword extends AppCompatActivity {
    EditText email;
    Button recoveryButton;
    String uid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        recoveryButton = findViewById(R.id.recoveryButton);
        email = findViewById(R.id.recoveryEmailET);
    }

    public void recoveryClick(View v) {
        if (TextUtils.isEmpty(email.getText())) {
            Toast.makeText(getApplicationContext(), "Email/username must be entered!", Toast.LENGTH_SHORT).show();
        } else {


            Query verifyEmail = firebaseRef.getInstance().getMyRef().child("User")
                    .orderByChild("email_address").equalTo(email.getText().toString());


            verifyEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean flag = false;

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        flag = true;
                        uid = postSnapshot.getKey();

                    }

                   /* if(flag){
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String uid=firebaseRef.getInstance().getCurrentUser();
                                            firebaseRef.getInstance().getMyRef().child("User").child(uid).child("password").setValue("trvl218smd3");
                                           Toast.makeText(getApplicationContext(),"Email has been sent!",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                });


                    }*/
                    if (flag) {
                        String Subject = "Password Reset: Travelogue";
                        String message = "Dear user, " +
                                "You have requested to change your password. Your new password is trvl218smd3" +
                                "Happy travelling!" +
                                "The Travelgogue team";

                        String to = email.getText().toString();
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                        email.putExtra(Intent.EXTRA_SUBJECT, Subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);

                        try {
                            startActivity(Intent.createChooser(email, "Send mail..."));
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        firebaseRef.getInstance().getMyRef().child("User").child(uid).child("password").setValue("trvl218smd3");
                        Toast.makeText(getApplicationContext(), "Email has been sent!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a registered email!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

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
                startActivity(new Intent(this, homepage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
