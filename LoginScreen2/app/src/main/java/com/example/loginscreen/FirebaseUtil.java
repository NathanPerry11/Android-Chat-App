package com.example.loginscreen;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {

    // Define an interface for callback methods
    public interface FirestoreSessionCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public static void storeSessionKeyInFirestore(String userId, String sessionKey, FirestoreSessionCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        long currentTime = System.currentTimeMillis();
        long sessionDuration = 3600000; // For example, 1 hour = 3600000 milliseconds
        long expirationTime = currentTime + sessionDuration;

        Map<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("sessionKey", sessionKey);
        sessionInfo.put("expirationTime", expirationTime);

        db.collection("userSessions").document(userId).set(sessionInfo)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    if (callback != null) callback.onFailure(e);
                });
    }
}
