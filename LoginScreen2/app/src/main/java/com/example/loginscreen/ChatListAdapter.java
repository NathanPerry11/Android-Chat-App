package com.example.loginscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private LayoutInflater inflater;
    private OnChatClickListener chatClickListener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatListAdapter(List<Chat> chatList, OnChatClickListener chatClickListener) {
        this.chatList = chatList;
        this.chatClickListener = chatClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View itemView = inflater.inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat, chatClickListener);
    }

    @Override
    public int getItemCount() {
        return chatList != null ? chatList.size() : 0;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatNameTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatNameTextView = itemView.findViewById(R.id.chat_name);
        }

        public void bind(final Chat chat, final OnChatClickListener listener) {
            chatNameTextView.setText(chat.getChatName());
            itemView.setOnClickListener(v -> listener.onChatClick(chat));
        }
    }
}