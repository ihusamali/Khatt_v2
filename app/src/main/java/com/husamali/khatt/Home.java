package com.husamali.khatt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Chats(),"chats").commit();
        bottomNav.getMenu().findItem(R.id.bNavChat).setChecked(true);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bNavChat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Chats(),"chats").addToBackStack(null).commit();
                }else if(item.getItemId() == R.id.bNavCall){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new Calls(),"calls").addToBackStack(null).commit();
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
                }else if(getSupportFragmentManager().findFragmentByTag("calls") != null && getSupportFragmentManager().findFragmentByTag("calls").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavCall).setChecked(true);
                }else if(getSupportFragmentManager().findFragmentByTag("contacts") != null && getSupportFragmentManager().findFragmentByTag("contacts").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavContact).setChecked(true);
                }else if(getSupportFragmentManager().findFragmentByTag("bluetooth") != null && getSupportFragmentManager().findFragmentByTag("bluetooth").isVisible()){
                    bottomNav.getMenu().findItem(R.id.bNavBluetooth).setChecked(true);
                }
            }
        });
    }
}