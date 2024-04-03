package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();
    private MessagesAdapter adapter;
    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private Button sendButton;

    private FloatingActionButton BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        BackBtn = (FloatingActionButton)findViewById(R.id.BackFAB);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessagesAdapter(messageList);
        messagesRecyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = messageInput.getText().toString();
                if (!content.isEmpty()) {
                    // Assuming every new message is sent by the user
                    messageList.add(new Message(content, true));
                    adapter.notifyItemInserted(messageList.size() - 1);
                    messageInput.setText("");
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1); // Scroll to the bottom
                }
            }
        });
        //Redirect to select chat on back button click
        BackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Redirect to Create User page
                Intent intent = new Intent(ChatActivity.this, SelectChat.class);
                startActivity(intent);

            }
        });

        // Initialize messageList with existing messages if available
        loadMessages();
    }

    private void loadMessages() {
        // This method should load existing messages into messageList
        // For simplicity, it's left empty. In a real app, load from database or network
    }
}