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
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.firebaseRef;


import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfile extends ActivityManagePermission {
    CheckBox showPassword;
    EditText pass,email,name,description;
    Button changeCover,saveChanges;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    CircleImageView imgView;
    ImageView coverPhoto;
    String fullname, userDescription, userEmail, userPassword, profile, cover;
    ProgressDialog progressDialog;
    Switch location;
    Uri filePath;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        showPassword=findViewById(R.id.showCB);
        pass=findViewById(R.id.passwordET);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(showPassword.isChecked()){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        saveChanges=findViewById(R.id.saveChangesButton);
        email=findViewById(R.id.emailET);
        name=findViewById(R.id.NameET);
        description=findViewById(R.id.descriptionET);
        imgView=findViewById(R.id.profile_image);
        coverPhoto=findViewById(R.id.CoverPhoto);
        location=findViewById(R.id.location);

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(editProfile.this ,changeProfilePic.class);
                startActivity(i);
            }
        });


        setDetails();


    }



    public void setDetails(){
        User current=firebaseRef.getInstance().getUser();
        name.setText(current.getFull_name());
        description.setText(current.getUser_description());
        email.setText(current.getEmail_address());
        pass.setText(current.getPassword());
        profile=current.getProfile_picture();
        cover=current.getCover_picture();

        if(!profile.isEmpty()){
            //progressDialog.setTitle("loading profile picture");
            String pictureUrl = firebaseRef.getInstance().getUser().getProfile_picture();
            Picasso.get().load(pictureUrl).into(imgView);

        }
        else{
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.usericon);
            imgView.setImageBitmap(bitmap);
        }
        if(!cover.equals("")){
           // progressDialog.setTitle("loading cover picture");
            String pictureUrl = firebaseRef.getInstance().getUser().getCover_picture();
            Picasso.get().load(pictureUrl).into(coverPhoto);

        }

    }
    public void setProfileDetails(){

         String uid=firebaseRef.getInstance().getCurrentUser();
         progressDialog=new ProgressDialog(this);
         progressDialog.setTitle("Getting your information");
         progressDialog.show();

         Query getUser=firebaseRef.getInstance().getMyRef().child("User").orderByChild("uid").equalTo(uid);
         getUser.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 boolean flag=true;
                 for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                     fullname=postSnapshot.child("full_name").getValue(String.class);
                     userEmail=postSnapshot.child("email_address").getValue(String.class);
                     userPassword=postSnapshot.child("password").getValue(String.class);
                     userDescription=postSnapshot.child("user_description").getValue(String.class);
                     profile=postSnapshot.child("profile_picture").getValue(String.class);
                     cover=postSnapshot.child("cover_picture").getValue(String.class);
                     flag=true;

                 }
                 if(flag){
                     name.setText(fullname);
                     pass.setText(userPassword);
                     email.setText(userEmail);
                     description.setText(userDescription);

                     if(!profile.equals("")){
                         progressDialog.setTitle("loading profile picture");
                         byte[] decodedString = Base64.decode(profile, Base64.DEFAULT);
                         Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                         //update the image view
                         imgView.setImageBitmap(decodedByte);
                     }
                     if(!cover.equals("")){
                         progressDialog.setTitle("loading cover picture");
                         byte[] decodedString = Base64.decode(cover, Base64.DEFAULT);
                         Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                         //update the image view
                         coverPhoto.setImageBitmap(decodedByte);
                     }
                 }
                 progressDialog.dismiss();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
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

    public void OnUpdate(){
        final String uid=firebaseRef.getInstance().getCurrentUser();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading changes....");
        progressDialog.show();

        DatabaseReference getUser=firebaseRef.getInstance().getMyRef().child("User").child(uid);
        getUser.child("full_name").setValue(name.getText().toString());
        getUser.child("user_description").setValue(description.getText().toString());
        getUser.child("password").setValue(pass.getText().toString());
        getUser.child("email_address").setValue(email.getText().toString());

        progressDialog.dismiss();
       // Query updateDetails=firebaseRef.getInstance().getMyRef().child("User").orderByChild("uid").equalTo(uid);
       /* updateDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    firebaseRef.getInstance().getMyRef().child("User").child(uid).child("full_name").setValue(name.getText().toString());
                    firebaseRef.getInstance().getMyRef().child("User").child(uid).child("user_description").setValue(description.getText().toString());
                    firebaseRef.getInstance().getMyRef().child("User").child(uid).child("password").setValue(pass.getText().toString());
                    firebaseRef.getInstance().getMyRef().child("User").child(uid).child("email_address").setValue(email.getText().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    public void saveChanges(View v){
        String getEmail=email.getText().toString();
        String getPassword=pass.getText().toString();
        String getname=name.getText().toString();
        String getDescription=description.getText().toString();
        //update db
        OnUpdate();
        firebaseRef.getInstance().getUser().setEmail_address(getEmail);
        firebaseRef.getInstance().getUser().setFull_name(getname);
        firebaseRef.getInstance().getUser().setPassword(getPassword);
        firebaseRef.getInstance().getUser().setUser_description(getDescription);
        //onBackPressed();//CHECK
        Intent i=new Intent(editProfile.this, profile.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(i);

    }

    public void changeCover(View v){
        boolean isPermissionGranted = isPermissionGranted(editProfile.this, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE);
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
                    Intent i=new Intent(editProfile.this, profile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                @Override
                public void permissionForeverDenied() {
                    // user has check never ask again
                    // you need to open setting manually
                    openSettingsApp(editProfile.this);
                }
            });
        }

    }

    protected void onActivityResult(int RequestCode, int ResultCode, Intent i) {
        super.onActivityResult(RequestCode, ResultCode, i);
        try {
            if (RequestCode == RESULT_LOAD_IMG && ResultCode == RESULT_OK && null != i) {
                 filePath = i.getData();
                String[] PathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(filePath, PathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(PathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView cover=findViewById(R.id.CoverPhoto);
                cover.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                String uid=firebaseRef.getInstance().getCurrentUser();

                final StorageReference mUserCoverPic = FirebaseStorage.getInstance().getReference()
                        .child("coverPictures").child(uid).child("coverPic.jpg");


                mUserCoverPic.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        mUserCoverPic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                //Do what you want with the url
                                Toast.makeText(editProfile.this, "Upload Done", Toast.LENGTH_LONG).show();

                                firebaseRef.getInstance().getUserNode().child("cover_picture").setValue(uri.toString());
                                progressDialog.dismiss();
                                firebaseRef.getInstance().getUser().setCover_picture(uri.toString());
                                SharedPreferences.Editor editor=firebaseRef.getSp(editProfile.this).edit();
                                editor.putString("cover",uri.toString());
                                editor.apply();

                                //Intent i=new Intent(editProfile.this, profile.class);
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                               // startActivity(i);

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(editProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });





            } else {
                Toast.makeText(getApplicationContext(), "Image not selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

}
