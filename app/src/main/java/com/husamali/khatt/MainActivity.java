package com.husamali.khatt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView khatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        khatt = findViewById(R.id.khatt);


//        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
//        khatt.startAnimation(animation1);

    }
}