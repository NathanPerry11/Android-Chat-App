package com.example.loginscreen;

public class Chat {
    private String chatId;
    private String chatName;

    public Chat(String chatId, String chatName) {
        this.chatId = chatId;
        this.chatName = chatName;
    }

    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }
}
