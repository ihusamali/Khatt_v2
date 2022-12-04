package com.husamali.khatt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.MyViewHolder>{
    List<BluetoothDevices> ls;
    Context c;

    public BluetoothDevicesAdapter(List<BluetoothDevices> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }

    @NonNull
    @Override
    public BluetoothDevicesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.row_devices,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDevicesAdapter.MyViewHolder holder, int position) {
        holder.name.setText(ls.get(position).getName());
        holder.address.setText(ls.get(position).getAddress());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,address;
        LinearLayout row;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameDevice);
            address = itemView.findViewById(R.id.addressDevice);
            row = itemView.findViewById(R.id.rowPairedDevices);

        }

    }
}