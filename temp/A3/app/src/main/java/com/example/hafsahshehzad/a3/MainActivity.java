package com.example.hafsahshehzad.a3;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating external folders
       boolean check= isExternalStorageWritable();
       if(check==true){
           createNewFolder(); //creating folder in SD card
           createDownloadFolder(); //creating download folder in sd card
           createSDcache(); //creating SD card cache 4
       }

        createInternalfolder(this); //creating internal folder
        createDownloadInternal(this); //creating download folder in internal folder
        createCache(this,"Internal cache file"); //creating internal cache
    }

    public boolean isExternalStorageWritable(){
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;

        }
        else{
            return false;
        }

    }


    public void createNewFolder(){
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), "SDCard Folder");
            if(!newFolder.exists()){
                newFolder.mkdirs();
            }
            else{
                Log.i("Tag","Folder already exists");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void createDownloadFolder(){
        File downloadfolder=new File(Environment.getExternalStorageDirectory()+File.separator+"SDCardDownload");
        if(!downloadfolder.exists()){
            downloadfolder.mkdirs();
        }
        else{
            Log.i("Tag","Folder already exists");
        }

    }

    public void createSDcache(){
        File cache=new File(Environment.getExternalStorageDirectory()+File.separator+"Cache");
        if(!cache.exists()){
            cache.mkdirs();
        }
        String CachePath=cache.toString();

        File file=new File(CachePath,"SDCardCachefile.txt");
    }

    public void createInternalfolder(Context context){
        File Testfolder = context.getDir("InternalFolder", Context.MODE_PRIVATE); //Creating an internal dir;
        if (!Testfolder.exists())
        {
            Testfolder.mkdirs();
        }
        else{
            Log.i("Tag","Folder already exists");
        }
    }

    public void createDownloadInternal(Context context){
        File folder = new File(getFilesDir() + "/" + "Internal Download Folder");
        if(!folder.exists()){
            folder.mkdirs();
        }
        else{
            Log.i("Tag","Folder already exists");
        }
    }

    public void createCache(Context context, String url){
        File file;
        try {
            String filename = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(filename, null, context.getCacheDir());
        }catch(IOException e){
            System.out.println(e.getMessage());
        }


    }


}


