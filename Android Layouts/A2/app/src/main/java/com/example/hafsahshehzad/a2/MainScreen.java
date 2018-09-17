package com.example.hafsahshehzad.a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button constraintButton= findViewById(R.id.constraintButton);
        constraintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this,ConstraintSignUp.class);
                startActivity(intent);

            }
        });

        Button relativeButton=findViewById(R.id.RelativeButton);
        relativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainScreen.this,RelativeSignup.class);
                startActivity(intent);
            }
        });


    }

}
