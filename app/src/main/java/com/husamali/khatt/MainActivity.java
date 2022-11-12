package com.husamali.khatt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView khatt;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        khatt = findViewById(R.id.khatt);
        handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(MainActivity.this, Home.class);
            startActivity(intent);
            finish();
        },1000);
    }
}