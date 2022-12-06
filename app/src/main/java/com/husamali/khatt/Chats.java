package com.husamali.khatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {

    RecyclerView recyclerView;

    ContactAdapter contactAdapter;
    List <UserProfile> users;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    List<String> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                    if (chatModel.getSender().equals(firebaseUser.getUid())){
                        usersList.add(chatModel.getReceiver());
                    }
                    if (chatModel.getReceiver().equals(firebaseUser.getUid())){
                        usersList.add(chatModel.getSender());
                    }

                    readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats() {
        users = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserProfile user = dataSnapshot.getValue(UserProfile.class);

                    for (String id : usersList){
                        if (user.getUserUID().equals(id)){
                            if (users.size() != 0) {
                                for (UserProfile user1 : users){
                                    if(!user.getUserUID().equals(user1.getUserUID())){
                                        users.add(user);
                                    }
                                }
                            }
                        }
                    }
                }

                contactAdapter = new ContactAdapter(getContext(), users);
                recyclerView.setAdapter(contactAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
