package com.example.loginscreen;
import com.example.loginscreen.UtilityCommunication.*;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class TopLevelController {

    private ServiceController SC = new ServiceController();
    private AuthController AC = new AuthController();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String Uid = mAuth.getCurrentUser().getUid();

    public void SendChatDBReq(String chatIdentifier, String SenderEmail, String content) throws Exception {
        //Encrypt
        String[] data = {chatIdentifier,SenderEmail,content};
        String ticket = UtilityCommunication.encryptTicket(data,Uid);
        SC.SendChattoDB(ticket);

    }

    public void ReportChatReq(String chatIdentifier) throws Exception {
        String[] data = {chatIdentifier};
        String ticket = UtilityCommunication.encryptTicket(data,Uid);
        SC.ReportChat(ticket);

    }

    public void SignOutReq(){
        AC.SignOut();
    }

    public void UserCreateReq(String type, String email) throws Exception {
        String[] data = {type,email};
        String ticket = UtilityCommunication.encryptTicket(data,Uid);
        AC.UserCreate(ticket);
    }
}
