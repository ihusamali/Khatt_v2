package com.husamali.khatt;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScanDevices extends AppCompatActivity {
    RecyclerView devicesRecycler;
    BluetoothDevicesAdapter bluetoothDevicesAdapter;
    List<BluetoothDevices> ls;
    SwipeRefreshLayout swipeRefreshLayout;
//    List<BluetoothDevices> ls1;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver bluetoothDevicesReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_devices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutScan);
        ls = new ArrayList<>();
//        ls1 = new ArrayList<>();
        devicesRecycler = findViewById(R.id.devicesRecycler);
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(ls,ScanDevices.this);
        devicesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        devicesRecycler.setAdapter(bluetoothDevicesAdapter);
        getDevices();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bluetoothDevicesAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        bluetoothAdapter.startDiscovery();
        bluetoothDevicesReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    ls.add(new BluetoothDevices(device.getName(),device.getAddress()));
                    bluetoothDevicesAdapter.notifyDataSetChanged();
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDevicesReceiver, filter);


    }

    private void getDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices !=null && pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                ls.add(new BluetoothDevices(device.getName(),device.getAddress()));
                bluetoothDevicesAdapter.notifyDataSetChanged();
            }
        }
    }

}