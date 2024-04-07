package com.example.loginscreen;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectChat extends AppCompatActivity {
    private List<Chat> chatList;

    private FloatingActionButton searchBtn;
    private String searchEntry;
    Button CreateBtn;
    FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void populateDummyChats() {
        chatList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            chatList.add(new Chat("chatId" + i, "Chat " + i));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_chat);
        CreateBtn = (Button)findViewById(R.id.CreateBtn);
        populateDummyChats();
        searchBtn = findViewById(R.id.SearchBtn);
        EditText searchBar = findViewById(R.id.search);


        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEntry = searchBar.getText().toString();
                db.collection("Users").document(searchEntry).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            try{
                                //Basically trying to force null pointer here to see if the document of this user exists
                                Map<String,Object> entries = task.getResult().getData();
                                entries.keySet();
                                Log.v("fF","yes");
                                //Only load if searched user exists
                                Intent intent = new Intent(SelectChat.this, ChatActivity.class);
                                String RecEmail = searchBar.getText().toString();
                                intent.putExtra("reciever",RecEmail);
                                startActivity(intent);
                            }catch (NullPointerException e){
                                Log.v("ff","New chat");
                            }
                        }else{
                            Log.v("bruh","ude");
                        }
                    }
                });
            }
        });
        RecyclerView chatListRecyclerView = findViewById(R.id.chat_list_recycler_view);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        ChatListAdapter adapter = new ChatListAdapter(chatList, chat -> {
//            Intent intent = new Intent(SelectChat.this, ChatActivity.class);
//            EditText searchEntry = findViewById(R.id.search);
//            String RecEmail = searchBar.getText().toString();
//            intent.putExtra("reciever",RecEmail);
//            intent.putExtra("chatId", chat.getChatId());
//            startActivity(intent);
//        });
//        chatListRecyclerView.setAdapter(adapter);
        CreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Redirect to Create User page
                db.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String permission = task.getResult().getData().get("Type").toString();
                        if (permission.equals("Admin")){
                            Intent intent = new Intent(SelectChat.this, CreateUser.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SelectChat.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

            }
        });
    }

}