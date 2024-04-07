package com.example.loginscreen;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectChat extends AppCompatActivity {
    private List<Chat> chatList;

    private FloatingActionButton searchBtn;
    private Button ReportBtn;
    private Button LogoutBtn;
    private String searchEntry;

    private FirebaseAuth Mauth = FirebaseAuth.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_chat);
        searchBtn = findViewById(R.id.SearchBtn);
        LogoutBtn = findViewById(R.id.LogoutButton);
        EditText searchBar = findViewById(R.id.search);
        ReportBtn = findViewById(R.id.ViewReportBtn);

        ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail = Mauth.getCurrentUser().getEmail();
                db.collection("Users").document(UserEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String permission = task.getResult().getData().get("Type").toString();
                        if (permission.equals("Admin")){
                            Intent intent = new Intent(SelectChat.this, CheckReport.class);
                            startActivity(intent);
                        }else{
                            ProgressBar permissionBar = findViewById(R.id.PermissionBar);
                            permissionBar.setVisibility(View.VISIBLE);
                            Toast.makeText(SelectChat.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            permissionBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchEntry = searchBar.getText().toString();
                CreateChatFromSearch(searchEntry);

            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mauth.signOut();
                Intent intent = new Intent(SelectChat.this, MainActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView chatListRecyclerView = findViewById(R.id.chat_list_recycler_view);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        populateChats(chatListRecyclerView);
    }

    private void populateChats(RecyclerView ChatView) {
        chatList = new ArrayList<>();
        db.collection("Chats").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String UserEmail = Mauth.getCurrentUser().getEmail();
                List<Chat> chats = new ArrayList<>();
                List<DocumentSnapshot> D = task.getResult().getDocuments();
                for(DocumentSnapshot d: D){
                    if (!d.getId().equals("NullUser")){
                        String[] s = d.getId().split("[+]");
                        if (s[0].equals(UserEmail)){
                            chatList.add(new Chat(s[1],s[1]));
                        }else if(s[1].equals(UserEmail)){
                            chatList.add(new Chat(s[0],s[0]));
                        }
                    }
                }
                ChatListAdapter adapter = new ChatListAdapter(chatList, chat -> {
                    Intent intent = new Intent(SelectChat.this, ChatActivity.class);
                    EditText searchEntry = findViewById(R.id.search);
                    intent.putExtra("reciever", chat.getChatName());
                    startActivity(intent);
                });
                ChatView.setAdapter(adapter);
            }
        });

    }

    private void CreateChatFromSearch(String searchEntry){
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
                        intent.putExtra("reciever",searchEntry);
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
}