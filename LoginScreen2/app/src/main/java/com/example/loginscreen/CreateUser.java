package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.loginscreen.databinding.ActivityCreateUserBinding;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateUser extends AppCompatActivity {
    FloatingActionButton Backbtn;
    //Defining a variable for every attribute in the application UI (one for the button, one for the username box etc...)
    TextView UserNameEntry;
    TextView PasswordEntry;
    TextView PasswordConfirmationEntry;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //This stuff is auto generated
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_user);

        mAuth = FirebaseAuth.getInstance();
        UserNameEntry = findViewById(R.id.NewUserNameEntry);
        PasswordEntry = findViewById(R.id.editTextText);
        PasswordConfirmationEntry = findViewById(R.id.editTextText2);

        //redirect to the login page when you click the back button
        Backbtn = (FloatingActionButton) findViewById(R.id.BackFAB);
        buttonReg = findViewById(R.id.CreateUserSubmit);
        progressBar = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);

        buttonReg.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                progressBar.setVisibility(View.VISIBLE);
                String email, password, passwordConfirmation;
                email = String.valueOf(UserNameEntry.getText());
                password = String.valueOf(PasswordEntry.getText());
                passwordConfirmation = String.valueOf(PasswordConfirmationEntry.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(CreateUser.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(CreateUser.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (passwordConfirmation.equals(password)){
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(CreateUser.this, "Account Created",
                                                Toast.LENGTH_SHORT).show();
                                        // Move to MainActivity (login page) after successful registration
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish(); // Optional: finish this activity so the user cannot navigate back to it
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(CreateUser.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateUser.this, "Passwords don't match.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        }));
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