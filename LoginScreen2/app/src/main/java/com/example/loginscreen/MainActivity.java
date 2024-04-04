package com.example.loginscreen;

import android.os.Bundle;
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

    Button CreateBtn;
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
        SubmitBtn = (Button)findViewById(R.id.Submit);
        CreateBtn = (Button)findViewById(R.id.CreateBtn);
        //Set click listener to do something when it clicks
        SubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                UserNameEntry = findViewById(R.id.Username);
                UserName = UserNameEntry.getText().toString();
                UserNameEntry.setText("");

                Intent intent = new Intent(MainActivity.this, Password.class);
                startActivity(intent);

            }
        });
        CreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Redirect to Create User page
                Intent intent = new Intent(MainActivity.this, CreateUser.class);
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