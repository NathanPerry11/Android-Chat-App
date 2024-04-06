package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String RecieverEmail = "this6@test.com";

    private FirebaseAuth Mauth;
    private int MessageID;

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
                //Get user name

                Mauth = FirebaseAuth.getInstance();
                String content = messageInput.getText().toString();

                if (!content.isEmpty()) {
                    //Get the logged in users email
                    String SenderEmail = Mauth.getCurrentUser().getEmail();
                    //Set up database entry
                    Map<String,Object> dbEntry = new HashMap<>();
                    Map<String,String> entryData = new HashMap<>();
                    //Add content of message and email of sender to entry
                    entryData.put("Content",content);
                    entryData.put("Sender",SenderEmail);
                    //Use timestamp as unique identifier
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                    dbEntry.put(timeStamp.toString(),entryData);
                    //Sort sender and reciever emails alphabetically so that id is symmetric regardless of who sent it
                    String[] AlphabeticalOrder = new String[2];
                    if (SenderEmail.compareTo(RecieverEmail)>0){
                        AlphabeticalOrder[0] = SenderEmail.replace("@","-");
                        AlphabeticalOrder[1] = RecieverEmail.replace("@","-");
                    }else{
                        AlphabeticalOrder[1] = SenderEmail;
                        AlphabeticalOrder[0] = RecieverEmail;
                    }
                    //try to update db section, if it doesn't exist, set the first entry
                    db.collection("Chats").document(AlphabeticalOrder[0]+"+"+AlphabeticalOrder[1]).update(dbEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "DocumentSnapshot successfully written!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firebase", "Error writing document", e);
                                    db.collection("Chats").document(AlphabeticalOrder[0]+"+"+AlphabeticalOrder[1]).set(dbEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Firebase", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Firebase", "Error writing document", e);
                                                }
                                            });
                                }
                            });

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