package com.example.hafsahshehzad.a2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RelativeSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_signup);

        Button registerButton= findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkbox=findViewById(R.id.checkBox);
                RadioGroup Gender=findViewById(R.id.Gender);

                int selectedID=Gender.getCheckedRadioButtonId();
                RadioButton rb=findViewById(selectedID);

                EditText email=findViewById(R.id.emailEt);
                EditText pass=findViewById(R.id.PasswordET);

                if(checkbox.isChecked()) {
                    if(TextUtils.isEmpty(email.getText().toString())|| TextUtils.isEmpty(pass.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Please enter missing values",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Email: " + email.getText().toString() + "Password: "
                                + pass.getText().toString() +
                                "Gender: " + rb.getText().toString() + " Terms and conditions selected", Toast.LENGTH_SHORT).show();


                    }
                }

                else{
                    Toast.makeText(getApplicationContext(),"Please verify agreement with Terms and Conditions to continue",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
