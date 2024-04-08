package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private List<Message> messageList = new ArrayList<>();
    private MessagesAdapter adapter;
    private RecyclerView messagesRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FloatingActionButton BackBtn;
    private String RecieverEmail;
    private String SenderEmail;
    private String chatIdentifier;
    private FirebaseAuth Mauth;

    private TopLevelController TLC = new TopLevelController();

    private Button ReportBtn;

    private Map<String,Object> Reportdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //setting variables for object on the screen, allows us to manipulate whats going on
        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        BackBtn = (FloatingActionButton)findViewById(R.id.BackFAB);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessagesAdapter(messageList);
        messagesRecyclerView.setAdapter(adapter);
        ReportBtn = findViewById(R.id.report_button);

        //get the reciever email from the previous screen's search
        Bundle bundle = getIntent().getExtras();
        try{
            RecieverEmail = bundle.getString("reciever");
            Log.v("firebase",RecieverEmail);
            Mauth = FirebaseAuth.getInstance();
            SenderEmail = Mauth.getCurrentUser().getEmail();

            //Create unique chat identifier
            String[] AlphabeticalOrder = new String[2];
            if (SenderEmail.compareTo(RecieverEmail)>0){
                AlphabeticalOrder[0] = SenderEmail;
                AlphabeticalOrder[1] = RecieverEmail;
            }else{
                AlphabeticalOrder[1] = SenderEmail;
                AlphabeticalOrder[0] = RecieverEmail;
            }
            chatIdentifier = AlphabeticalOrder[0]+"+"+AlphabeticalOrder[1];
        }catch (Exception e){
            chatIdentifier = bundle.getString("Reported");
        }

        //Load other messages
        loadMessages();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SendChatToDB();
                } catch (Exception e) {

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

        //Report a Chat
        ReportBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v("F","click");
                try {
                    ReportChat();
                } catch (Exception e) {
                }

            }
        });
    }

    private void loadMessages() {
        // This method should load existing messages into messageList
        // For simplicity, it's left empty. In a real app, load from database or network
        db.collection("Chats").document(chatIdentifier).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    try{
                        //Sort keys so messages load chronologically
                        Map<String,Object> entries = task.getResult().getData();
                        Object[] K = entries.keySet().toArray();
                        Arrays.sort(K);
                        for (Object e: K){
                            Map<String,String> entry = (Map<String, String>) entries.get(e.toString());
                            String message = entry.get("Content");
                            Log.v("Firebase",message);
                            if (entry.get("Sender").equals(SenderEmail)){
                                messageList.add(new Message(message, true));
                            }else if (entry.get("Sender").equals(RecieverEmail)){
                                messageList.add(new Message(message, false));
                            }else{
                                if(entry.get("Sender").equals(chatIdentifier.split("[+]")[0])){
                                    messageList.add(new Message(message,true));
                                }else{
                                    messageList.add(new Message(message,false));
                                }
                            }
                            adapter.notifyItemInserted(messageList.size() - 1);
                            messagesRecyclerView.scrollToPosition(messageList.size() - 1);

                        }
                    }catch (NullPointerException e){
                        Log.v("Chats","New chat");
                    }
                }
            }
        });

    }

    private void SendChatToDB() throws Exception {
        String content = messageInput.getText().toString();
        TLC.SendChatDBReq(chatIdentifier,SenderEmail,content);

            // Assuming every new message is sent by the user

        messageList.add(new Message(content, true));
        adapter.notifyItemInserted(messageList.size() - 1);
        messageInput.setText("");
        messagesRecyclerView.scrollToPosition(messageList.size() - 1); // Scroll to the bottom
    }

    private void ReportChat() throws Exception {
        TLC.ReportChatReq(chatIdentifier);

    }
}