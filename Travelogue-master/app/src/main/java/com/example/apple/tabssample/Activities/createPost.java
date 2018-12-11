package com.example.apple.tabssample.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.firebaseRef;
import com.example.apple.tabssample.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.example.apple.tabssample.Classes.Post;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import com.example.apple.tabssample.Classes.Likes;
import com.example.apple.tabssample.Classes.Share;
import com.example.apple.tabssample.Classes.PictureComment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;


public class createPost extends ActivityManagePermission {

    TextView status;
    Button createPost,createEevnt,createTrip, addPic;
    private static int RESULT_LOAD_IMG=1;
    Uri filePathUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        addPic=findViewById(R.id.addPhoto);

        status=findViewById(R.id.status);
        createEevnt=findViewById(R.id.createEvent);
        createPost=findViewById(R.id.createPost);
        createTrip=findViewById(R.id.createTrip);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public void onClickAddPicture(View v){
        boolean isPermissionGranted = isPermissionGranted(createPost.this, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE);
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
                    Intent i=new Intent(createPost.this, PostActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                @Override
                public void permissionForeverDenied() {
                    // user has check never ask again
                    // you need to open setting manually
                    openSettingsApp(createPost.this);
                }
            });
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                filePathUri = data.getData();
                String[] PathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(filePathUri, PathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(PathColumn[0]);
                String picture = cursor.getString(columnIndex);
                cursor.close();
                //Bitmap bitmap = BitmapFactory.decodeFile(picture);


            }
            else {
                Toast.makeText(getApplicationContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }





    public void OncreatePost(View v){
        DatabaseReference ref=firebaseRef.getInstance().getMyRef().child("Posts").push();
        ref.setValue(new Post(ref.getKey()+"",
                getDateTime(),
                "",
                status.getText()+"",
                status.getText()+"",
                firebaseRef.getInstance().getCurrentUser(),
                new Vector<String> (),
                new Vector<Likes>() ,
                new Vector<PictureComment> (),
                new Vector<Share> ()
                ));
        final StorageReference mPostPic = FirebaseStorage.getInstance().getReference()
                .child("postPics").child(ref.getKey()).child("postPic.jpg");

        mPostPic.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                mPostPic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        //Do what you want with the url
                        Toast.makeText(createPost.this, "Upload Done", Toast.LENGTH_LONG).show();
                        DatabaseReference ref=firebaseRef.getInstance().getMyRef().child("Posts").push();
                        ref.child(ref.getKey()).child("pictures").setValue(uri.toString());

                        Intent i=new Intent(createPost.this, PostActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // progressDialog.dismiss();
                Toast.makeText(createPost.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        UploadTask uploadTask = mPostPic.putFile(filePathUri);

    }

    public void onCreateTrip(View v){
        Intent i=new Intent(this, createTrip.class);
        startActivity(i);
    }
    public void onCreateEvent(View v){
        Intent i=new Intent(this, CreateEvent.class);
        startActivity(i);
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
