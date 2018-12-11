package com.example.apple.tabssample.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.tabssample.Classes.Trip;
import com.example.apple.tabssample.Classes.User;
import com.example.apple.tabssample.Fragments.DatePickerFragment;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class createTrip extends AppCompatActivity {
    Button create;
    EditText tripName, tripDescription;
    TextView  tripStartLocation, tripEndLocation, tripStartDate, tripEndDate;
    String getTripName, getTripStartDate, getTripEndDate, getTripStartLocation, getTripEndLocation, getTripDescription;
    ArrayList<String> pictures = new ArrayList<>();
    private final static int TRIP_START_PLACE_PICKER_REQUEST = 999;
    private final static int TRIP_END_PLACE_PICKER_REQUEST = 888;
    protected GeoDataClient mGeoDataClient;
    ImageView image;
    private static int RESULT_LOAD_IMG=1;
    Uri filePath;
    String pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        mGeoDataClient = Places.getGeoDataClient(this, null);

        create=findViewById(R.id.createTripButton);
        tripName=findViewById(R.id.tripName);
        tripStartDate=findViewById(R.id.tripStartDate);
        tripStartLocation=(TextView) findViewById(R.id.tripStartLocation);
        tripEndDate=findViewById(R.id.tripEndDate);
        tripEndLocation=(TextView)findViewById(R.id.tripEndLocation);
        tripDescription=findViewById(R.id.tripDescription);
        image = findViewById(R.id.tripImage);
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
            case R.id.logout:
                startActivity(new Intent(this, homepage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //gets the result here
        // Log.d("On Activity result: ", "Place Picker");

        if (requestCode ==  TRIP_START_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                tripStartLocation.setText(toastMsg);
            }
            else
            {
                tripStartLocation.setText(" ");
            }
        }

        else if (requestCode ==  TRIP_END_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                tripEndLocation.setText(toastMsg);
            }
            else
            {
                tripEndLocation.setText(" ");
            }

        }

        else if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data)
        {

            filePath = data.getData();
            String[] PathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(filePath, PathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(PathColumn[0]);
            pic = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(pic);
            image.setImageBitmap(bitmap);

        }


    }


    public void selectEndLocation(View v)
    {
        // code to add end location

        // Log.wtf("End location btn click: : ", "Place Picker");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(createTrip.this), TRIP_END_PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onUserSelectValueEndDate(int year, int month, int day)
    {
        tripEndDate.setText(R.string.day + String.valueOf(day) + " " + R.string.month + String.valueOf(day) + " "+ R.string.year + String.valueOf(year) + " ");
        //  Toast.makeText(getApplicationContext(), "year: " + year + " month: " + month + "day " + day, Toast.LENGTH_SHORT).show();

    }
    public void onUserSelectValueStartDate(int year, int month, int day)
    {
        tripStartDate.setText(R.string.day + String.valueOf(day) + " " + R.string.month + String.valueOf(day) + " "+ R.string.year + String.valueOf(year) + " ");
        // Toast.makeText(getApplicationContext(), "year: " + year + " month: " + month + "day " + day, Toast.LENGTH_SHORT).show();

    }

    public void add_trip_photo(View v)
    {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    public void selectStartDate(View v)
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setVal(2);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectEndDate(View v)
    {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setVal(3);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    public void selectStartLocation(View v)
    {
        // code to add start location

        //Log.wtf("Start location btn click: : ", "Place Picker");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(createTrip.this), TRIP_START_PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void create_trip(View v){

        getTripName = tripName.getText().toString();
        getTripDescription = tripDescription.getText().toString();
        getTripStartDate= tripStartDate.getText().toString();
        getTripEndDate = tripEndDate.getText().toString();
        getTripStartLocation = tripStartLocation.getText().toString();
        getTripEndLocation = tripEndLocation.getText().toString();

        if(TextUtils.isEmpty(tripName.getText())){
            Toast.makeText(getApplicationContext(),R.string.error_msg_trip_name, Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(tripStartLocation.getText())){
            Toast.makeText(getApplicationContext(),R.string.error_msg_trip_start_location, Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tripStartDate.getText())){
            Toast.makeText(getApplicationContext(),R.string.error_msg_trip_start_date, Toast.LENGTH_SHORT).show();
        }
        else{
            //add trip to db

            pictures.add(" ");
            pictures.add(" ");

            Trip new_trip=new Trip(getTripName, getTripDescription, getTripStartDate, getTripEndDate, getTripStartLocation, getTripEndLocation);
            final DatabaseReference eventsRef = firebaseRef.getInstance().getMyRef().child("Trip");
            final String newKey = eventsRef.push().getKey();
            //DatabaseReference key = firebaseRef.getInstance().getMyRef().child("Events").push();

            new_trip.setTripID(newKey);
            eventsRef.child(newKey).setValue(new_trip);


            final StorageReference mEventPicture = FirebaseStorage.getInstance().getReference()
                    .child("tripPictures").child(newKey).child("tripPic.jpg");

            mEventPicture.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    mEventPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            //Do what you want with the url
                            Toast.makeText(createTrip.this, R.string.upload_done, Toast.LENGTH_LONG).show();

                            eventsRef.child(newKey).child("picture").setValue(uri.toString());
                            //progressDialog.dismiss();


                            Intent i=new Intent(createTrip.this, TripPreview.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();
                    //Toast.makeText(setPicture.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


            UploadTask uploadTask = mEventPicture.putFile(filePath);


        }
    }


}
