package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.loginscreen.databinding.ActivityCreateUserBinding;

public class CreateUser extends AppCompatActivity {
    FloatingActionButton Backbtn;
    //Defining a variable for every attribute in the application UI (one for the button, one for the username box etc...)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This stuff is auto generated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_user);

        //redirect to the login page when you click the back button
        Backbtn = (FloatingActionButton) findViewById(R.id.BackFAB);
        Backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Redirect to Create User page
                Intent intent = new Intent(CreateUser.this, MainActivity.class);
                startActivity(intent);

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CreateHome), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}