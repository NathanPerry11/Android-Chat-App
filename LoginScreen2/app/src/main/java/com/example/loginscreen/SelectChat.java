package com.example.loginscreen;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectChat extends AppCompatActivity {
    private List<Chat> chatList;

    private FloatingActionButton searchBtn;
    private String searchEntry;

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

        populateDummyChats();
        searchBtn = findViewById(R.id.SearchBtn);
        EditText searchBar = findViewById(R.id.search);
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
    }
}