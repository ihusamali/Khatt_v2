package com.husamali.khatt;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScanDevices extends AppCompatActivity {
    RecyclerView devicesRecycler;
    BluetoothDevicesAdapter bluetoothDevicesAdapter;
    List<BluetoothDevices> ls;
    BluetoothAdapter bluetoothAdapter;
    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission not Granted", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
    BroadcastReceiver bluetoothDevicesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        ls.add(new BluetoothDevices(device.getName(),device.getAddress()));
                        bluetoothDevicesAdapter.notifyDataSetChanged();
                    }
                }else{
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        ls.add(new BluetoothDevices(device.getName(),device.getAddress()));
                        bluetoothDevicesAdapter.notifyDataSetChanged();
                    }

                }


            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                if(ls.size() == 0 ){
                    Toast.makeText(getApplicationContext(),"No new Devices Found",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"Click On Device to start chat",Toast.LENGTH_LONG).show();

                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_devices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN);
        }
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
        ls = new ArrayList<>();
        devicesRecycler = findViewById(R.id.devicesRecycler);
        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(ls,ScanDevices.this);
        devicesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        devicesRecycler.setAdapter(bluetoothDevicesAdapter);
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDevicesReceiver,intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothDevicesReceiver,intentFilter1);

    }
}