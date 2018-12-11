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
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
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


import com.example.apple.tabssample.Classes.Event;
import com.example.apple.tabssample.Classes.Trip;
import com.example.apple.tabssample.Fragments.DatePickerFragment;
import com.example.apple.tabssample.Fragments.TimePickerFragment;
import com.example.apple.tabssample.MainActivity;
import com.example.apple.tabssample.R;
import com.example.apple.tabssample.firebaseRef;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Vector;

public class CreateEvent extends AppCompatActivity {
    Button createEvent;
    EditText eventName, eventInfo;
    TextView eventPlace , eventStartDate, eventEndDate, eventStartTime, eventEndTime;
    String getEventName, getEventStartDate, getEventEndDate, getEventLocation, getEventDescription, getEventStartTime, getEventEndTime;
    Vector<String> picture = new Vector<>();
    ImageView image;
    private static int RESULT_LOAD_IMG=1;
    Uri filePath;
    int done;
    String pic;
    Toolbar toolbar;

    private final static int EVENT_PLACE_PICKER_REQUEST = 777;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createEvent = findViewById(R.id.createEvent);
        eventName = findViewById(R.id.eventName);
        eventStartDate = findViewById(R.id.eventStartDate);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndDate = findViewById(R.id.eventEndDate);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventInfo = findViewById(R.id.moreEventInfo);
        eventPlace = findViewById(R.id.eventPlace);

        image = findViewById(R.id.eventImage);

    }

    public void upload_event_image(View v)
    {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void selectEventLocation(View v) {

        //Log.wtf("Event location btn click: : ", "Place Picker");
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(CreateEvent.this), EVENT_PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //gets the result here
        // Log.d("On Activity result: ", "Place Picker");

        if (requestCode == EVENT_PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                eventPlace.setText(toastMsg);
            } else {
                eventPlace.setText(" ");
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

            //encode string
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//            final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            picture.add(imageString);
//            pic = imageString;




        }


    }


    public void showStartTimePicker(View v)
    {

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setVal(0);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showEndTimePicker(View v)
    {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setVal(1);
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showStartDatePicker(View v)
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setVal(0);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndDatePicker(View v)
    {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setVal(1);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void create_event(View v) {

        getEventName = eventName.getText().toString();
        getEventStartDate = eventStartDate.getText().toString();
        getEventEndDate = eventEndDate.getText().toString();
        getEventStartTime = eventStartTime.getText().toString();
        getEventEndTime = eventEndTime.getText().toString();
        getEventDescription = eventInfo.getText().toString();
        getEventLocation = eventPlace.getText().toString();


        if (TextUtils.isEmpty(eventName.getText())) {
            Toast.makeText(getApplicationContext(), R.string.error_msg_event_name, Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(eventPlace.getText())) {
            Toast.makeText(getApplicationContext(), R.string.error_msg_event_location, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(eventStartDate.getText())) {
            Toast.makeText(getApplicationContext(), R.string.error_msg_event_start_date, Toast.LENGTH_SHORT).show();
        } else {



            String  getEventStartDateAndTime, getEventEndDateAndTime;
            getEventStartDateAndTime = getEventStartDate + getEventStartTime;
            getEventEndDateAndTime = getEventEndDate + getEventEndTime;

            Event new_event = new Event(" ", getEventStartDateAndTime, getEventEndDateAndTime, getEventName, getEventDescription, getEventLocation, picture, null, null, null);
            final DatabaseReference eventsRef = firebaseRef.getInstance().getMyRef().child("Events");
            final String newKey = eventsRef.push().getKey();
            //DatabaseReference key = firebaseRef.getInstance().getMyRef().child("Events").push();

            new_event.setId(newKey);
            eventsRef.child(newKey).setValue(new_event);

            final StorageReference mEventPicture = FirebaseStorage.getInstance().getReference()
                    .child("eventPictures").child(newKey).child("eventPic.jpg");

            mEventPicture.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    mEventPicture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            //Do what you want with the url
                            Toast.makeText(CreateEvent.this, R.string.upload_done, Toast.LENGTH_LONG).show();

                            eventsRef.child(newKey).child("picture").setValue(uri.toString());
                            //progressDialog.dismiss();


                            Intent i=new Intent(CreateEvent.this, ViewEvent.class);
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


            //eventsRef.child("picture").child("0").setValue(pic);
        }
    }

    public void onUserSelectValueStartTime(int hourOfDay, int minute) {


        eventStartTime.setText(R.string.hour + String.valueOf(hourOfDay)+" " +R.string.minute + String.valueOf(minute));
        //  Toast.makeText(getApplicationContext(), "hour: " + hourOfDay + " minute: " + minute, Toast.LENGTH_SHORT).show();

    }

    public void onUserSelectValueEndTime(int hourOfDay, int minute) {

        eventEndTime.setText(R.string.hour + String.valueOf(hourOfDay)+" " +R.string.minute + String.valueOf(minute));
        // Toast.makeText(getApplicationContext(), "hour:" + hourOfDay + " minute: " + minute, Toast.LENGTH_SHORT).show();

    }
    public void onUserSelectValueStartDate(int year, int month, int day) {

        eventStartDate.setText(R.string.day + String.valueOf(day) + " " + R.string.month + String.valueOf(day) + " "+ R.string.year + String.valueOf(year) + " ");
        //   Toast.makeText(getApplicationContext(), "year: " + year + " month: " + month + "day " + day, Toast.LENGTH_SHORT).show();

    }
    public void onUserSelectValueEndDate(int year, int month, int day) {

        eventEndDate.setText(R.string.day + String.valueOf(day) + " " + R.string.month + String.valueOf(day) + " "+ R.string.year + String.valueOf(year) + " ");
        // Toast.makeText(getApplicationContext(), "year: " + year + " month: " + month + "day " + day, Toast.LENGTH_SHORT).show();

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
