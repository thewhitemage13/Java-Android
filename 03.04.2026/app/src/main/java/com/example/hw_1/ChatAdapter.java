package com.example.hw_1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatItem> chatList;

    public ChatAdapter(List<ChatItem> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatItem item = chatList.get(position);

        holder.avatarText.setText(item.avatar);
        holder.chatTitle.setText(item.title);
        holder.chatMessage.setText(item.message);
        holder.chatTime.setText(item.time);
        holder.chatCount.setText(item.count);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView avatarText, chatTitle, chatMessage, chatTime, chatCount;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarText = itemView.findViewById(R.id.avatarText);
            chatTitle = itemView.findViewById(R.id.chatTitle);
            chatMessage = itemView.findViewById(R.id.chatMessage);
            chatTime = itemView.findViewById(R.id.chatTime);
            chatCount = itemView.findViewById(R.id.chatCount);
        }
    }
}
