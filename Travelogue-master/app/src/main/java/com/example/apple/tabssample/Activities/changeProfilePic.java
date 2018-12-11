package com.example.apple.tabssample.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class changeProfilePic extends ActivityManagePermission {

    Button change;
    ImageView imageView;
    int done;
    Uri FilePath;

    final static int RESULT_LOAD_IMG=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        String profile= firebaseRef.getInstance().getUser().getProfile_picture();
        change=findViewById(R.id.button);
        imageView=findViewById(R.id.imageView2);
        String pictureUrl = firebaseRef.getInstance().getUser().getProfile_picture();
        Picasso.get().load(pictureUrl).into(imageView);
    }

    public void changePicture(View v){
        boolean isPermissionGranted = isPermissionGranted(changeProfilePic.this, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE);
        if (isPermissionGranted)
        {
            //open gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

        }
        else
        {
            askCompactPermission(PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, new PermissionResult() {
                @Override
                public void permissionGranted() {
                    //permission granted
                    //open gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

                }

                @Override
                public void permissionDenied() {
                    //permission denied
                    //replace with your action
                    Intent i=new Intent(changeProfilePic.this, profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                @Override
                public void permissionForeverDenied() {
                    // user has check never ask again
                    // you need to open setting manually
                    openSettingsApp(changeProfilePic.this);
                }
            });
        }
    }

    protected void onActivityResult(int RequestCode, int ResultCode, Intent i) {
        super.onActivityResult(RequestCode, ResultCode, i);
        try {
            if (RequestCode == RESULT_LOAD_IMG && ResultCode == RESULT_OK && null != i) {
                FilePath = i.getData();
                String[] PathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(FilePath, PathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(PathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(getApplicationContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public void done(View v){
       final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading profile picture...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String uid=firebaseRef.getInstance().getCurrentUser();

        final StorageReference mUserProfilePic = FirebaseStorage.getInstance().getReference()
                .child("profilePics").child(uid).child("profilePic.jpg");

        mUserProfilePic.putFile(FilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mUserProfilePic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        //Do what you want with the url
                        Toast.makeText(changeProfilePic.this, "Upload Done", Toast.LENGTH_LONG).show();

                        firebaseRef.getInstance().getUserNode().child("profile_picture").setValue(uri.toString());
                        progressDialog.dismiss();
                        firebaseRef.getInstance().getUser().setProfile_picture(uri.toString());
                        SharedPreferences.Editor editor=firebaseRef.getSp(changeProfilePic.this).edit();
                        editor.putString("profile",uri.toString());
                        editor.apply();

                        Intent i=new Intent(changeProfilePic.this, profile.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(changeProfilePic.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        UploadTask uploadTask = mUserProfilePic.putFile(FilePath);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
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
