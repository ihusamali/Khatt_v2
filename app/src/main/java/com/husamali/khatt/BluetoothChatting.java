package com.husamali.khatt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BluetoothChatting extends AppCompatActivity {
    ImageView back;
    ImageView dp;
    TextView name;
    ImageView send;
    EditText msg;
    ImageView plus;

    List<ChattingBluetoothModel> chat;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chatting);
        back = findViewById(R.id.backChattingBluetooth);
        name = findViewById(R.id.nameBluetooth);
        dp = findViewById(R.id.dpBluetooth);
        send = findViewById(R.id.sendBluetooth);
        msg = findViewById(R.id.msgBluetooth);
        plus = findViewById(R.id.plusBluetooth);
        chat = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewBluetooth);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}