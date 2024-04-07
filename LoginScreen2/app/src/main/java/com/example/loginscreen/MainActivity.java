package com.example.loginscreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;


public class MainActivity extends AppCompatActivity {
    //Defining a variable for every attribute in the application UI (one for the button, one for the username box etc...)
    Button SubmitBtn;

    Button CreateBtn;
    EditText UserNameEntry;
    EditText PasswordEntry;
    TextView Display;

    String email, password;
    ProgressBar submitProgressBar;

    FirebaseAuth mAuth;

    String sessionKey;
    long sessionDurationInMillis = 3600000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This stuff is auto generated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        UserNameEntry = findViewById(R.id.Username);
//        PasswordEntry = findViewById(R.id.Password);

        //Assign the actual button to the variable
        SubmitBtn = (Button)findViewById(R.id.Submit);

        UserNameEntry = findViewById(R.id.Username);
        PasswordEntry = findViewById(R.id.Password);
        mAuth = FirebaseAuth.getInstance();

        submitProgressBar = findViewById(R.id.LoginProgressBar);
        //Set click listener to do something when it clicks
        SubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Assign Username and Password box to the variables
                submitProgressBar.setVisibility(View.VISIBLE);
                email = UserNameEntry.getText().toString();
                password = PasswordEntry.getText().toString();
                Log.v("Password", password);
                UserNameEntry.setText("");
                PasswordEntry.setText("");

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    submitProgressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    submitProgressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    sessionKey = SecurityUtil.generateSecureSessionKey();
                                    if (sessionKey != null) {
                                        FirebaseUtil.storeSessionKeyInFirestore(user.getUid(), sessionKey, new FirebaseUtil.FirestoreSessionCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(getApplicationContext(), "Session key stored successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onFailure(Exception e) {
                                                Toast.makeText(MainActivity.this, "Failed to store session key. Please try again.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        // Handle the case where sessionKey is null
                                        Toast.makeText(MainActivity.this, "Error generating session key. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(getApplicationContext(), SelectChat.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    submitProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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

        System.out.println(email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}
