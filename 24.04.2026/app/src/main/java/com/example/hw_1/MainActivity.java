package com.example.hw_1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        listView.setAdapter(adapter);

        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("students");

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                list.clear();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String group = snapshot.child("group").getValue(String.class);

                    list.add(name + " - " + group);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }
}