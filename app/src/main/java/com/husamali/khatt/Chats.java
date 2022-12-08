package com.husamali.khatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.onesignal.OneSignal;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {

    RecyclerView recyclerView;

    ContactAdapter contactAdapter;
    List <UserProfile> users;

//    FirebaseUser firebaseUser;
//    DatabaseReference databaseReference;
//
//    List<ChatList> usersList;

    ImageView profile;

    String idd;

    //de8d2fd4-146e-4481-aa6c-263f99d50e01

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);

        idd = OneSignal.getDeviceState().getUserId();
        Toast.makeText(getContext(), "Hello" + idd, Toast.LENGTH_LONG).show();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        users = new ArrayList<>();
        contactAdapter = new ContactAdapter(getContext(), users );
        recyclerView.setAdapter(contactAdapter);

        profile = view.findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageProfile.class);
                startActivity(intent);
            }
        });
//        usersList = new ArrayList<>();

        getUsers();


//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//

//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                usersList.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
//
//                    usersList.add(chatList);
//                }
//
//                chatList();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
        return view;
    }

    private void getUsers() {
    users.clear();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot ds: snapshot.getChildren()){
                String id = ds.getKey();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Chats").child(id);
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("sender").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(snapshot.child("receiver").getValue(String.class));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserProfile user = snapshot.getValue(UserProfile.class);
                                    boolean check = true;
                                    for (int i=0;i<users.size();i++){
                                        if(users.get(i).getUserUID().equals(user.getUserUID())){
                                            check=false;
                                            break;
                                        }
                                    }
                                    if(check){
                                        users.add(user);
                                        contactAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }else if(snapshot.child("receiver").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(snapshot.child("sender").getValue(String.class));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserProfile user = snapshot.getValue(UserProfile.class);
                                    boolean check = true;
                                    for (int i=0;i<users.size();i++){
                                        if(users.get(i).getUserUID().equals(user.getUserUID())){
                                            check=false;
                                            break;
                                        }
                                    }
                                    if(check){
                                        users.add(user);
                                        contactAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


    }
//
//    private void chatList() {
//        users = new ArrayList<>();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
//
//                    for (ChatList chatlist : usersList){
//
//                       //if (userProfile.getUserUID().equals(chatlist.getId())){
//                            users.add(userProfile);
//                        //}
//                    }
//
//                    contactAdapter = new ContactAdapter(getContext(), users );
//                    recyclerView.setAdapter(contactAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


}
