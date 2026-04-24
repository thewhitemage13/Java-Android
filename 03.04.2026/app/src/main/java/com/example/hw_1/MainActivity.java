package com.example.hw_1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ChatItem> chatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatItems = new ArrayList<>();

        chatItems.add(new ChatItem("JC", "Jornal da Cidade Online", "https://www.jornaldacidade...", "13:30", "359"));
        chatItems.add(new ChatItem("HT", "Hindustan Times - Official", "India says another big update...", "08:30", "126"));
        chatItems.add(new ChatItem("FT", "Financial Times: Coronavirus", "School must always teach...", "Wed", "14"));
        chatItems.add(new ChatItem("SM", "Saved Messages", "Draft: Monospaced", "Wed", "1"));
        chatItems.add(new ChatItem("₿", "Legit doubler investment site", "Happy Eid Mubarak 😍😍", "Fri", "5"));
        chatItems.add(new ChatItem("TG", "Telegram News", "New update is available", "10:45", "8"));
        chatItems.add(new ChatItem("IT", "IT News Ukraine", "Java Android homework", "12:20", "22"));

        ChatAdapter adapter = new ChatAdapter(chatItems);
        recyclerView.setAdapter(adapter);
    }
}