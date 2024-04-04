package com.example.loginscreen;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Password extends AppCompatActivity {
    //Defining a variable for every attribute in the application UI (one for the button, one for the username box etc...)
    Button LoginBtn;
    Button BackBtn;
    EditText PasswordEntry;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This stuff is auto generated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.password);

        //Assign the actual button to the variable
        LoginBtn = (Button) findViewById(R.id.Login);
        BackBtn = (Button) findViewById(R.id.BackBtn);
        //Set click listener to do something when it clicks
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordEntry = findViewById(R.id.Password);
                Password = PasswordEntry.getText().toString();
                Log.v("Password", Password);
                PasswordEntry.setText("");

                Intent intent = new Intent(Password.this, SelectChat.class);
                startActivity(intent);

            }
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to Create User page
                Intent intent = new Intent(Password.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
