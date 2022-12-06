package com.husamali.khatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chatting extends AppCompatActivity {
    ImageView back;
    ImageView dp;
    TextView name;
    ImageView send;
    EditText msg;
    ImageView plus;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    MessageAdapter messageAdapter;
    List<ChatModel> chat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        back = findViewById(R.id.backChatting);
        name = findViewById(R.id.name);
        dp = findViewById(R.id.dp);
        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        plus = findViewById(R.id.plus);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);


        intent = getIntent();
        String userID = intent.getStringExtra("userID");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);


        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String final_msg = msg.getText().toString();
                if (final_msg!=""){
                    sendMessage(firebaseUser.getUid(), userID , final_msg);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Type a message", Toast.LENGTH_LONG).show();
                }
                msg.setText("");
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile user = snapshot.getValue(UserProfile.class);
                name.setText(user.getUsername());
                Glide.with(getApplicationContext()).load(user.getDp()).into(dp);

                readMessages(firebaseUser.getUid(), userID, user.getDp());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendMessage(String sender, String receiver, String final_msg) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender" , sender);
        hashMap.put("receiver" , receiver);
        hashMap.put("message" , final_msg);

        databaseReference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(String myID, String userID, String imageURL){
        chat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chat.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myID) && chatModel.getSender().equals(userID) ||
                            chatModel.getReceiver().equals(userID)  && chatModel.getSender().equals(myID)){
                        chat.add(chatModel);
                    }

                    messageAdapter = new MessageAdapter(getApplicationContext(), chat, imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}