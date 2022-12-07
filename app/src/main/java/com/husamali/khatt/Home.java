package com.husamali.khatt;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNetworkAvailable(getApplicationContext()))
        {
            Toast.makeText(getApplicationContext(), "Internet Available : " , Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Internet not Available : " , Toast.LENGTH_SHORT).show();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Bluetooth(),"bluetooth").commit();
//            bottomNav.getMenu().findItem(R.id.bNavBluetooth).setChecked(true);
        }
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Chats(),"chats").commit();
        bottomNav.getMenu().findItem(R.id.bNavChat).setChecked(true);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bNavChat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Chats(),"chats").addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.bNavContact){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Contacts(),"contacts").addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.bNavBluetooth){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Bluetooth(),"bluetooth").addToBackStack(null).commit();
                }
                return true;
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().findFragmentByTag("chats") != null && getSupportFragmentManager().findFragmentByTag("chats").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavChat).setChecked(true);
                }else if(getSupportFragmentManager().findFragmentByTag("contacts") != null && getSupportFragmentManager().findFragmentByTag("contacts").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavContact).setChecked(true);
                }else if(getSupportFragmentManager().findFragmentByTag("bluetooth") != null && getSupportFragmentManager().findFragmentByTag("bluetooth").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavBluetooth).setChecked(true);
                }
            }
        });
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}