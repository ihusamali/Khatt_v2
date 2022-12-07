package com.husamali.khatt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context c;
    List<UserProfile> contacts;

    public void setFilteredList(List<UserProfile> filteredList){
        this.contacts = filteredList;
        notifyDataSetChanged();
    }

    public ContactAdapter (Context c , List<UserProfile> ct){
        this.c = c;
        this.contacts = ct;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.contacts_row,parent,false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        UserProfile user = contacts.get(position);
        holder.name.setText(user.getUsername());
        Glide.with(c).load(user.getDp()).into(holder.dp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, Chatting.class);
                intent.putExtra("userID", user.getUserUID());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView dp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            dp = itemView.findViewById(R.id.dp);
        }
    }
}
