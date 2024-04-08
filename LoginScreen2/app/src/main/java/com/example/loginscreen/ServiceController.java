package com.example.loginscreen;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ServiceController {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Message> messageList = new ArrayList<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String Uid = mAuth.getCurrentUser().getUid();
    public void SendChattoDB(String ticket) throws Exception {
        //Decrypt
        String[] data = UtilityCommunication.decryptTicket(ticket,Uid);
        String chatIdentifier = data[0];
        String SenderEmail = data[1];
        String content = data[2];
        if (!content.isEmpty()) {

            //Set up database entry
            Map<String, Object> dbEntry = new HashMap<>();
            Map<String, String> entryData = new HashMap<>();


            //Add content of message and email of sender to entry
            entryData.put("Content", content);
            entryData.put("Sender", SenderEmail);


            //Use timestamp as unique identifier
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            dbEntry.put(timeStamp.toString(), entryData);


            //try to update db section, if it doesn't exist, set the first entry
            db.collection("Chats").document(chatIdentifier).update(dbEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Firebase", "Error writing document", e);
                            db.collection("Chats").document(chatIdentifier).set(dbEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        }
    }

    public void ReportChat(String ticket) throws Exception {
        String[] data = new String[0];
        try {
            data = UtilityCommunication.decryptTicket(ticket,Uid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String chatIdentifier = data[0];
        db.collection("Chats").document(chatIdentifier).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    try{
                        Map<String, Object> Reportdata = task.getResult().getData();
                        Map<String,Object> header = new HashMap<>();
                        header.put("Reason","Inappropriate");
                        db.collection("Reports").document(chatIdentifier).set(header);
                        for(String s: Reportdata.keySet()){
                            Log.v("S",s);
                            Map<String,Object> entry = new HashMap<>();
                            entry.put(s,Reportdata.get(s));
                            db.collection("Reports").document(chatIdentifier).update(entry).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.v("F","failed");
                                }
                            });
                        }
                    }catch (NullPointerException e){
                        Log.v("V","Empty Chat");
                    }
                }
            }
        });
    }

    public void CreateChatFromSearch(){

    }
}
