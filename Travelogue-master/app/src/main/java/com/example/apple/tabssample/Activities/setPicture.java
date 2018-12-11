package com.example.apple.tabssample.Activities;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;

import java.io.ByteArrayOutputStream;

import com.example.apple.tabssample.firebaseRef;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class setPicture extends ActivityManagePermission {

    CircleImageView image;
    Button choose, later;
    int count;
    String picture;
    private static int RESULT_LOAD_IMG=1;
    FirebaseStorage storage;
    StorageReference storageref;
    Uri filePathUri;
    int done;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_profile_picture);
        choose=findViewById(R.id.select);
        later=findViewById(R.id.later);
        image=findViewById(R.id.profile_image);
        progressDialog=new ProgressDialog(this);
        count=0;
        if(count==0){
            later.setText(R.string.not_now);

        }
        else{
            later.setText(R.string.done);
        }
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();

    }



    public void onClickChoose(View v){
        count=1;
        later.setText(R.string.done);
        //ask permission

        boolean isPermissionGranted = isPermissionGranted(setPicture.this, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE);
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
                    Intent i=new Intent(setPicture.this, profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                @Override
                public void permissionForeverDenied() {
                    // user has check never ask again
                    // you need to open setting manually
                    openSettingsApp(setPicture.this);
                }
            });
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                filePathUri = data.getData();
                String[] PathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(filePathUri, PathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(PathColumn[0]);
                picture = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(picture);
                image.setImageBitmap(bitmap);

            }
            else {
                Toast.makeText(getApplicationContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            }

    }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void onClickLater(View v)
    {
        progressDialog.setTitle("Uploading picture");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String uid=firebaseRef.getInstance().getCurrentUser();

        final StorageReference mUserProfilePic = FirebaseStorage.getInstance().getReference()
                .child("profilePics").child(uid).child("profilePic.jpg");

        mUserProfilePic.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mUserProfilePic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        //Do what you want with the url
                        Toast.makeText(setPicture.this, "Upload Done", Toast.LENGTH_LONG).show();

                        firebaseRef.getInstance().getUserNode().child("profile_picture").setValue(uri.toString());
                        progressDialog.dismiss();
                        firebaseRef.getInstance().getUser().setProfile_picture(uri.toString());
                        SharedPreferences.Editor editor=firebaseRef.getSp(setPicture.this).edit();
                        editor.putString("profile",uri.toString());
                        editor.apply();


                        Intent i=new Intent(setPicture.this, profile.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(setPicture.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        UploadTask uploadTask = mUserProfilePic.putFile(filePathUri);

//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
//            {
//                if (!task.isSuccessful())
//                {
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                }
//
//                // Continue with the task to get the download URL
//                return mUserProfilePic.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task)
//            {
//                if (task.isSuccessful())
//                {
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

}
