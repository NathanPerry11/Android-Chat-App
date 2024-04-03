package com.example.loginscreen;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    //Defining a variable for every attribute in the application UI (one for the button, one for the username box etc...)
    Button SubmitBtn;
    EditText UserNameEntry;
    EditText PasswordEntry;
    TextView Display;
    String UserName;
    String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This stuff is auto generated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Assign the actual button to the variable
        SubmitBtn = (Button)findViewById(R.id.button);
        //Set click listener to do something when it clicks
        SubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Assign Username and Password box to the variables
                UserNameEntry = findViewById(R.id.Username);
                PasswordEntry = findViewById(R.id.Password);
                UserName = UserNameEntry.getText().toString();
                Password = PasswordEntry.getText().toString();
                Log.v("Password",Password);
                UserNameEntry.setText("");
                PasswordEntry.setText("");

                Intent intent = new Intent(MainActivity.this, SelectChat.class);
                startActivity(intent);

            }
        });
        System.out.println(UserName);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}