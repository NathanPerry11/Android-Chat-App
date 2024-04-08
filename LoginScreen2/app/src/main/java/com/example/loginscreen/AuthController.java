package com.example.loginscreen;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AuthController {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String Uid = mAuth.getCurrentUser().getUid();
    public void SignOut(){
        mAuth.signOut();
    }

    public void UserCreate(String ticket) throws Exception {
        String[] data = UtilityCommunication.decryptTicket(ticket,Uid);
        String type = data[0];
        String email = data[1];
        HashMap<String,String> H = new HashMap<>();
        H.put("Type",type);
        db.collection("Users").document(email).set(H).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
