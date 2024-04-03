package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectChat extends AppCompatActivity {
    private List<Chat> chatList;

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

        RecyclerView chatListRecyclerView = findViewById(R.id.chat_list_recycler_view);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChatListAdapter adapter = new ChatListAdapter(chatList, chat -> {
            Intent intent = new Intent(SelectChat.this, ChatActivity.class);
            intent.putExtra("chatId", chat.getChatId());
            startActivity(intent);
        });
        chatListRecyclerView.setAdapter(adapter);
    }
}